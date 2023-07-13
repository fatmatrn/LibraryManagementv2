package com.turan.librarymanagement.controller;

import com.turan.librarymanagement.dto.BorrowerDTO;
import com.turan.librarymanagement.dto.LoginRequest;
import com.turan.librarymanagement.security.jwt.JwtUtils;
import com.turan.librarymanagement.service.BorrowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterAndLogin {
    private final BorrowerService borrowerService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public RegisterAndLogin(BorrowerService borrowerService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.borrowerService = borrowerService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<BorrowerDTO> register(@Valid @RequestBody BorrowerDTO borrowerDTO){
        BorrowerDTO borrowerDTO1 =borrowerService.createBorrower(borrowerDTO);
        return new ResponseEntity<>(borrowerDTO1, HttpStatus.CREATED);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@Valid @RequestBody LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        // Gelen mail ve password Authantication yapılacak
        Authentication authentication= authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // kullanıcı bu aşamada valide edildi ve token uretimine geçiliyor
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();// anlık olarak login olan kullanıcını bilgisini verir
        String jwttoken= jwtUtils.generateJwtToken(userDetails);
        return new ResponseEntity<>(jwttoken,HttpStatus.OK);
    }
}
