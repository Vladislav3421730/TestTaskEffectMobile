package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.BlockRequestDto;
import com.example.testtaskeffectmobile.model.BlockRequest;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.model.enums.BlockStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlockRequestMapper {

    @Mapping(source = "user", target = "userId")
    @Mapping(source = "card", target = "cardId")
    @Mapping(source = "blockStatusType", target = "blockStatus")
    BlockRequestDto toDto(BlockRequest blockRequest);

    default String mapFromBlockStatusToString(BlockStatusType blockStatusType) {
        return blockStatusType.name();
    }

    default UUID mapFromUserToUserId(User user) {
        return user.getId();
    }

    default UUID mapFromCardToCardId(Card card) {
        return card.getId();
    }





}
