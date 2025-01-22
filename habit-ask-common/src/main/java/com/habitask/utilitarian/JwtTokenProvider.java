package com.habitask.utilitarian;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.jwtSecret:jwtSecret}")
    private String secretKey; // Reemplázala con una clave segura
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds; // 1 hora

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
/*
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }*/

    // Generar el token
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);
       // key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("base64Key->"+base64Key);
        log.info("key->"+key);
        return Jwts.builder()
                .setSubject(email) // Usuario asociado al token
                .setIssuedAt(now) // Fecha de emisión
                .setExpiration(expiryDate) // Fecha de expiración
                .signWith(SignatureAlgorithm.HS512, key) // Firma con clave secreta
                .compact();
    }

    // Validar el token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    // Extraer el usuario (username) del token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Extraer el token del encabezado de la solicitud
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Obtener la autenticación basada en el token
    public Authentication getAuthentication(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
