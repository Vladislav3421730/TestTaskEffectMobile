package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.exception.CardBalanceException;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.factory.TransactionFactory;
import com.example.testtaskeffectmobile.mapper.TransactionMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CardRepository cardRepository;
    private final CardValidationUtils cardValidationUtils;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Page<TransactionDto> findAll(PageRequest pageRequest) {
        return transactionRepository.findAll(pageRequest)
                .map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionDto> findAllByCardId(UUID id, PageRequest pageRequest) {
        return transactionRepository.findByCardId(id, pageRequest)
                .map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionDto> findAllByUserId(UUID id, PageRequest pageRequest) {
        return transactionRepository.findByCardUserId(id, pageRequest)
                .map(transactionMapper::toDto);
    }

    @Override
    public WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto) {

        String cardNumber = withdrawalRequestDto.getNumber();
        BigDecimal withdrawalAmount = withdrawalRequestDto.getAmount();

        Card card = cardRepository.findCardByUserEmailAndNumber(email, cardNumber).orElseThrow(() -> {
                    log.error("Card with number {} wasn't founded", cardNumber);
                    throw new CardNotFoundException(String.format("Card with number %s wasn't found", cardNumber));
                });

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
    public RechargeResponseDto recharge(String email, RechargeRequestDto rechargeRequestDto) {

        String cardNumber = rechargeRequestDto.getNumber();
        BigDecimal rechargeAmount = rechargeRequestDto.getAmount();

        Card card = cardRepository.findCardByUserEmailAndNumber(email, cardNumber).orElseThrow(() -> {
                    log.error("Card with number {} wasn't founded", cardNumber);
                    throw new CardNotFoundException(String.format("Card with number %s wasn't found", cardNumber));
                });

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
}
