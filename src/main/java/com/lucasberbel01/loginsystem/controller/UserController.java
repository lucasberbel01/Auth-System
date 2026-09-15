package com.lucasberbel01.loginsystem.controller;

import com.lucasberbel01.loginsystem.dto.UserLoginDTO;
import com.lucasberbel01.loginsystem.dto.UserPatchDTO;
import com.lucasberbel01.loginsystem.dto.UserRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.enums.UserRole;
import com.lucasberbel01.loginsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {

    //TODO GlobalExceptionHandler
    // autenticacao JWT
    // validar se todos os endpoints funcionam antes do JWT

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    //================================================================================
    //GETS
    // ================================================================================

    @GetMapping("/all")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(service.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    //================================================================================
    //POST
    // ================================================================================

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO userResponseDTO = service.createUser(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    //================================================================================
    //PUT E PATCH
    // ================================================================================

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable Long id, @Valid @RequestBody UserPatchDTO request) {
        return ResponseEntity.ok(service.patchUser(id, request));
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<UserResponseDTO> patchUserRole(@PathVariable Long id, @Valid @RequestBody UserRole request) {
        return ResponseEntity.ok(service.updateUserRole(id, request));
    }

    //================================================================================
    //DELETE
    // ================================================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //================================================================================
    //LOGIN
    // ================================================================================
    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginDTO request) {
        return ResponseEntity.ok(service.login(request));
    }

}
