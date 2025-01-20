
import Dto.UserDTO;
import controller.UserController;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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

        /*UserDTO usuarioDTO = new UserDTO(1L, "Test User", "test@example.com");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<UserDTO> respuesta = userController.registerUser(user);

        assertNotNull(respuesta.getBody());
        assertEquals("Test User", respuesta.getBody().getName());
        verify(userService, times(1)).registerUser(user);*/
    }

    @Test
    void login_successful() {
       /* when(userService.validateCredentials("test@example.com", "Password123!")).thenReturn(true);

        ResponseEntity<String> respuesta = userController.login("test@example.com", "Password123!");

        assertEquals(200, respuesta.getStatusCodeValue());
        assertEquals("Inicio de sesión exitoso", respuesta.getBody());*/
    }

    @Test
    void login_failed() {
       /* when(userService.validateCredentials("test@example.com", "WrongPassword")).thenReturn(false);

        ResponseEntity<String> respuesta = userController.login("test@example.com", "WrongPassword");

        assertEquals(401, respuesta.getStatusCodeValue());
        assertEquals("Credenciales inválidas", respuesta.getBody());*/
    }
}
