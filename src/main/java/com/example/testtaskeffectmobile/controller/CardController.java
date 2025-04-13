package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.dto.error.AppErrorDto;
import com.example.testtaskeffectmobile.dto.request.AddCardRequestDto;
import com.example.testtaskeffectmobile.dto.request.StatusCardRequestDto;
import com.example.testtaskeffectmobile.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "Endpoints for managing cards (create, delete, get, update status)")
public class CardController {

    private final CardService cardService;

    @GetMapping
    @Operation(summary = "Find all cards")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cards returned successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Page<CardDto>> findAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        Page<CardDto> cards = cardService.findAll(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/me")
    @Operation(summary = "Find all cards for authorized user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of cards returned successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Page<CardDto>> findAllUserCards(
            Principal principal,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (page == null) page = 0;
        if (pageSize == null) pageSize = 20;
        Page<CardDto> cards = cardService.findAllByUserEmail(principal.getName(), PageRequest.of(page, pageSize));
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get card by ID", description = "Retrieve card information by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "card found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<CardDto> findById(@PathVariable UUID id) {
        CardDto cardDto = cardService.findById(id);
        return ResponseEntity.ok(cardDto);
    }

    @PostMapping
    @Operation(summary = "Add new card", description = "Creates and saves a new card")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card successfully created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid card data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Void> saveCard(
            @RequestBody @Valid AddCardRequestDto addCardRequestDto) {
        cardService.save(addCardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete card", description = "Deletes a card by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Card successfully deleted"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update card status", description = "Changes the status of a card (e.g., ACTIVE, BLOCKED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card status successfully updated"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID id,
            @RequestBody @Valid StatusCardRequestDto statusCardRequestDto) {
        cardService.updateStatus(id, statusCardRequestDto);
        return ResponseEntity.ok().build();
    }

}
