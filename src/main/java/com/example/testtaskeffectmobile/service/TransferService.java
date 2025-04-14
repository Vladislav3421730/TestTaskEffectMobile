package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.TransferRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.TransferResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;

public interface TransferService {


    WithdrawalResponseDto withdrawal(String email, WithdrawalRequestDto withdrawalRequestDto);

    RechargeResponseDto recharge(String email, RechargeRequestDto rechargeRequestDto);

    TransferResponseDto transfer(String email, TransferRequestDto transferRequestDto);
}
