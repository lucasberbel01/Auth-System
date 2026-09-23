package com.lucasberbel01.loginsystem.controller;

import com.lucasberbel01.loginsystem.dto.UserPatchDTO;
import com.lucasberbel01.loginsystem.dto.UserRequestDTO;
import com.lucasberbel01.loginsystem.dto.UserResponseDTO;
import com.lucasberbel01.loginsystem.enums.UserRole;
import com.lucasberbel01.loginsystem.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.hibernate.annotations.Filter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement (name = "bearerAuth")
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
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@ParameterObject
                                                                 @PageableDefault(size = 10, sort = "username") Pageable pageable) {
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
    //PUT E PATCH
    // ================================================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
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
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
