package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusCardRequestDto {

    @NotNull(message = "Status mus be not null")
    @Status
    private String status;
}
