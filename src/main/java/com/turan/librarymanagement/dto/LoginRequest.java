package com.turan.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = "Please provide valid email address")
    private String email;
    @NotBlank(message = "Please provide a password")
    private String password;
}
