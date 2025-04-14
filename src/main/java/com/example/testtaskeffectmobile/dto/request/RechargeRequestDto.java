package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.CardNumber;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeRequestDto {
    @CardNumber
    private String number;

    @DecimalMin(value = "5.00", message = "Minimum recharge amount is 5.00")
    private BigDecimal amount;
}
