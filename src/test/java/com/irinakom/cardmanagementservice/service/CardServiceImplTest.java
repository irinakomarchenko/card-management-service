package com.irinakom.cardmanagementservice.service;

import com.irinakom.cardmanagementservice.entity.Card;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.exception.ResourceNotFoundException;
import com.irinakom.cardmanagementservice.repository.CardRepository;
import com.irinakom.cardmanagementservice.repository.UserRepository;
import com.irinakom.cardmanagementservice.service.impl.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void requestBlock_success() {
        User user = new User();
        user.setId(1L);

        Card card = new Card();
        card.setId(1L);
        card.setOwner(user);
        card.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findByIdAndOwner(1L, user))
                .thenReturn(Optional.of(card));

        cardService.requestCardBlock(user, 1L);

        assertEquals(CardStatus.PENDING_BLOCK, card.getStatus());
    }

    @Test
    void requestBlock_notFound() {
        User user = new User();

        when(cardRepository.findByIdAndOwner(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> cardService.requestCardBlock(user, 1L)
        );
    }
}
