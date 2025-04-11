package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.responce.JwtResponseDto;
import com.example.testtaskeffectmobile.dto.request.LoginUserRequestDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;

public interface AuthService {

    JwtResponseDto createAuthToken(LoginUserRequestDto user);
    void registerUser(RegisterUserRequestDto user);
}
