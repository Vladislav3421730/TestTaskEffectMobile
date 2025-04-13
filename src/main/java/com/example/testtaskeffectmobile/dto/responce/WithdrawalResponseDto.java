package com.example.testtaskeffectmobile.dto.responce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WithdrawalResponseDto {
    private UUID cardId;
    private String number;
    private BigDecimal withdrawalAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transferTime;
    private BigDecimal remainingBalance;
    private UUID userId;
}
