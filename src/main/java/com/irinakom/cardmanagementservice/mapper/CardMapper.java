package com.irinakom.cardmanagementservice.mapper;

import com.irinakom.cardmanagementservice.dto.response.CardResponse;
import com.irinakom.cardmanagementservice.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(
            target = "maskedCardNumber",
            expression = "java(maskCardNumber(card.getLastFourDigits()))"
    )
    CardResponse toResponse(Card card);

    default String maskCardNumber(String lastFourDigits) {
        if (lastFourDigits == null || lastFourDigits.isEmpty()) {
            return "****";
        }
        return "**** **** **** " + lastFourDigits;
    }
}
