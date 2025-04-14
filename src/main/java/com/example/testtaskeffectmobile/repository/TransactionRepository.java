package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByCardId(UUID id, PageRequest pageRequest);

    Page<Transaction> findByCardUserId(UUID id, PageRequest pageRequest);

    Page<Transaction> findByCardUserEmail(String email, PageRequest pageRequest);

    Page<Transaction> findByCardUserEmailAndCardId(String email, UUID id, PageRequest pageRequest);


}
