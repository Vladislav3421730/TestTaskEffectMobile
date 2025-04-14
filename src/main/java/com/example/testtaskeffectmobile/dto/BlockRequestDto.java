package com.example.testtaskeffectmobile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "DTO representing a block card request")
public class BlockRequestDto {

    @Schema(description = "Unique identifier of the block request", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Creation date and time of the block request", example = "2025-04-14 12:30:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date and time of the block request", example = "2025-04-14 12:45:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Current status of the block request", example = "PENDING")
    private String blockStatus;

    @Schema(description = "ID of the user who requested the block", example = "6e5d5c99-2c79-4a1d-9cc8-2dd4b934cd0d")
    private UUID userId;

    @Schema(description = "ID of the card to be blocked", example = "bb456e89-291a-4c87-9b01-b7fdf03489f0")
    private UUID cardId;
}
