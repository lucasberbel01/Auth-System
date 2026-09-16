package com.lucasberbel01.loginsystem.controller;

import com.lucasberbel01.loginsystem.dto.*;
import com.lucasberbel01.loginsystem.exception.EmailOrPasswordIncorrectException;
import com.lucasberbel01.loginsystem.model.User;
import com.lucasberbel01.loginsystem.security.CustomUserDetails;
import com.lucasberbel01.loginsystem.security.TokenService;
import com.lucasberbel01.loginsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.service = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    //================================================================================
    //LOGIN
    // ================================================================================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(token, UserResponseDTO.fromEntity(user)));
        } catch (BadCredentialsException e) {
            throw new EmailOrPasswordIncorrectException("Wrong email or password");
        }
    }

    //================================================================================
    //POST
    // ================================================================================
    @PostMapping("/register")
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