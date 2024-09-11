package com.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;

@Service
public class StripeClient {
	
	@Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_51PDFhBKqoZbs3F8D8ASeENN0ZWfKDUbbfgYVpDvfcOryhQl5qWSHexxr4uRqjAtlaH0uPmDr4obOq4lhG6643UBc00TyWyiR7K";
    }
	
	public Charge chargeCreditCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        System.out.println(chargeParams);
        return charge;
    }

}
