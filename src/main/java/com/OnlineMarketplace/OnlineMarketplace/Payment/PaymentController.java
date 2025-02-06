package com.OnlineMarketplace.OnlineMarketplace.Payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-payment")
    // will need to be updated when the frontend comes out !!!
    public ResponseEntity<?> createPaymentIntent(@RequestParam Double amount) {
        Stripe.apiKey = stripeApiKey;
        try {
            PaymentIntent intent = PaymentIntent.create(PaymentIntentCreateParams.builder()
                    .setAmount(Math.round(amount * 100))
                    .setCurrency("eur")
                    .build());
            return ResponseEntity.ok(intent.getClientSecret());
        } catch (StripeException e){
            return ResponseEntity.status(500).body("Error when creating a payment: " + e.getMessage());
        }
    }

    }


