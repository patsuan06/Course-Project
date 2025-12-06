package com.trinity.courierapp.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StripeConfig {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51SbHLSEzyWZkHJ6rzV4QIS18z70TIXBUzfgFztzm2JKadQXZb88eucVYKcvHft3b0MTJxLST2LFzL5Z3kJ9sF30D00IX7BywGt"; // secret key
    }
}
