package com.habitask.controller;

import com.habitask.Dto.UserDTO;
import com.habitask.ResponseVo;
import com.habitask.model.User;
import com.habitask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
}
