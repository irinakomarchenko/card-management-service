--liquibase formatted sql
--changeset irinakom:003-create-cards-table

CREATE TABLE IF NOT EXISTS cards (
                                     id BIGSERIAL PRIMARY KEY,
                                     encrypted_card_number VARCHAR(512) NOT NULL UNIQUE,
    last_four_digits VARCHAR(4) NOT NULL,
    user_id BIGINT NOT NULL,
    expiration_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE', 'PENDING_BLOCK', 'BLOCKED', 'EXPIRED')),
    balance NUMERIC(19,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_cards_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    );

CREATE INDEX idx_cards_user_id ON cards(user_id);
CREATE INDEX idx_cards_status ON cards(status);

--rollback DROP TABLE IF EXISTS cards;
