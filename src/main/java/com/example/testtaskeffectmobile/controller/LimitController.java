package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.LimitDto;
import com.example.testtaskeffectmobile.dto.error.AppErrorDto;
import com.example.testtaskeffectmobile.dto.error.FieldErrorDto;
import com.example.testtaskeffectmobile.dto.request.LimitRequestDto;
import com.example.testtaskeffectmobile.service.LimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/limit")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
        ),
})
@Tag(name = "Limit Management", description = "Endpoints for managing daily and monthly spending limits")
@SecurityRequirement(name = "Bearer Authentication")
public class LimitController {

    private final LimitService limitService;

    @GetMapping
    @Operation(summary = "find all limits")
    public ResponseEntity<Page<LimitDto>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<LimitDto> limits = limitService.findAll(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(limits);
    }

    @GetMapping("/{cardId}")
    @Operation(summary = "find all limit by card id")
    @ApiResponse(
            responseCode = "404",
            description = "Limit not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
    )
    public ResponseEntity<LimitDto> findByCardId(@PathVariable UUID cardId) {
        LimitDto limit = limitService.findByCardId(cardId);
        return ResponseEntity.ok(limit);
    }

    @PutMapping("/daily")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Limit not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Set daily limit", description = "Updates the user's daily spending limit")
    public ResponseEntity<LimitDto> setDayLimit(@RequestBody @Valid LimitRequestDto limitRequestDto) {
        LimitDto limitDto = limitService.updateLimit(limitRequestDto, true);
        return ResponseEntity.ok(limitDto);
    }

    @PutMapping("/monthly")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Limit not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Set monthly limit", description = "Updates the user's monthly spending limit")
    public ResponseEntity<LimitDto> setMonthLimit(@RequestBody @Valid LimitRequestDto limitRequestDto) {
        LimitDto limitDto = limitService.updateLimit(limitRequestDto, false);
        return ResponseEntity.ok(limitDto);
    }

}
