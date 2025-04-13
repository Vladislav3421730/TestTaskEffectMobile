package com.example.testtaskeffectmobile.utils;

import com.example.testtaskeffectmobile.exception.CardBalanceException;
import com.example.testtaskeffectmobile.exception.CardLimitException;
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
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardValidationUtils {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public void validateStatus(Card card, BigDecimal amount, OperationType operationType) {
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

    public void validateBalance(Card card, BigDecimal amount, OperationType operationType) {
        if (card.getBalance().compareTo(amount) < 0) {
            Transaction failedTransaction = TransactionFactory
                    .create(card, amount, operationType, OperationResult.FAILED);
            transactionRepository.save(failedTransaction);
            log.error("Amount {} more than balance {}", amount, card.getBalance());
            throw new CardBalanceException(String.format("Amount %s more than balance %s", amount, card.getBalance()));
        }
    }

    public void validateLimit(Card card, BigDecimal amount, OperationType operationType) {
        Optional<BigDecimal> dayAmount = cardRepository.findTodayWithdrawalsByCardId(card.getId());

        if (dayAmount.isPresent() &&
                dayAmount.get().add(amount).compareTo(card.getLimit().getDailyLimit()) > 0) {
            Transaction failedTransaction = TransactionFactory
                    .create(card, amount, operationType, OperationResult.FAILED);
            transactionRepository.save(failedTransaction);
            log.error("Amount {} and day's withdrawal {} more than limit this day {}",
                    amount, dayAmount, card.getLimit().getCard());
            throw new CardLimitException(String.format("Amount %s and day's withdrawal %s more than limit this day %s",
                    amount, dayAmount, card.getLimit().getCard()));
        }

        Optional<BigDecimal> monthAmount = cardRepository.findTotalWithdrawalsForCurrentMonthByCardId(card.getId());

        if (monthAmount.isPresent() &&
                monthAmount.get().add(amount).compareTo(card.getLimit().getDailyLimit()) > 0) {
            Transaction failedTransaction = TransactionFactory
                    .create(card, amount, operationType, OperationResult.FAILED);
            transactionRepository.save(failedTransaction);
            log.error("Amount {} and month withdrawal {} more than limit this mont {}",
                    amount, dayAmount, card.getLimit().getCard());
            throw new CardLimitException(String.format("Amount %s and month withdrawal %s more than limit this month %s",
                    amount, dayAmount, card.getLimit().getCard()));
        }

    }

}
