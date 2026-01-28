package com.irinakom.cardmanagementservice.controller;

import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.Role;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public Page<CardResponse> getMyCards(
            @RequestParam(required = false) CardStatus status,
            Pageable pageable
    ) {
        return cardService.getUserCards(getCurrentUser(), status, pageable);
    }

    @GetMapping("/{id}")
    public CardResponse getMyCard(@PathVariable Long id) {

        return cardService.getUserCardById(getCurrentUser(), id);
    }

    @PostMapping("/{id}/block-request")
    public void requestBlock(@PathVariable Long id) {

        cardService.requestCardBlock(getCurrentUser(), id);
    }


    private User getCurrentUser() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        return user;
    }
}

