package com.example.testtaskeffectmobile.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.UUID;

@Data
public class AddCardRequestDto {
    private UUID userId;

    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$", message = "Card number must be in the format '#### #### #### ####'")
    private String number;
}
