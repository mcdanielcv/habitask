package com.habitask.service;

import com.habitask.Dto.UserDTO;
import com.habitask.errors.InvalidCredentialsException;
import com.habitask.errors.UserNotFoundException;
import com.habitask.model.User;
import com.habitask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    public Optional<User> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User no encontrado"));
        return Optional.of(user);
    }

    public Optional<UserDTO> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return Optional.of(new UserDTO(user.getId(), user.getName(), user.getEmail()));
    }

    public boolean validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario con email no encontrado"));

        log.info("contrasenaDB->"+user.getPassword()+"-ingresada->"+password+"-codificada->");
        if (!user.getPassword().equalsIgnoreCase(passwordEncoder.encode(password))) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
        return true;
    }

    // Método para obtener el usuario autenticado
    public String getAuthenticatedUser() {
        // Obtener el usuario autenticado desde el contexto de Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null; // No hay usuario autenticado
    }
}
