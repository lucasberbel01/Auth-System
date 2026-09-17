package com.lucasberbel01.loginsystem.service;

import com.lucasberbel01.loginsystem.exception.InvalidResetCodeException;
import com.lucasberbel01.loginsystem.exception.UserNotFoundException;
import com.lucasberbel01.loginsystem.model.PasswordResetCode;
import com.lucasberbel01.loginsystem.model.User;
import com.lucasberbel01.loginsystem.repository.PasswordResetCodeRepository;
import com.lucasberbel01.loginsystem.repository.UserRepository;
import com.lucasberbel01.loginsystem.security.CodeUtils;
import com.lucasberbel01.loginsystem.security.TokenService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final UserRepository userRepo;
    private final PasswordResetCodeRepository codeRepo;
    private final CodeUtils codeUtils;
    private final JavaMailSender mailSender;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepo, PasswordResetCodeRepository codeRepo, CodeUtils codeUtils, JavaMailSender mailSender, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.codeRepo = codeRepo;
        this.codeUtils = codeUtils;
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final int CODE_EXPIRATION_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;

    @Transactional
    public void forgotPassword(String email) {
        Optional<User> user = userRepo.findUserByEmail(email);

        if (user.isEmpty()) return;

        codeRepo.deleteAll();

        String rawCode = codeUtils.generateCode();

        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setUserEmail(email);
        resetCode.setCode(codeUtils.hash(rawCode));
        resetCode.setExpiryDate(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
        codeRepo.save(resetCode);

        sendEmail(email, rawCode);
    }

    @Transactional
    public String verifyCode (String email, String rawCode) {
        PasswordResetCode resetCode = codeRepo.findTopByUserEmailOrderByIdDesc(email)
                .orElseThrow(() -> new InvalidResetCodeException("Invalid code or expired"));

        if (resetCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidResetCodeException("Invalid code or expired");
        }

        if (resetCode.getAttempts() >= MAX_ATTEMPTS) {
            throw new InvalidResetCodeException("Max attempts exceeded");
        }

        if (!resetCode.getCode().equals(codeUtils.hash(rawCode))) {
            resetCode.setAttempts(resetCode.getAttempts() + 1);
            codeRepo.save(resetCode);
            throw new InvalidResetCodeException("Invalid code or expired");
        }

        codeRepo.deleteAllByUserEmail(email);

        return tokenService.generateResetToken(email);
    }

    @Transactional
    public void resetPassword(String resetToken, String newPassword){
        String email = tokenService.validateAndExtractEmailFromToken(resetToken);

        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    private void sendEmail(String emailTo, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject("Reset Password Code");
        message.setText("Your reset password code is: " + code +
                "\nIt expires in " + CODE_EXPIRATION_MINUTES + " minutes.");
        mailSender.send(message);
    }


}
