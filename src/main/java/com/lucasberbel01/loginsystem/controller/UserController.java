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

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    //================================================================================
    //GETS
    // ================================================================================

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(service.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("IsAuthenticated()")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("IsAuthenticated()")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    //================================================================================
    //POST
    // ================================================================================

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO userResponseDTO = service.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    //================================================================================
    //PUT E PATCH
    // ================================================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable Long id, @Valid @RequestBody UserPatchDTO request) {
        return ResponseEntity.ok(service.patchUser(id, request));
    }

    @PatchMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> patchUserRole(@PathVariable Long id, @Valid @RequestBody UserRole request) {
        return ResponseEntity.ok(service.updateUserRole(id, request));
    }

    //================================================================================
    //DELETE
    // ================================================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
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
