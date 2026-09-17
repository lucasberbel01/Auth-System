package com.lucasberbel01.loginsystem.controller;

import com.lucasberbel01.loginsystem.dto.ForgotPasswordRequest;
import com.lucasberbel01.loginsystem.dto.ResetPasswordRequest;
import com.lucasberbel01.loginsystem.dto.VerifyCodeRequest;
import com.lucasberbel01.loginsystem.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/recover")
public class PasswordRecoverController {

    private final PasswordResetService passwordResetService;

    public  PasswordRecoverController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword (@RequestBody @Valid ForgotPasswordRequest request){
        passwordResetService.forgotPassword(request.email());
        return ResponseEntity.ok(Map.of("message", "If the email exists, we sent reset code to it"));
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyCode (@RequestBody @Valid VerifyCodeRequest request ){
        String resetToken = passwordResetService.verifyCode(request.email(),  request.code());
        return ResponseEntity.ok(Map.of("Reset token", resetToken));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        passwordResetService.resetPassword(request.resetToken(), request.password());
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }
}
