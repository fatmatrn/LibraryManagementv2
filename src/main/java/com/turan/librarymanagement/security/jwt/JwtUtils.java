package com.turan.librarymanagement.security.jwt;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
        private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
        @Value("${LibraryManagement.app.jwtSecret}")
        private String jwtSecret;
        @Value("${LibraryManagement.app.jwtExpirationMs}")
        private Long jwtExpirationMs;
        // generate jwt token
        public String generateJwtToken(UserDetails userDetails) {
            return Jwts.builder().// token için setleme yapacaz email,date,jwtSeecret
                        setSubject(userDetails.getUsername()).// burada setleme yaptık email bilgisini verdik amac unique deger vermek
                        setIssuedAt(new Date()).//oluşturlma zamanı
                        setExpiration(new Date(new Date().getTime()+jwtExpirationMs)).//verilen sueriyı ekleme yaptık
                        signWith(SignatureAlgorithm.HS512,jwtSecret).// şifreleme yap jwt ekleme yaparak
                        compact();//hepsini topla
        }

        // jwt token içinden email bilgisini ulaşacağım method
            //içnden email al oradan tum bilgilere ulaşmak isteriz
        public String getEmailFromToken(String token) {
            return Jwts.parser().setSigningKey(jwtSecret).
                    parseClaimsJws(token).
                    getBody().
                    getSubject();
        }

        //jwt validate  işlemi için method

            public boolean validateJwtToken(String token){
                try {

                    Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                    return true;
                } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                         IllegalArgumentException e) {
                    logger.error(String.format(
                            "JWT Token Validation Error: %s", e.getMessage()));
                }
                return false;

            }


}
