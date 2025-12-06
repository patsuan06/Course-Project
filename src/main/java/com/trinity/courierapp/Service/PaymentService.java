package com.trinity.courierapp.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String createPaymentIntent(Long amountInCents, String userId) throws StripeException {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("usd")
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return intent.getClientSecret();
    }

}
