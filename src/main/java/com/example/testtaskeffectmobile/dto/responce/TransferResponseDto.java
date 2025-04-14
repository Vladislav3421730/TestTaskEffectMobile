package com.example.testtaskeffectmobile.dto.responce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransferResponseDto {

    private UUID cardId;
    private String number;
    private UUID targetCardId;
    private String targetNumber;
    private BigDecimal transferAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transferTime;
    private BigDecimal balance;
    private UUID userId;
}
