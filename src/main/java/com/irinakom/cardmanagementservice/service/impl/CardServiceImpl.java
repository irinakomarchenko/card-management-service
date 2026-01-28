package com.irinakom.cardmanagementservice.service.impl;

import com.irinakom.cardmanagementservice.dto.request.CreateCardRequest;
import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.Card;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.exception.ResourceNotFoundException;
import com.irinakom.cardmanagementservice.mapper.CardMapper;
import com.irinakom.cardmanagementservice.repository.CardRepository;
import com.irinakom.cardmanagementservice.repository.UserRepository;
import com.irinakom.cardmanagementservice.service.CardNumberGenerator;
import com.irinakom.cardmanagementservice.service.CardService;
import com.irinakom.cardmanagementservice.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardNumberGenerator cardNumberGenerator;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public CardResponse createCard(CreateCardRequest request) {

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + request.ownerId())
                );

        if (request.expirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date must be in the future");
        }

        String plainCardNumber = cardNumberGenerator.generate();
        String encryptedCardNumber = encryptionService.encrypt(plainCardNumber);
        String lastFourDigits = plainCardNumber.substring(12);

        Card card = new Card();
        card.setOwner(owner);
        card.setEncryptedCardNumber(encryptedCardNumber);
        card.setLastFourDigits(lastFourDigits);
        card.setExpirationDate(request.expirationDate());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(
                request.initialBalance() != null
                        ? request.initialBalance()
                        : BigDecimal.ZERO
        );

        Card saved = cardRepository.save(card);
        return cardMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void blockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }

        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new IllegalStateException("Cannot block expired card");
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    @Override
    @Transactional
    public void activateCard(Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot activate expired card");
        }

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new IllegalStateException("Card is already active");
        }

        card.setStatus(CardStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        cardRepository.deleteById(card.getId());
    }

    @Override
    public Page<CardResponse> getAllCards(CardStatus status, Pageable pageable) {
        Page<Card> page = (status == null)
                ? cardRepository.findAll(pageable)
                : cardRepository.findByStatus(status, pageable);

        return page.map(cardMapper::toResponse);
    }

    @Override
    public Page<CardResponse> getUserCards(User user, CardStatus status, Pageable pageable) {
        Page<Card> page = (status == null)
                ? cardRepository.findByOwner(user, pageable)
                : cardRepository.findByOwnerAndStatus(user, status, pageable);

        return page.map(cardMapper::toResponse);
    }

    @Override
    public CardResponse getUserCardById(User user, Long cardId) {
        Card card = cardRepository.findByIdAndOwner(cardId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found with id: " + cardId)
                );

        return cardMapper.toResponse(card);
    }

    @Override
    @Transactional
    public void requestCardBlock(User user, Long cardId) {
        Card card = cardRepository.findByIdAndOwner(cardId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found with id: " + cardId)
                );

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE cards can be requested for blocking");
        }

        card.setStatus(CardStatus.PENDING_BLOCK);
    }

    @Override
    @Transactional
    public void confirmBlockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (card.getStatus() != CardStatus.PENDING_BLOCK) {
            throw new IllegalStateException("Card must be in PENDING_BLOCK status");
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    @Override
    @Transactional
    public void rejectBlockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (card.getStatus() != CardStatus.PENDING_BLOCK) {
            throw new IllegalStateException("Card must be in PENDING_BLOCK status");
        }

        card.setStatus(CardStatus.ACTIVE);
    }


    private Card getCardOrThrow(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found with id: " + cardId)
                );
    }
}
