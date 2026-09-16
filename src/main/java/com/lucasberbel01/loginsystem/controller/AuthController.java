package com.lucasberbel01.loginsystem.controller;

import com.lucasberbel01.loginsystem.dto.UserLoginDTO;
import com.lucasberbel01.loginsystem.dto.UserRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService userService) {
        this.service = userService;
    }

    //================================================================================
    //LOGIN
    // ================================================================================
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginDTO request) {
        return ResponseEntity.ok(service.login(request));
    }


    //================================================================================
    //POST
    // ================================================================================

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO userResponseDTO = service.createUser(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(userResponseDTO);
    }
}
