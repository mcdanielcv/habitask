package com.habitask.utilitarian;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@PropertySource(value = {"classpath:application.properties"})
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.jwtSecret:jwtSecret}")
    private String jwtSecret;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long jwtExpirationMs;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }


    public void printProperties() {
        System.out.println("JWT Secret: " + jwtSecret);
        System.out.println("JWT Expiration: " + jwtExpirationMs);
    }

    /**
     * Genera un token JWT basado en el email del usuario.
     *
     * @param email El email del usuario para incluir en el token.
     * @return El token JWT generado.
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("base64Key->"+base64Key);
        return Jwts.builder()
                .setSubject(email) // Usuario asociado al token
                .setIssuedAt(now) // Fecha de emisión
                .setExpiration(expiryDate) // Fecha de expiración
                .signWith(SignatureAlgorithm.HS512, base64Key) // Firma con clave secreta
                .compact();
    }

    /**
     * Obtiene los claims del token JWT.
     *
     * @param token El token JWT.
     * @return Claims extraídos del token.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Valida un token JWT.
     *
     * @param token El token JWT.
     * @return `true` si el token es válido, `false` en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false; // Token inválido o expirado
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remover el prefijo "Bearer "
        }
        return null;
    }
}
