package com.example.testtaskeffectmobile.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LimitRequestDto {

    @NotNull(message = "cardId must be not null")
    private UUID cardId;

    @NotNull(message = "limit must be not null")
    @DecimalMin(value = "0.00", message = "min amount for limit is 0")
    private BigDecimal limit;
}
