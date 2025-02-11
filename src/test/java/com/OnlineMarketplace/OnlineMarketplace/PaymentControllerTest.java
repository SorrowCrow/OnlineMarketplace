package com.OnlineMarketplace.OnlineMarketplace;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.OnlineMarketplace.OnlineMarketplace.Payment.PaymentController;
import com.OnlineMarketplace.OnlineMarketplace.Payment.PaymentService;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.stripe.exception.StripeException;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {
     @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;
    private User testUser;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        // Initialize a test user for mocking the user service
        testUser = new User("test@example.com", "John", "Doe", "password123");
        testUser.setId(1L);
    }

    @Test
    public void testCreatePaymentSession_Success() throws Exception {
        // Mocking the behavior of jwtUtils and userService
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn(testUser.getEmail());
        when(userService.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(paymentService.createPaymentSession(testUser.getEmail(), testUser.getCart().getListings()))
                .thenReturn("paymentSessionUrl");

        // Perform the POST request to create the payment session
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments/create-session")
                .header("Authorization", "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("paymentSessionUrl"));

        // Verify that the service methods were called
        verify(userService, times(1)).findByEmail(testUser.getEmail());
        verify(paymentService, times(1)).createPaymentSession(testUser.getEmail(), testUser.getCart().getListings());
    }

    @Test
    public void testCreatePaymentSession_UserNotFound() throws Exception {
        // Mocking the behavior of jwtUtils and userService
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn(testUser.getEmail());
        when(userService.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Perform the POST request to create the payment session
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments/create-session")
                .header("Authorization", "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("User does not exist"));

        // Verify that the service methods were called
        verify(userService, times(1)).findByEmail(testUser.getEmail());
        verify(paymentService, never()).createPaymentSession(any(), any());
    }

    @Test
    public void testPaymentSuccess_Success() throws Exception {
        // Mocking the behavior of paymentService
        when(paymentService.paymentSuccess(any())).thenReturn("Payment successful");

        // Perform the GET request for payment success
        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments/success")
                .param("session_id", "session123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful"));

        // Verify that the service method was called
        verify(paymentService, times(1)).paymentSuccess("session123");
    }

    @Test
    public void testPaymentSuccess_StripeException() throws Exception {
        // Mocking StripeException
    StripeException stripeException = mock(StripeException.class);
    when(stripeException.getMessage()).thenReturn("Payment error");

    // Use doThrow to simulate StripeException being thrown
    doThrow(stripeException).when(paymentService).paymentSuccess(any());

    // Perform the GET request for payment success
    mockMvc.perform(MockMvcRequestBuilders.get("/api/payments/success")
            .param("session_id", "session123"))
            .andExpect(status().isOk())
            .andExpect(content().string("Payment processing error: Payment error"));

    // Verify that the service method was called
    verify(paymentService, times(1)).paymentSuccess("session123");
    }

    @Test
    public void testPaymentFailure() throws Exception {
        // Perform the GET request for payment failure
        when(paymentService.paymentFailure()).thenReturn("Payment failed");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payments/failure"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment failed"));

        
        verify(paymentService, times(1)).paymentFailure();
    }

}
