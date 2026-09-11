package com.lucasberbel01.loginsystem.dto;

public record UserPatchDTO(
        String username,
        String email,
        String password) {}
