package com.irinakom.cardmanagementservice.service;

import com.irinakom.cardmanagementservice.dto.request.CreateCardRequest;
import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    CardResponse createCard(CreateCardRequest request);

    void blockCard(Long cardId);
    void confirmBlockCard(Long cardId);
    void rejectBlockCard(Long cardId);
    void activateCard(Long cardId);
    void deleteCard(Long cardId);

    Page<CardResponse> getAllCards(CardStatus status, Pageable pageable);
    Page<CardResponse> getUserCards(User user, CardStatus status, Pageable pageable);
    CardResponse getUserCardById(User user, Long cardId);

    void requestCardBlock(User user, Long cardId);
}


