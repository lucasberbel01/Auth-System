package com.lucasberbel01.loginsystem.dto;

public record LoginResponseDTO(String token, UserResponseDTO user) {
}