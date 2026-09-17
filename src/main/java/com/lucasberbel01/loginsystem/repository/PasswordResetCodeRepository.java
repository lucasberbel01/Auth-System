package com.lucasberbel01.loginsystem.repository;

import com.lucasberbel01.loginsystem.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findTopByUserEmailOrderByIdDesc(String userEmail);
    List<PasswordResetCode> findAllByUserEmail(String userEmail);
}
