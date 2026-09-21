package com.lucasberbel01.loginsystem.dto;

import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "The username can not be blank")
        @Size(min = 3, max = 100, message = "Usernames must have between 3 and 100 characters")
        String username,

        @NotBlank(message = "The password can not be blank")
        @Size(min = 6, message = "Passowors must have at least 6 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Passwords must contain a number, a capital letter and a special character")
        String password,

        @NotBlank(message = "You must inform an email")
        @Email(message = "The informed email is invalid")
        String email
) {}