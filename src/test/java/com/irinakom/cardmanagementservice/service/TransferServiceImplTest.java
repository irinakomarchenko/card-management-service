package com.irinakom.cardmanagementservice.service;

import com.irinakom.cardmanagementservice.entity.Card;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.exception.InsufficientFundsException;
import com.irinakom.cardmanagementservice.repository.CardRepository;
import com.irinakom.cardmanagementservice.service.impl.TransferServiceImpl;
import com.irinakom.cardmanagementservice.dto.request.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private User user;
    private Card from;
    private Card to;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        from = new Card();
        from.setId(1L);
        from.setOwner(user);
        from.setStatus(CardStatus.ACTIVE);
        from.setBalance(BigDecimal.valueOf(500));
        from.setExpirationDate(LocalDate.now().plusYears(1));

        to = new Card();
        to.setId(2L);
        to.setOwner(user);
        to.setStatus(CardStatus.ACTIVE);
        to.setBalance(BigDecimal.valueOf(100));
        to.setExpirationDate(LocalDate.now().plusYears(1));
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        TransferRequest request =
                new TransferRequest(1L, 2L, BigDecimal.valueOf(200));

        when(cardRepository.findByIdAndOwnerWithLock(1L, user))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerWithLock(2L, user))
                .thenReturn(Optional.of(to));

        transferService.transfer(user, request);

        verify(cardRepository).findByIdAndOwnerWithLock(1L, user);
        verify(cardRepository).findByIdAndOwnerWithLock(2L, user);

        assert from.getBalance().equals(BigDecimal.valueOf(300));
        assert to.getBalance().equals(BigDecimal.valueOf(300));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        TransferRequest request =
                new TransferRequest(1L, 2L, BigDecimal.valueOf(1000));

        when(cardRepository.findByIdAndOwnerWithLock(1L, user))
                .thenReturn(Optional.of(from));
        when(cardRepository.findByIdAndOwnerWithLock(2L, user))
                .thenReturn(Optional.of(to));

        assertThatThrownBy(() -> transferService.transfer(user, request))
                .isInstanceOf(InsufficientFundsException.class);
    }
}
