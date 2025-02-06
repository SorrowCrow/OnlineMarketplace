package com.OnlineMarketplace.OnlineMarketplace.Payment;

import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-session")
    public String createPaymentSession(@RequestParam String userEmail, @RequestParam Long listingId) {
        try {
            return paymentService.createPaymentSession(userEmail,listingId);
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


