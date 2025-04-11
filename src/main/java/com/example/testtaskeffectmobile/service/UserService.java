package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.request.BannedRequestDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;

import java.util.UUID;

public interface UserService {
    void save(RegisterUserRequestDto registerUserRequestDto);
    void delete(UUID id);
    void banUser(UUID id, BannedRequestDto bannedRequestDto);
}
