package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.dto.UserDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    CardMapper cardMapper = Mappers.getMapper(CardMapper.class);

    User toNewEntity(RegisterUserRequestDto registerUserRequestDto);

    @Mapping(source = "cards", target = "cards")
    UserDto toDto(User user);

    default List<CardDto> mapFromCardListToCardDtoList(List<Card> cards) {
        if (cards == null) {
            return null;
        }
        return cards.stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }


}
