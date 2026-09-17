package com.lucasberbel01.loginsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password-reset-code")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private int attempts = 0;

    @Override
    public String toString() {
        return "PasswordResetCode{" +
                "id=" + id +
                ", email='" + userEmail + '\'' +
                ", code='" + code + '\'' +
                ", dateTime=" + expiryDate +
                ", attempts=" + attempts +
                '}';
    }
}
