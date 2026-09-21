package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @Email(message = "The informed email is invalid")
        @NotBlank(message = "You must inform an email")
        String email
) {}
