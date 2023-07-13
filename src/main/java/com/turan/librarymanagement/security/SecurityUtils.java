package com.turan.librarymanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {
    // Controller veya service katmanında anlık olarak login olan kullnıcıya ulaşmak için bu classı yazdık

    //guncel kullanıcı bilgileri
    public static Optional<String> getCurrentUserLogin(){// NullPointerException gelme ihtimaline karsı optional olark aldık
        SecurityContext securityContext =SecurityContextHolder.getContext();
        Authentication authentication =securityContext.getAuthentication();
        // .ofNullable() methodua eger null gelirse biz içi boş return ediyoruz dolu ise istediğimiz datayı return eder
        return Optional.ofNullable(extractPrincipal(authentication));// authenticated olarak gelen datanın sadece emaili için

    }


    private static String extractPrincipal(Authentication authentication){
        if (authentication==null){
            return null;
        }else if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }else if(authentication.getPrincipal() instanceof String){
            return (String) authentication.getPrincipal();
        }
        return null;
    }


}
