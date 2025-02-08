package com.OnlineMarketplace.OnlineMarketplace.Payment;

import com.OnlineMarketplace.OnlineMarketplace.Auth.MessageResponse;
import com.OnlineMarketplace.OnlineMarketplace.Security.JwtUtils;
import com.OnlineMarketplace.OnlineMarketplace.User.User;
import com.OnlineMarketplace.OnlineMarketplace.User.UserService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-session")
    public String createPaymentSession(HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.getJwtFromCookies(request));

        Optional<User> optionalUser = userService.findByEmail(username);

        if(optionalUser.isEmpty()){
            return "User does not exist";
        }

        User user = optionalUser.get();

        log.info(user.getEmail());

        try {
            return paymentService.createPaymentSession(user.getEmail(), user.getCart().getListings());
        } catch (StripeException e) {
            e.printStackTrace();
            return "Error when creating a payment session: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId) {
        try {
            return paymentService.paymentSuccess(sessionId);
        } catch (StripeException e) {
            return "Payment processing error: " + e.getMessage();
        }
    }
    @GetMapping("/failure")
    public String paymentFailure() {
        return paymentService.paymentFailure();
    }



    }


