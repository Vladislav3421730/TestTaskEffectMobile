package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> findAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        Page<TransactionDto> transactions = transactionService.findAll(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<TransactionDto>> findAllByCardId(
            @PathVariable UUID id,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        Page<TransactionDto> transactions = transactionService.findAllByCardId(id, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<Page<TransactionDto>> findAllByUserId(
            @PathVariable UUID id,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        Page<TransactionDto> transactions = transactionService.findAllByUserId(id, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }


    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseDto> withdrawal(
            Principal principal,
            @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto) {
        WithdrawalResponseDto withdrawalResponseDto = transactionService.withdrawal(principal.getName(), withdrawalRequestDto);
        return ResponseEntity.ok(withdrawalResponseDto);
    }

    @PostMapping("/recharge")
    public ResponseEntity<RechargeResponseDto> recharge(
            Principal principal,
            @RequestBody @Valid RechargeRequestDto rechargeRequestDto) {
        RechargeResponseDto rechargeResponseDto = transactionService.recharge(principal.getName(), rechargeRequestDto);
        return ResponseEntity.ok(rechargeResponseDto);
    }

}
