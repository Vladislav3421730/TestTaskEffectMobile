package com.example.testtaskeffectmobile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BannedRequestDto {
    @NotNull(message = "banned must be not null")
    private Boolean banned;
}
