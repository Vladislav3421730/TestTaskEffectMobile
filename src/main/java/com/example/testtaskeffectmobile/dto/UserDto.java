package com.example.testtaskeffectmobile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "DTO representing a user and their associated cards.")
public class UserDto {

    @Schema(description = "Unique identifier for the user", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Email address of the user", example = "user@example.com")
    private String email;

    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "List of cards associated with the user")
    private List<CardDto> cards;

    @Schema(description = "Ban status of the user", example = "false")
    private Boolean isBan;
}
