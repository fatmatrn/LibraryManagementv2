package com.turan.librarymanagement.security.service;

import com.turan.librarymanagement.domain.Borrower;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    // security e user larımızı tanıtmamız lazım
    private String email;
    private String password;

    // security rolleri bilmez o yuzden GrantedAuthority den anladığı için
    private Collection<? extends GrantedAuthority> authorities;

    // borrower ---> UserDetails
    public static UserDetailsImpl build(Borrower borrower) {
        List<SimpleGrantedAuthority> authorities = borrower.getRoles().// donen deger list olarak SimpleGrantedAuthority class turnde
                stream().map(role -> new SimpleGrantedAuthority(role.getType().name())).//mapleme işlemi yapıyoruz
                collect(Collectors.toList());
        return new UserDetailsImpl(borrower.getEmail(), borrower.getPassword(), authorities);// 3 bilgiyi return ediyoruz
    }

    // override methodları kendimizin kullandığı fieldları uyarlma yapıyoruz
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.email; // jwt utilsde getUsername çağırdığımızda email gider
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}