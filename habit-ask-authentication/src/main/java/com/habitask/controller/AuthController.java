package com.habitask.controller;

import com.habitask.Dto.UserDTO;
import com.habitask.ResponseVo;
import com.habitask.service.UserService;
import com.habitask.utilitarian.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            boolean isValid = userService.validateCredentials(email, password);
            log.info("isValid->"+isValid);
            if (isValid) {
                log.info("generar token->");
                String token = jwtTokenProvider.generateToken(email);
                log.info("fin generar token->");
                log.info("token->"+token);
                log.info("fin generar token2->");
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseVo(true, "Inicio de Sesion Exitoso", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, "Credenciales Invalidas"));
            }
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

    @GetMapping("/autenticateUser")
    public ResponseEntity<?> getAuthenticatedUser(){
        try {
            UserDTO userDTO = userService.getAuthenticatedUser();
            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVo(false,"Usuario no encontrado"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVo(false, "Exito", userDTO));
        }catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, e.getMessage()));
        }
    }
}
