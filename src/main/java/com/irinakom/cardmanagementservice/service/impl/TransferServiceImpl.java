package com.irinakom.cardmanagementservice.service.impl;

import com.irinakom.cardmanagementservice.dto.request.TransferRequest;
import com.irinakom.cardmanagementservice.entity.Card;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.exception.CardBlockedException;
import com.irinakom.cardmanagementservice.exception.InsufficientFundsException;
import com.irinakom.cardmanagementservice.exception.ResourceNotFoundException;
import com.irinakom.cardmanagementservice.repository.CardRepository;
import com.irinakom.cardmanagementservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;

    @Override
    @Transactional
    public void transfer(User user, TransferRequest request) {


        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Card from = cardRepository.findByIdAndOwnerWithLock(
                        request.fromCardId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Source card not found")
                );

        Card to = cardRepository.findByIdAndOwnerWithLock(
                        request.toCardId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Target card not found")
                );

        validate(from, to, request.amount());

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));
    }

    private void validate(Card from, Card to, BigDecimal amount) {

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        if (from.getStatus() != CardStatus.ACTIVE || to.getStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException("Card is not active");
        }

        if (from.getExpirationDate().isBefore(LocalDate.now())
                || to.getExpirationDate().isBefore(LocalDate.now())) {
            throw new CardBlockedException("Card is expired");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }
}
