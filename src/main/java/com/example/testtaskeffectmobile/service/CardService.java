package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.dto.request.AddCardRequestDto;
import com.example.testtaskeffectmobile.dto.request.StatusCardRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CardService {

    CardDto save(AddCardRequestDto addCardRequestDto);

    void delete(UUID id);

    void updateStatus(UUID id, StatusCardRequestDto status);

    Page<CardDto> findAll(PageRequest pageRequest, BigDecimal minBalance, BigDecimal maxBalance, LocalDate expiredBefore, LocalDate expiredAfter);

    Page<CardDto> findAllByUserEmail(String email, PageRequest pageRequest);

    CardDto findById(UUID id);


}
