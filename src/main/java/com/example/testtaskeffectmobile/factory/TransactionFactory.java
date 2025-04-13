package com.example.testtaskeffectmobile.factory;

import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;

import java.math.BigDecimal;

public class TransactionFactory {

    public static Transaction create(
            Card card,
            BigDecimal amount,
            OperationType operationType,
            OperationResult operationResult) {
        return Transaction.builder()
                .card(card)
                .operation(operationType)
                .operationResult(operationResult)
                .amount(amount)
                .build();
    }
}
