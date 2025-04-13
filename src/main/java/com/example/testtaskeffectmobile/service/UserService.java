package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.UserDto;
import com.example.testtaskeffectmobile.dto.request.BannedRequestDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;
import com.example.testtaskeffectmobile.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface UserService {
    void save(RegisterUserRequestDto registerUserRequestDto);
    void delete(UUID id);
    void banUser(UUID id, BannedRequestDto bannedRequestDto);
    Page<UserDto> findAll(PageRequest pageRequest);
    UserDto findById(UUID id);
}
