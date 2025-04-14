package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for updating the card status")
public class StatusCardRequestDto {

    @NotNull(message = "Status must not be null")
    @Status
    @Schema(description = "New card status. The allowed values are defined in the custom @Status annotation", example = "ACTIVE")
    private String status;
}

