package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.TransactionDto;
import com.example.testtaskeffectmobile.dto.error.AppErrorDto;
import com.example.testtaskeffectmobile.dto.request.RechargeRequestDto;
import com.example.testtaskeffectmobile.dto.request.TransferRequestDto;
import com.example.testtaskeffectmobile.dto.request.WithdrawalRequestDto;
import com.example.testtaskeffectmobile.dto.responce.RechargeResponseDto;
import com.example.testtaskeffectmobile.dto.responce.TransferResponseDto;
import com.example.testtaskeffectmobile.dto.responce.WithdrawalResponseDto;
import com.example.testtaskeffectmobile.service.TransactionService;
import com.example.testtaskeffectmobile.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transaction Management", description = "Endpoints for viewing and managing user transactions")
@SecurityRequirement(name = "Bearer Authentication")
@ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
)
public class TransactionController {

    private final TransactionService transactionService;
    private final TransferService transferService;

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find all transactions"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    @Operation(summary = "Get all transactions", description = "Retrieve a paginated list of all transactions")
    public ResponseEntity<Page<TransactionDto>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "operation", required = false) String operation,
            @RequestParam(value = "operationResult", required = false) String operationResult) {
        Page<TransactionDto> transactions = transactionService.findAll(PageRequest.of(page, pageSize),
                minAmount,
                maxAmount,
                operation,
                operationResult
        );
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{cardId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transactions not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),

    })
    @Operation(summary = "Get transactions by card ID", description = "Retrieve transactions associated with a specific card ID")
    public ResponseEntity<Page<TransactionDto>> findAllByCardId(
            @PathVariable UUID cardId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<TransactionDto> transactions = transactionService.findAllByCardId(cardId, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{userId}/user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transactions not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Get transactions by user ID", description = "Retrieve transactions associated with a specific user ID")
    public ResponseEntity<Page<TransactionDto>> findAllByUserId(
            @PathVariable UUID userId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<TransactionDto> transactions = transactionService.findAllByUserId(userId, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/me")
    @ApiResponse(responseCode = "200", description = "Successfully find transaction")
    @Operation(summary = "Get current user's transactions", description = "Retrieve transactions of the currently authenticated user")
    public ResponseEntity<Page<TransactionDto>> findAllUsersTransactions(
            Principal principal,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<TransactionDto> transactions = transactionService.findAllByUserEmail(principal.getName(), PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/me/{cardId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transactions not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Get current user's transactions by card ID", description = "Retrieve current user's transactions for a specific card")
    public ResponseEntity<Page<TransactionDto>> findAllUsersTransactionsByCardId(
            Principal principal,
            @PathVariable UUID cardId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<TransactionDto> transactions = transactionService
                .findAllByUserEmailAndCardId(principal.getName(), cardId, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/withdrawal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Withdraw money", description = "Initiate a withdrawal transaction for the current user")
    public ResponseEntity<WithdrawalResponseDto> withdrawal(
            Principal principal,
            @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto) {
        WithdrawalResponseDto withdrawalResponseDto = transferService.withdrawal(principal.getName(), withdrawalRequestDto);
        return ResponseEntity.ok(withdrawalResponseDto);
    }

    @PostMapping("/recharge")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Recharge card", description = "Recharge a card with a specified amount for the current user")
    public ResponseEntity<RechargeResponseDto> recharge(
            Principal principal,
            @RequestBody @Valid RechargeRequestDto rechargeRequestDto) {
        RechargeResponseDto rechargeResponseDto = transferService.recharge(principal.getName(), rechargeRequestDto);
        return ResponseEntity.ok(rechargeResponseDto);
    }

    @PostMapping("/transfer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find transaction"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Transfer funds", description = "Transfer money from one card to another")
    public ResponseEntity<TransferResponseDto> transfer(
            Principal principal,
            @RequestBody @Valid TransferRequestDto transferRequestDto) {
        TransferResponseDto transferResponseDto = transferService.transfer(principal.getName(), transferRequestDto);
        return ResponseEntity.ok(transferResponseDto);
    }

}
