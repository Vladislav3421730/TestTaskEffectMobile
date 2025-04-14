package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CustomTransactionRepository {

    Page<Transaction> findByFilters(BigDecimal minAmount,
                                    BigDecimal maxAmount,
                                    OperationType operation,
                                    OperationResult operationResult, Pageable pageable);
}
