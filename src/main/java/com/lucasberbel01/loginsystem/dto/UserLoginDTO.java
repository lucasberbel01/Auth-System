package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message = "The email can not be empty")
        @Email(message = "invalid email")
        String email,

        @NotBlank(message = "The password can not be empty")
        String password) {
}
