package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface TransactionService {
    Page<TransactionDto> findAll(PageRequest pageRequest);

    Page<TransactionDto> findAllByCardId(UUID id, PageRequest pageRequest);

    Page<TransactionDto> findAllByUserId(UUID id, PageRequest pageRequest);


    WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto);

    RechargeResponseDto recharge(String email, RechargeRequestDto rechargeRequestDto);

}
