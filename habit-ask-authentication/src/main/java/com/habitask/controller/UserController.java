package com.habitask.controller;

import com.habitask.Dto.UserDTO;
import com.habitask.ResponseVo;
import jakarta.validation.Valid;
import com.habitask.model.User;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.habitask.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseVo(true, "Usuario Encontrado", userService.getUserById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User userDb = userService.registerUser(user);
            UserDTO userDTO = new UserDTO(userDb.getId(), userDb.getName(), userDb.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseVo(true, "Usuario registrado", userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        boolean isValid = userService.validateCredentials(email, password);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseVo(true, "Inicio de sesión exitoso"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseVo(false, "Credenciales inválidas"));
        }
    }
}
