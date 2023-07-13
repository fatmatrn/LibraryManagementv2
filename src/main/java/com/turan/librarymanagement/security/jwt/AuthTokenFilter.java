package com.turan.librarymanagement.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // requestin içinden jwt token elde edeceğiz
        String jwtToken =parseJwt(request); // jwt token requestten aldık
        // bundan sonra bilgilerimizi Security Context içine atmamaız lazım
        try {
            if(jwtToken!=null && jwtUtils.validateJwtToken(jwtToken)) {// burada gelen ile valide işlemi methodu ile kullnıcı kontrol ederiz
                String email= jwtUtils.getEmailFromToken(jwtToken);//gelkne token dan email i al method yapmıştık
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // valide edilen user bilgilerini SecurityContext e gonderme // ***  SecurityContext class da farklı anlatımı
                UsernamePasswordAuthenticationToken authenticationToken=// bu data turnde Contexte gondemek zorundayız
                        new UsernamePasswordAuthenticationToken(userDetails,//burada userdetails degilde userDetails.getUsername() şeklinde email de gonderebilirz
                                null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
                logger.error("User Not Found {} : ",e.getMessage());
        }
        filterChain.doFilter(request,response);//filter oalrak eklenmesi gerektiği için ekiliyoruz
    }

    private String parseJwt(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")){// jwt token header da geliyormu String mi ve "Bearer " ile başlıyotmu
            return header.substring(7);// burada token başlangıc yerinden basşladığı için bu şekilde alıyoruz
        }
        return null;

    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher =new AntPathMatcher();// burada yapmasakda Security Config de yapabiliriz
        return antPathMatcher.match("/register",request.getServletPath()) ||
                antPathMatcher.match("/login",request.getServletPath());
    }
}
