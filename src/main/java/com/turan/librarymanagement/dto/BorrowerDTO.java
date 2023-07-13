package com.turan.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDTO {
  @NotNull(message = "Please provide name")
  private String name;
  @NotNull(message = "Please provide email")
  private String email;
  @NotNull(message = "Please provide phone number")  //(541) 317-8828
  @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4}$",message = "Please provide valid phone number")
  private String phoneNumber;
  private String password;

}