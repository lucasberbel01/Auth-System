package com.lucasberbel01.loginsystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lucasberbel01.loginsystem.exception.InvalidResetCodeException;
import com.lucasberbel01.loginsystem.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${SIGNKEY}")
    private String signKey;

    private static final String ISSUER = "loginsystem-api";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().name())
                    .withClaim("id", user.getId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
            return decoded.getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant genExpirationDate() {
        return Instant.now().plusSeconds(3600 * 2) // 2 horas
                .atZone(ZoneOffset.UTC)
                .toInstant();
    }


    //=========================================================================
    // RESET PASSWORD
    //=========================================================================

    public String generateResetToken(String email){
        Algorithm algorithm = Algorithm.HMAC256(signKey);

        return JWT.create()
                .withSubject(email)
                .withClaim("purpose", "password_reset")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 min
                .sign(algorithm);
    }

    public String validateAndExtractEmailFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(signKey);

        try{
            DecodedJWT decoded = JWT.require(algorithm)
                    .build()
                    .verify(token);

            String purpose = decoded.getClaim("purpose").asString();
            if (!"password_reset".equals(purpose)) {
                throw new InvalidResetCodeException("Invalid token");
            }

            return decoded.getSubject();

        }catch (JWTVerificationException exception){
            throw new InvalidResetCodeException("Invalid token or expired");
        }
    }
}