package com.mealplannerv2.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mealplannerv2.infrastructure.security.jwt.error.InvalidJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
public class TokenDecoder {

    private final JwtConfigurationProperties properties;

    public DecodedJWT getDecodedJWT(String token, String name) {
        final DecodedJWT jwt;
        try {
            jwt = decodeJwtToken(token);
        } catch (JWTVerificationException e) {
            log.error("{}: {}", name, e.getMessage());
            throw new InvalidJwtException("JWT verification failed");
        }
        return jwt;
    }

    private DecodedJWT decodeJwtToken(String token) {
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }
}
