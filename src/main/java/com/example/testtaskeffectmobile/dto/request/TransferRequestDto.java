package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.CardNumber;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    @CardNumber
    private String number;

    @CardNumber
    private String targetNumber;

    @DecimalMin(value = "5.00", message = "Minimum amount for transfer is 5.00")
    private BigDecimal amount;
}
