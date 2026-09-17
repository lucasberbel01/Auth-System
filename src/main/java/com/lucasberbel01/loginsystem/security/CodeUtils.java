package com.lucasberbel01.loginsystem.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CodeUtils {

    private final SecureRandom random = new SecureRandom();

    public String generateCode() {
        int code =  random.nextInt(1_000_000);
        return String.format("%06d", code);
    }

    public String hash(String value){
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }
}
