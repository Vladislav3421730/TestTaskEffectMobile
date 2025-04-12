package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.request.AddCardRequestDto;
import com.example.testtaskeffectmobile.model.User;

import java.util.UUID;

public interface CardService {

    void save(AddCardRequestDto addCardRequestDto);
    void delete(UUID id);

}
