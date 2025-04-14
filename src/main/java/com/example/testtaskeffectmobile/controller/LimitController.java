package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.LimitDto;
import com.example.testtaskeffectmobile.dto.request.LimitRequestDto;
import com.example.testtaskeffectmobile.service.LimitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/limit")
public class LimitController {

    private final LimitService limitService;

    @PutMapping("/daily")
    public ResponseEntity<LimitDto> setDayLimit(@RequestBody @Valid LimitRequestDto limitRequestDto) {
        LimitDto limitDto = limitService.updateLimit(limitRequestDto, true);
        return ResponseEntity.ok(limitDto);
    }

    @PutMapping("/monthly")
    public ResponseEntity<LimitDto> setMonthLimit(@RequestBody @Valid LimitRequestDto limitRequestDto) {
        LimitDto limitDto = limitService.updateLimit(limitRequestDto, false);
        return ResponseEntity.ok(limitDto);
    }

}
