package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyCodeRequest(
        @Email @NotBlank
        String email,

        @NotBlank @Pattern(regexp = "\\d{6}")
        String code
)
{}
