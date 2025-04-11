package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.JwtResponseDto;
import com.example.testtaskeffectmobile.dto.LoginUserDto;
import com.example.testtaskeffectmobile.dto.RegisterUserDto;

public interface AuthService {

    JwtResponseDto createAuthToken(LoginUserDto user);
    void registerUser(RegisterUserDto user);
}
