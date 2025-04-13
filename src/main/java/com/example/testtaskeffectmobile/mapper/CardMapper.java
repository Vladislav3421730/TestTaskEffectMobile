package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.model.enums.CardStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {

    @Mapping(source = "user", target = "userId")
    CardDto toDto(Card card);

    default String mapNumberFromCardToCardDto(String decrypted) {
        StringBuilder str = new StringBuilder(decrypted);
        for (int i = 0; i < decrypted.length() - 4; i++) {
            if (decrypted.charAt(i) != ' ') {
                str.replace(i, i + 1, "*");
            }
        }
        return str.toString();
    }

    default UUID mapFromUserToUserId(User user) {
        return user.getId();
    }

    default String mapFromCardStatusToString(CardStatus cardStatus) {
        return cardStatus.name();
    }

}
