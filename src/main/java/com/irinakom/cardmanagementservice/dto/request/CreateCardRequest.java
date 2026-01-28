package com.irinakom.cardmanagementservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(
        @NotNull Long ownerId,
        @NotNull @Future LocalDate expirationDate,
        @PositiveOrZero BigDecimal initialBalance
) {}
