package com.irinakom.cardmanagementservice.repository;

import com.irinakom.cardmanagementservice.entity.Card;
import com.irinakom.cardmanagementservice.entity.CardStatus;
import com.irinakom.cardmanagementservice.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Page<Card> findByStatus(CardStatus status, Pageable pageable);
    Page<Card> findByOwner(User owner, Pageable pageable);
    Page<Card> findByOwnerAndStatus(User owner, CardStatus status, Pageable pageable);
    Optional<Card> findByIdAndOwner(Long id, User owner);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT c FROM Card c
        WHERE c.id = :id AND c.owner = :owner
    """)
    Optional<Card> findByIdAndOwnerWithLock(Long id, User owner);
}
