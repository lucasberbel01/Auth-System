package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank
        String resetToken,

        @NotBlank
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Passwords must contain a number, a capital letter and a special character")
        String password
) {
}
