package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.BlockRequestDto;
import com.example.testtaskeffectmobile.dto.error.AppErrorDto;
import com.example.testtaskeffectmobile.dto.error.FieldErrorDto;
import com.example.testtaskeffectmobile.dto.request.BlockCardRequestDto;
import com.example.testtaskeffectmobile.service.BlockRequestService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/block")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Block Requests", description = "Endpoints for managing block card requests")
@ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
)
public class BlockRequestController {

    private final BlockRequestService blockRequestService;

    @GetMapping
    @Operation(summary = "Find all block requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of block requests returned successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<Page<BlockRequestDto>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        Page<BlockRequestDto> blockRequests =  blockRequestService.findAll(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(blockRequests);
    }

    @PatchMapping("/status/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Block requests updated successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Request wasn't found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =  FieldErrorDto.class))
            )
    })
    @Operation(summary = "Update the status of a block request by ID")
    public ResponseEntity<BlockRequestDto> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid BlockCardRequestDto blockCardRequestDto) {
        BlockRequestDto blockRequestDto = blockRequestService.updateStatus(id, blockCardRequestDto);
        return ResponseEntity.ok(blockRequestDto);
    }

    @GetMapping("/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of block requests returned successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Request wasn't found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Get all block requests by user ID")
    public ResponseEntity<List<BlockRequestDto>> findAllByUserId(@PathVariable UUID userId) {
        List<BlockRequestDto> blockRequests =  blockRequestService.findAllByUserId(userId);
        return ResponseEntity.ok(blockRequests);
    }

    @GetMapping("/me")
    @ApiResponse(responseCode = "200", description = "List of block requests returned successfully")
    @Operation(summary = "Get all block requests for the current user")
    public ResponseEntity<List<BlockRequestDto>> findAllByUserEmail(Principal principal) {
        List<BlockRequestDto> blockRequests =  blockRequestService.findAllByUserEmail(principal.getName());
        return ResponseEntity.ok(blockRequests);
    }

    @PostMapping("/card/{cardId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of block requests returned successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Request wasn't found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
    })
    @Operation(summary = "Create a new block request for a card")
    public ResponseEntity<BlockRequestDto> createRequest(Principal principal, @PathVariable UUID cardId) {
        BlockRequestDto blockRequestDto = blockRequestService.save(principal.getName(), cardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(blockRequestDto);
    }


}
