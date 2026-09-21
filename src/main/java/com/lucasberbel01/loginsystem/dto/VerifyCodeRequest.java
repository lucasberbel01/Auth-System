package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyCodeRequest(
        @Email(message = "The informed email is invalid")
        @NotBlank(message = "You must inform an email")
        String email,

        @NotBlank(message = "You must inform the code sent in your email")
        @Pattern(regexp = "\\d{6}")
        String code
)
{}
