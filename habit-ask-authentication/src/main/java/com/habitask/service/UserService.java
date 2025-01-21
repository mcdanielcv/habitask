package com.habitask.service;

import com.habitask.Dto.UserDTO;
import com.habitask.errors.InvalidCredentialsException;
import com.habitask.errors.UserNotFoundException;
import com.habitask.model.User;
import com.habitask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Slf4j
@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User no encontrado"));
        return Optional.of(new UserDTO(user.getId(), user.getName(), user.getEmail()));
    }

    public Optional<UserDTO> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return Optional.of(new UserDTO(user.getId(), user.getName(), user.getEmail()));
    }

    public boolean validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario con email no encontrado"));

        log.info("contrasenaDB->"+user.getPassword()+"-ingresada->"+password+"-codificada->"+passwordEncoder.encode(password));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
        return true;
    }

    // Método para obtener el usuario autenticado
    public UserDTO getAuthenticatedUser() {
        // Obtener el usuario autenticado desde el contexto de Spring Security
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return (UserDTO) principal; // Devuelve el objeto User si está autenticado
        } else {
            return null; // Si no está autenticado, devuelve null
        }
    }
}
