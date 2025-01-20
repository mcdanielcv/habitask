package controller;

import Dto.UserDTO;
import jakarta.validation.Valid;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody User user) {
        User userDb = userService.registerUser(user);
        UserDTO userDTO = new UserDTO(userDb.getId(), userDb.getName(), userDb.getEmail());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<UserDTO> userDb = userService.getUserById(id);
        return userDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean isValid = userService.validateCredentials(email, password);
        if (isValid) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
