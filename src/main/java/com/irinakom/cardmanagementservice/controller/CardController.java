package com.irinakom.cardmanagementservice.controller;

import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.Role;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.security.CustomUserDetails;
import com.irinakom.cardmanagementservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public Page<CardResponse> getMyCards(
            @RequestParam(required = false) CardStatus status,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return cardService.getUserCards(userDetails.getUser(), status, pageable);
    }

    @GetMapping("/{id}")
    public CardResponse getMyCard(@PathVariable Long id) {

        return cardService.getUserCardById(getCurrentUser(), id);
    }

    @PostMapping("/{id}/block-request")
    public void requestBlock(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        cardService.requestCardBlock(userDetails.getUser(), id);
    }

    private User getCurrentUser() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        return user;
    }
}

