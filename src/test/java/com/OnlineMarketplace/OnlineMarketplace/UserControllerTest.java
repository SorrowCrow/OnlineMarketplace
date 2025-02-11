package com.OnlineMarketplace.OnlineMarketplace;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserController;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Mock
    private UserService userService;
    @MockBean
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setup() {
        // Setup MockMvc and ObjectMapper
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Create a test user
        testUser = new User("test@example.com", "John", "Doe", "password123");
        testUser.setId(1L);
    }

    @Test
    public void testGetUserById() throws Exception {
        // Mock the userService to return the testUser
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.surname").value(testUser.getSurname()))
                .andExpect(jsonPath("$.id").value(testUser.getId()));

        // Verify that the service method was called once
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Prepare the updated user
        User updatedUser = new User("test@example.com", "Jane", "Smith", "newpassword123");
        updatedUser.setId(1L);

        // Mock the userService to return the updated user
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.surname").value(updatedUser.getSurname()))
                .andExpect(jsonPath("$.password").value(updatedUser.getPassword()));

        // Verify that the service method was called once
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Mock the userService to simulate a successful delete
        when(userService.deleteUser(1L)).thenReturn(true);

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        // Verify that the service method was called once
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_Failure() throws Exception {
        // Mock the userService to simulate a failed delete
        when(userService.deleteUser(1L)).thenReturn(false);

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User deletion failed"));

        // Verify that the service method was called once
        verify(userService, times(1)).deleteUser(1L);
    }
}
