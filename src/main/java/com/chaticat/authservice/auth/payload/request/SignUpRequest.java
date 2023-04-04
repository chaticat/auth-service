package com.chaticat.authservice.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 15)
    private String username;

    @NotBlank
    @Size(min = 4, max = 30)
   /* @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!_$%]).{4,30})")*/
    private String password;

}
