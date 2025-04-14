package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface TransactionService {
    Page<TransactionDto> findAll(PageRequest pageRequest);

    Page<TransactionDto> findAllByCardId(UUID id, PageRequest pageRequest);

    Page<TransactionDto> findAllByUserId(UUID id, PageRequest pageRequest);

    Page<TransactionDto> findAllByUserEmail(String email, PageRequest pageRequest);

    Page<TransactionDto> findAllByUserEmailAndCardId(String email, UUID id, PageRequest pageRequest);
}
