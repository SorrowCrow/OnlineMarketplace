package com.OnlineMarketplace.OnlineMarketplace;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.OnlineMarketplace.OnlineMarketplace.Auth.AuthController;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleService;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthController authController;  // Inject mocks into AuthController

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testSignUp() throws Exception {
        // Mock the user service to create a user
        User newUser = new User("newuser@example.com", "John", "Doe", "password123");
        when(userService.createUser(any(),false)).thenReturn(newUser);

        // Perform the POST request for sign up
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"newuser@example.com\", \"password\": \"password123\", \"name\": \"John\", \"surname\": \"Doe\"}"))
                .andExpect(status().isOk());

        // Verify that createUser was called once
        verify(userService, times(1)).createUser(any(), false);
    }

}
