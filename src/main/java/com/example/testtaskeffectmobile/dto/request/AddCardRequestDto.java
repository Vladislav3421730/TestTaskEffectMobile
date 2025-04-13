package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.CardNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddCardRequestDto {

    @NotNull(message = "userId must be not null")
    private UUID userId;

    @CardNumber
    @NotBlank(message = "Card number must not be blank")
    private String number;
}
