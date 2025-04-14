package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CustomCardRepository {

    Page<Card> findByFilters (BigDecimal minBalance, BigDecimal maxBalance, LocalDate expiredBefore, LocalDate expiredAfter, Pageable pageable);
}
