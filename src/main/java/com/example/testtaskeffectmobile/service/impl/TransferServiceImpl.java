package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.TransferRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.TransferResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.exception.CardBalanceException;
import com.example.testtaskeffectmobile.exception.CardLimitException;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.exception.CardStatusException;
import com.example.testtaskeffectmobile.factory.TransactionFactory;
import com.example.testtaskeffectmobile.mapper.TransactionMapper;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.repository.TransactionRepository;
import com.example.testtaskeffectmobile.service.TransferService;
import com.example.testtaskeffectmobile.utils.CardValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final CardValidationUtils cardValidationUtils;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(noRollbackFor = {CardLimitException.class, CardBalanceException.class, CardStatusException.class})
    public WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto) {

        String cardNumber = withdrawalRequestDto.getNumber();
        BigDecimal withdrawalAmount = withdrawalRequestDto.getAmount();

        Card card = findCardByUserEmailAndNumber(email, cardNumber);

        cardValidationUtils.validateStatus(card, withdrawalAmount, OperationType.WITHDRAWAL);
        cardValidationUtils.validateBalance(card, withdrawalAmount, OperationType.WITHDRAWAL);
        cardValidationUtils.validateLimit(card, withdrawalAmount, OperationType.WITHDRAWAL);

        card.setBalance(card.getBalance().subtract(withdrawalAmount));
        cardRepository.save(card);

        Transaction transaction = TransactionFactory
                .create(card, withdrawalAmount, OperationType.WITHDRAWAL, OperationResult.SUCCESSFULLY);
        transactionRepository.save(transaction);

        return WithdrawalResponseDto.builder()
                .cardId(card.getId())
                .number(card.getNumber())
                .withdrawalAmount(withdrawalAmount)
                .remainingBalance(card.getBalance())
                .transferTime(LocalDateTime.now())
                .userId(card.getUser().getId())
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {CardStatusException.class})
    public RechargeResponseDto recharge(String email, RechargeRequestDto rechargeRequestDto) {

        String cardNumber = rechargeRequestDto.getNumber();
        BigDecimal rechargeAmount = rechargeRequestDto.getAmount();

        Card card = findCardByUserEmailAndNumber(email, cardNumber);

        cardValidationUtils.validateStatus(card, rechargeAmount, OperationType.RECHARGE);

        card.setBalance(card.getBalance().add(rechargeAmount));
        cardRepository.save(card);

        Transaction transaction = TransactionFactory
                .create(card, rechargeAmount, OperationType.RECHARGE, OperationResult.SUCCESSFULLY);
        transactionRepository.save(transaction);

        return RechargeResponseDto.builder()
                .cardId(card.getId())
                .number(card.getNumber())
                .rechargeAmount(rechargeAmount)
                .balance(card.getBalance())
                .transferTime(LocalDateTime.now())
                .userId(card.getUser().getId())
                .build();

    }

    @Override
    @Transactional(noRollbackFor = {CardBalanceException.class, CardStatusException.class})
    public TransferResponseDto transfer(String email, TransferRequestDto transferRequestDto) {

        String cardNumber = transferRequestDto.getNumber();
        String targetCardNumber = transferRequestDto.getTargetNumber();
        BigDecimal transferAmount = transferRequestDto.getAmount();

        Card card = findCardByUserEmailAndNumber(email, cardNumber);
        cardValidationUtils.validateStatus(card, transferAmount, OperationType.TRANSFER);

        Card targetCard = findCardByUserEmailAndNumber(email, targetCardNumber);
        cardValidationUtils.validateStatus(targetCard, transferAmount, OperationType.TRANSFER);

        cardValidationUtils.validateBalanceForTransfer(card, targetCard, transferAmount, OperationType.TRANSFER);

        card.setBalance(card.getBalance().subtract(transferAmount));
        targetCard.setBalance(targetCard.getBalance().add(transferAmount));

        cardRepository.save(card);
        cardRepository.save(targetCard);

        Transaction transaction = TransactionFactory
                .create(card, targetCard, transferAmount, OperationType.TRANSFER, OperationResult.SUCCESSFULLY);
        transactionRepository.save(transaction);

        return TransferResponseDto.builder()
                .cardId(card.getId())
                .targetCardId(targetCard.getId())
                .number(card.getNumber())
                .targetNumber(targetCard.getNumber())
                .transferTime(LocalDateTime.now())
                .balance(card.getBalance())
                .transferAmount(transferAmount)
                .userId(card.getUser().getId())
                .build();
    }

    private Card findCardByUserEmailAndNumber(String email, String number) {
        return cardRepository.findCardByUserEmailAndNumber(email, number).orElseThrow(() -> {
            log.error("Card with number {} wasn't founded", number);
            throw new CardNotFoundException(String.format("Card with number %s wasn't found", number));
        });
    }
}
