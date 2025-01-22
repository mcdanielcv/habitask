package com.habitask.controller;

import com.habitask.Dto.UserDTO;
import com.habitask.ResponseVo;
import com.habitask.model.User;
import com.habitask.service.UserService;
import com.habitask.utilitarian.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            // 1. Verificar las credenciales del usuario
            Optional<User> userOptional = userService.getUserByEmail(email);

            log.info("userOptional->"+userOptional);

            if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, "Email o contraseña incorrectos"));
            }

            User userEntity = userOptional.get();
            log.info("***1****");
            List<GrantedAuthority> authorities = List.of();

            // 3. Crear un objeto Authentication
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userEntity.getEmail(), null, authorities);
            log.info("***2****");
            // 4. Establecer la autenticación en el contexto de Spring Security
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("***3****");
            // 5. Generar un token JWT
            String token = jwtTokenProvider.generateToken(userEntity.getEmail());
            log.info("tokenmodi->"+token);
            // 6. Devolver la respuesta con el token
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVo(true, "Inicio de Sesión Exitoso", token));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, e.getMessage()));
        }
    }


    @GetMapping("/autenticateUser")
    public ResponseEntity<?> getAuthenticatedUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUser = authentication.getName(); // Obtiene el email del usuario autenticado
            return ResponseEntity.ok("Usuario autenticado: " + currentUser);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, e.getMessage()));
        }
    }

    // Endpoint para logout (simulación)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            // Aquí manejarías el cierre de sesión
            return ResponseEntity.ok("Sesión cerrada exitosamente");
        }catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }
}
