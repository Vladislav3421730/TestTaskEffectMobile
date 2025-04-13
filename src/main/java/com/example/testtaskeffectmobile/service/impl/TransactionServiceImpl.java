package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.convertor.CardNumberCryptoConverter;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.exception.CardBalanceException;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.factory.TransactionFactory;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.repository.TransactionRepository;
import com.example.testtaskeffectmobile.service.TransactionService;
import com.example.testtaskeffectmobile.utils.CardValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CardRepository cardRepository;
    private final CardValidationUtils cardValidationUtils;
    private final TransactionRepository transactionRepository;
    private final CardNumberCryptoConverter cardNumberCryptoConverter;

    @Override
    public WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto) {
        Card card = cardRepository.findCardByUserEmailAndNumber(email,
                        cardNumberCryptoConverter.convertToDatabaseColumn(withdrawalRequestDto.getNumber()))
                .orElseThrow(() -> {
                    log.error("Card with number {} wasn't founded", withdrawalRequestDto.getNumber());
                    throw new CardNotFoundException(String.format("Card with if %s wasn't found", withdrawalRequestDto.getNumber()));
                });
        cardValidationUtils.validate(card, withdrawalRequestDto.getAmount(), OperationType.WITHDRAWAL);
        if (card.getBalance().compareTo(withdrawalRequestDto.getAmount()) < 0) {
            Transaction failedTransaction = TransactionFactory
                    .create(card, withdrawalRequestDto.getAmount(), OperationType.WITHDRAWAL, OperationResult.FAILED);
            transactionRepository.save(failedTransaction);
            log.error("Amount {} more than balance {}", withdrawalRequestDto.getAmount(), card.getBalance());
            throw new CardBalanceException(String.format("Amount %s more than balance %s", withdrawalRequestDto.getAmount(), card.getBalance()));
        }
        card.setBalance(card.getBalance().subtract(withdrawalRequestDto.getAmount()));

        Transaction transaction = TransactionFactory
                .create(card, withdrawalRequestDto.getAmount(), OperationType.WITHDRAWAL, OperationResult.SUCCESSFULLY);
        transactionRepository.save(transaction);

        return WithdrawalResponseDto.builder()
                .cardId(card.getId())
                .number(card.getNumber())
                .withdrawalAmount(withdrawalRequestDto.getAmount())
                .remainingBalance(card.getBalance())
                .transferTime(LocalDateTime.now())
                .userId(card.getUser().getId())
                .build();
    }
}
