package com.irinakom.cardmanagementservice.dto.response;

import com.irinakom.cardmanagementservice.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponse(
        Long id,
        String maskedCardNumber,
        CardStatus status,
        BigDecimal balance,
        LocalDate expirationDate
) {}
