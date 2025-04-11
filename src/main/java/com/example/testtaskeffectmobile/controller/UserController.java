package com.example.testtaskeffectmobile.controller;

import com.example.testtaskeffectmobile.dto.request.BannedRequestDto;
import com.example.testtaskeffectmobile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ban/{id}")
    public ResponseEntity<Void> banUser(@PathVariable UUID id, @RequestBody BannedRequestDto bannedRequestDto) {
        userService.banUser(id, bannedRequestDto);
        return ResponseEntity.noContent().build();
    }


}
