package com.example.testtaskeffectmobile.utils;

import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.exception.CardStatusException;
import com.example.testtaskeffectmobile.factory.TransactionFactory;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.CardStatus;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardValidationUtils {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;


    public void validate(Card card, BigDecimal amount, OperationType operationType) {
        if (card.getStatus().equals(CardStatus.BLOCKED)) {
            log.error("Operation forbidden, card with id {} was blocked", card.getId());

            Transaction transaction = TransactionFactory.create(card, amount, operationType, OperationResult.CARD_BLOCKED);
            transactionRepository.save(transaction);

            throw new CardStatusException(String.format("Operation forbidden, card with id %s was blocked", card.getId()));
        }
        if (card.getExpirationDate().equals(LocalDate.now())) {
            log.error("Operation forbidden, card with id {} was expired at {}", card.getId(), card.getExpirationDate());
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);

            Transaction transaction = TransactionFactory.create(card, amount, operationType, OperationResult.CARD_EXPIRED);
            transactionRepository.save(transaction);

            throw new CardStatusException(String.format("Operation forbidden, card with id %s was expired at %s", card.getId(), card.getExpirationDate()));
        }
    }

}
