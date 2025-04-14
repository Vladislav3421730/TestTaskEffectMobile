package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.TransactionDto;
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
import com.example.testtaskeffectmobile.repository.CustomTransactionRepository;
import com.example.testtaskeffectmobile.repository.TransactionRepository;
import com.example.testtaskeffectmobile.repository.TransactionRepositoryCustomImpl;
import com.example.testtaskeffectmobile.service.TransactionService;
import com.example.testtaskeffectmobile.utils.CardValidationUtils;
import com.example.testtaskeffectmobile.utils.EnumValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomTransactionRepository customTransactionRepository;
    private final TransactionMapper transactionMapper;


    public Page<TransactionDto> findAll(Pageable pageable, BigDecimal minAmount, BigDecimal maxAmount,
                                        String operation, String operationResult) {

        OperationType operationTypeEnum = EnumValidation.safeParseEnum(OperationType.class, operation);
        OperationResult operationResultEnum = EnumValidation.safeParseEnum(OperationResult.class, operationResult);

        return customTransactionRepository.findByFilters(
                        minAmount,
                        maxAmount,
                        operationTypeEnum,
                        operationResultEnum,
                        pageable)
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
    public Page<TransactionDto> findAllByUserEmail(String email, PageRequest pageRequest) {
        return transactionRepository.findByCardUserEmail(email, pageRequest)
                .map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionDto> findAllByUserEmailAndCardId(String email, UUID id, PageRequest pageRequest) {
        return transactionRepository.findByCardUserEmailAndCardId(email, id, pageRequest)
                .map(transactionMapper::toDto);
    }
}
