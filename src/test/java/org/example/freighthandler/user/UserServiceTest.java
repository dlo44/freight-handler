package org.example.freighthandler.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserService.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindByUserIdNumber_returnsUser() {
        // Arrange
        User user = new User();
        user.setUserId(1001L);
        user.setName("John Doe");

        Mockito.when(userRepository.findByUserId(1001L))
                .thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUserIdNumber(1001L);

        // Assert
        assertNotNull(result);
        assertEquals(1001L, result.getUserId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testFindByUserIdNumber_notFound_returnsNull() {
        // Arrange
        Mockito.when(userRepository.findByUserId(999L))
                .thenReturn(Optional.empty());

        // Act
        User result = userService.findByUserIdNumber(999L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSaveUser_returnsSavedUser() {
        // Arrange
        User user = new User();
        user.setUserId(1001L);
        user.setName("John Doe");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUserId(1001L);
        savedUser.setName("John Doe");

        Mockito.when(userRepository.save(user)).thenReturn(savedUser);

        // Act
        User result = userService.saveUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1001L, result.getUserId());
        assertEquals("John Doe", result.getName());
    }
}
