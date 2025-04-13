package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.CardNumber;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawalRequestDto {

    @CardNumber
    private String number;

    @DecimalMin(value = "5.00", message = "Minimum withdrawal amount is 10")
    private BigDecimal amount;
}
