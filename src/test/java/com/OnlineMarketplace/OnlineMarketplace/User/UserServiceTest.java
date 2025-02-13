package com.OnlineMarketplace.OnlineMarketplace.User;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.OnlineMarketplace.OnlineMarketplace.Auth.SignUpRequestDTO;
import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    private User user;
    private String verificationToken;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setName("John");
        user.setSurname("Doe");
        user.setPassword("password123");
        verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        user.setAccountVerified(false);
    }

    @Test
    void testCreateUser() {
        SignUpRequestDTO request = new SignUpRequestDTO();
        request.setEmail("test@example.com");
        request.setName("John");
        request.setSurname("Doe");
        request.setPassword("password123");

        Role role = new Role(1, ERole.ROLE_USER);

        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(request, false);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertFalse(createdUser.isAccountVerified());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testVerifyUserSuccess() {
        when(userRepository.findByVerificationToken(verificationToken)).thenReturn(Optional.of(user));

        boolean result = userService.verifyUser(verificationToken);

        assertTrue(result);
        assertNull(user.getVerificationToken());
        assertNull(user.getVerificationTokenExpiry());
        assertTrue(user.isAccountVerified());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testVerifyUserFailure_InvalidToken() {
        when(userRepository.findByVerificationToken(anyString())).thenReturn(Optional.empty());

        boolean result = userService.verifyUser("invalid-token");

        assertFalse(result);
    }

    @Test
    void testVerifyUserFailure_ExpiredToken() {
        user.setVerificationTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByVerificationToken(verificationToken)).thenReturn(Optional.of(user));

        boolean result = userService.verifyUser(verificationToken);

        assertFalse(result);
        assertFalse(user.isAccountVerified());
    }

    @Test
    void testResendVerificationEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.resendVerificationEmail(user.getEmail());

        assertTrue(result);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }

    @Test
    void testResendVerificationEmail_Failure_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        boolean result = userService.resendVerificationEmail("nonexistent@example.com");

        assertFalse(result);
    }

    @Test
    void testResendVerificationEmail_Failure_AlreadyVerified() {
        user.setAccountVerified(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        boolean result = userService.resendVerificationEmail(user.getEmail());

        assertFalse(result);
    }
}
