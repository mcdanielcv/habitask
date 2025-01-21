package com.habitask.controller;

import com.habitask.Dto.UserDTO;
import com.habitask.ResponseVo;
import com.habitask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.habitask.service.UserService;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            boolean isValid = userService.validateCredentials(email, password);
            if (isValid) {
                String token = "XSADASDASD";//jwtTokenProvider.generateToken(user.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseVo(true, "Inicio de Sesion Exitoso", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, "Credenciales Invalidas"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVo(false, e.getMessage()));
        }

    }

    // Endpoint para logout (simulación)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Aquí manejarías el cierre de sesión
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }

    @GetMapping("/autenticateUser")
    public UserDTO getAuthenticatedUser(){
        return userService.getAuthenticatedUser();
    }

}
