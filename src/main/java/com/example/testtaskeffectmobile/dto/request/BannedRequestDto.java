package com.example.testtaskeffectmobile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to change banned status of a user or card")
public class BannedRequestDto {

    @NotNull(message = "banned must be not null")
    @Schema(description = "Indicates whether the entity should be banned (true) or unbanned (false)", example = "true")
    private Boolean banned;
}
