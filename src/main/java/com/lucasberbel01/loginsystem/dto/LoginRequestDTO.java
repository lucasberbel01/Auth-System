package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "You must inform an email")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "The password can not be empty")
        String password) {
}
