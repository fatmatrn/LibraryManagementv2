package com.turan.librarymanagement.security;


import com.turan.librarymanagement.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // Amacımız : Encoder, provider,manager, AuthTokenfilter yapılarını oluşturmak
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http.csrf().disable().
                //REstful API Stateless olacağını soyluyoruz
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().and().
                authorizeRequests().
                antMatchers("/login",
                            "/register",
                            "/actuator/info",// beanlar hakkında bilgi veriyor
                            "/actuator/health").permitAll().
                anyRequest().authenticated();
            //AuthTokenFilter en aşağıda oluşturduk cunku build işlmi için bildirmrmemiz gerikir
                // filter class dondurur yani  yani UsernamePasswordAuthenticationFilter onune kendi class entegre ediyoruz

                http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //*************** cors Ayarları ****************************
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*"). //"http:127.0.0.1/8080 diye spesific adresden gelenleri kabul et diye de diyebiliriz
                        allowedHeaders("*").// şimdilik test amaclı olduğı için hepsine allowed dedik
                        allowedMethods("*"); // tum methodlarada izin veriyoruz
            }
        };
    }
    //*******************SWAGGER***********************
    private static final String [] AUTH_WHITE_LIST= { //burası swagger uzerinden permitall gibi izin işlemleri String Array olarak setleme yaptık aşağıda kullanacağız
            "/v3/api-docs/**", // swagger
            "swagger-ui.html", //swagger
            "/swagger-ui/**", // swagger
            "/",  // bunalrı swagger içinde permitall ettiğimiz için permitall da da aynısını yazmamıza gerek yok
            "index.html",
            "/images/**",
            "/css/**",
            "/js/**"
    };
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        WebSecurityCustomizer customizer=new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                //Array yapıda cağırdık kullanımı kolay
                web.ignoring().antMatchers(AUTH_WHITE_LIST);// yukarıdakı listeyi swagger da kulanmıdğımız için bu şekilde kullanmalıyız
            }
        };
        return customizer;
    }
    //**************************************************************************



    // Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);// gomuluı olarak geliyor springsecurity de zorluk seviyesi
    }
    //provider
    @Bean
    public DaoAuthenticationProvider authProvider(){
      DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(userDetailsService);
      authenticationProvider.setPasswordEncoder(passwordEncoder());
      return authenticationProvider;

    }
    // AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http)throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).
                authenticationProvider(authProvider()).
                build();
    }

    //AuthTokenFilter (JWT token ureten ve valid eden class)
    @Bean
    public AuthTokenFilter authTokenFilter(){

        return new AuthTokenFilter();
    }



}
