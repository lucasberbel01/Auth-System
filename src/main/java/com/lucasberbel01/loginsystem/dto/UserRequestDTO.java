package com.lucasberbel01.loginsystem.dto;

import com.lucasberbel01.loginsystem.enums.UserRole;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "O nome de usuário não pode estar em branco")
        @Size(min = 3, max = 100, message = "O nome de usuário deve ter entre 3 e 100 caracteres")
        String username,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "A senha deve conter pelo menos 1 numero, 1 letra maiuscula e 1 caracter especial")
        String password,

        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "O e-mail informado é inválido")
        String email
) {}