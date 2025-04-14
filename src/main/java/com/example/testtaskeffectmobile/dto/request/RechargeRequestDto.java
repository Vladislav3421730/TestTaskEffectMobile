package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.CardNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO for recharging a card")
public class RechargeRequestDto {

    @CardNumber
    @Schema(description = "Card number to recharge", example = "1234 5678 9012 3456")
    private String number;

    @DecimalMin(value = "5.00", message = "Minimum recharge amount is 5.00")
    @Schema(description = "Recharge amount", example = "100.00", minimum = "5.00")
    private BigDecimal amount;
}
