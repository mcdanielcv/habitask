package service;

import Dto.UserDTO;
import errors.InvalidCredentialsException;
import errors.UserNotFoundException;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import repository.UserRepository;

import java.util.Optional;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
        return true;
    }
}
