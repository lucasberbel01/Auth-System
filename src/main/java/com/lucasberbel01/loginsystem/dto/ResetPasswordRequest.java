package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank
        String resetToken,

        @NotBlank(message = "The password can not be blank")
        @Size(min = 6, message = "Passwords must have at least 6 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Passwords must contain a number, a capital letter and a special character")
        String password
) {
}
