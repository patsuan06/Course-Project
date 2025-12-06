package com.trinity.courierapp.Controller;

import com.trinity.courierapp.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/intent")
    public ResponseEntity<?> createIntent(@RequestParam Long amount) {
        try {
            // you can get the user from JWT here
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            String clientSecret = paymentService.createPaymentIntent(amount, email);
            return ResponseEntity.ok(clientSecret);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment");
        }
    }
}
