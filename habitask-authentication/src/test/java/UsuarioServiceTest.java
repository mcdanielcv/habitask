import Dto.UserDTO;
import errors.UserNotFoundException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.UserRepository;
import service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_successful() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User resultado = userService.registerUser(user);
        assertNotNull(resultado);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByEmail_successful() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<UserDTO> resultado = userService.getUserByEmail("test@example.com");
        assertTrue(resultado.isPresent());
        assertEquals("Test User", resultado.get().getName());
    }

    @Test
    void validateCredentials_successful() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encodedPassword")).thenReturn(true);
        boolean resultado = userService.validateCredentials("test@example.com", "Password123!");
        assertTrue(resultado);
    }

    @Test
    void getUserById_userNotFound_lanzaExcepcion() {
        MockitoAnnotations.openMocks(this);
        Long idNoExistente = 99L;
        when(userRepository.findById(idNoExistente)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(idNoExistente));
    }
}
