package com.example.testtaskeffectmobile.mapper;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mapping(source = "card", target = "cardId")
    @Mapping(source = "targetCard", target = "targetCardId")
    @Mapping(source = "operation", target = "operationType")
    TransactionDto toDto(Transaction transaction);

    default String mapOperationTypeToDto(OperationType operationType) {
        return operationType.name();
    }

    default String mapOperationResultToDto(OperationResult operationResult) {
        return operationResult.name();
    }

    default UUID mapCardToDto(Card card) {
        return card==null ? null : card.getId();
    }
}
