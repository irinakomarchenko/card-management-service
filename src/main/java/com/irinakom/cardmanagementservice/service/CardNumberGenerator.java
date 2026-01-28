package com.irinakom.cardmanagementservice.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CardNumberGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder number = new StringBuilder("4");

        for (int i = 0; i < 14; i++) {
            number.append(random.nextInt(10));
        }

        number.append(luhn(number.toString()));
        return number.toString();
    }

    private int luhn(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate && (n *= 2) > 9) n -= 9;
            sum += n;
            alternate = !alternate;
        }
        return (10 - sum % 10) % 10;
    }
}
