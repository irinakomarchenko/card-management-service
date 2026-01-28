package com.irinakom.cardmanagementservice.controller;

import com.irinakom.cardmanagementservice.dto.request.CreateCardRequest;
import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class AdminCardController {

    private final CardService cardService;

    @PostMapping
    public CardResponse createCard(@RequestBody @Valid CreateCardRequest request) {

        return cardService.createCard(request);
    }

    @GetMapping
    public Page<CardResponse> getAllCards(
            @RequestParam(required = false) CardStatus status,
            Pageable pageable
    ) {
        return cardService.getAllCards(status, pageable);
    }

    @PostMapping("/{id}/confirm-block")
    public void confirmBlock(@PathVariable Long id) {
        cardService.confirmBlockCard(id);
    }

    @PostMapping("/{id}/reject-block")
    public void rejectBlock(@PathVariable Long id) {
        cardService.rejectBlockCard(id);
    }

    @PostMapping("/{id}/block")
    public void blockCard(@PathVariable Long id) {
        cardService.blockCard(id);
    }

    @PostMapping("/{id}/activate")
    public void activateCard(@PathVariable Long id) {
        cardService.activateCard(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

}
