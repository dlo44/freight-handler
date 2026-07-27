package org.example.freighthandler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserByUserId_returnsUserDto() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUserId(1001L);
        user.setName("John Doe");

        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUserId(1001L);
        dto.setName("John Doe");

        when(userService.findByUserIdNumber(1001L)).thenReturn(user);
        when(modelMapper.map(eq(user), eq(UserDTO.class))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/users/1001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1001))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserByUserId_notFound_returns404() throws Exception {
        // Arrange
        when(userService.findByUserIdNumber(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/users/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_returnsCreatedDto() throws Exception {
        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setUserId(1001L);
        request.setName("John Doe");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUserId(1001L);
        savedUser.setName("John Doe");

        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUserId(1001L);
        dto.setName("John Doe");

        when(userService.saveUser(any(User.class))).thenReturn(savedUser);
        when(modelMapper.map(eq(savedUser), eq(UserDTO.class))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1001))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.id").value(1));

        verify(userService).saveUser(any(User.class));
    }
}