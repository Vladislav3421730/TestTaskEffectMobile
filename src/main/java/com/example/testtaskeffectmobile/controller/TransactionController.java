package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseDto> withdrawal(
            Principal principal,
            @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto) {
        WithdrawalResponseDto withdrawalResponseDto = transactionService.withdrawal(principal.getName(), withdrawalRequestDto);
        return ResponseEntity.ok(withdrawalResponseDto);
    }

}
