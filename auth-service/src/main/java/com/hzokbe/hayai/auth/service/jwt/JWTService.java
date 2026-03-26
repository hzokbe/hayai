package com.hzokbe.hayai.auth.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JWTService {
    @Value("jwt.secret")
    private String secret;

    public String generateJWT(String subject) {
        var algorithm = Algorithm.HMAC256(secret);

        var now = Instant.now();

        return JWT
                .create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(3_600))
                .sign(algorithm);
    }
}
