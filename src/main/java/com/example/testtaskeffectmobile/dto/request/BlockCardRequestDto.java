package com.example.testtaskeffectmobile.dto.request;

import com.example.testtaskeffectmobile.validation.BlockStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for updating the status of a block request")
public class BlockCardRequestDto {

    @Schema(description = "New status of the block request. Allowed values: CREATED, COMPLETED, REJECTED", example = "CREATED")
    @BlockStatus
    @NotNull(message = "status must be not null")
    private String status;
}
