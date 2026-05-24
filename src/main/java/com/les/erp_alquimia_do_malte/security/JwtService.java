package com.les.erp_alquimia_do_malte.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String gerarToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "userId", userDetails.getUsuarioId().toString(),
                "role", userDetails.getTipo().name(),
                "setorId", userDetails.getSetorId() != null ? userDetails.getSetorId().toString() : ""
        );
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public UUID extrairUsuarioId(String token) {
        String id = extrairClaim(token, c -> c.get("userId", String.class));
        return id != null && !id.isBlank() ? UUID.fromString(id) : null;
    }

    public boolean tokenValido(String token, UserDetails userDetails) {
        return extrairEmail(token).equals(userDetails.getUsername()) && !expirado(token);
    }

    private boolean expirado(String token) {
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(
                Jwts.parser().verifyWith(getKey()).build()
                        .parseSignedClaims(token).getPayload()
        );
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
}
