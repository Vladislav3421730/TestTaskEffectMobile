package com.example.testtaskeffectmobile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDto {

    private UUID id;
    private BigDecimal amount;
    private String operationType;
    private String operationResult;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private UUID cardId;
    private UUID targetCardId;
}
