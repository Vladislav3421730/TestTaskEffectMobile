package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;

public interface TransactionService {

    WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto);

}
