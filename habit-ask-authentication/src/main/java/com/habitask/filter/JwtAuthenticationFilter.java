package com.habitask.filter;

import com.habitask.service.CustomUserDetailsService;
import com.habitask.utilitarian.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider; // Proveedor del JWT (expiración, validación, etc.)
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Obtener el token del encabezado de la solicitud
        String token = jwtTokenProvider.getTokenFromRequest(request);
        logger.info("token Validar->"+token);
        try {
            // 2. Validar el token
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 3. Obtener el email (o username) del token
                String username = jwtTokenProvider.getUsernameFromToken(token);

                // Cargar el usuario desde la base de datos usando el servicio CustomUserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crear una autenticación basada en el token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException ex) {
            logger.error("No se pudo autenticar el usuario: {}"+ ex.getMessage());
        }

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
