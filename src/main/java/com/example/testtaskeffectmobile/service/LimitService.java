package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.LimitDto;
import com.example.testtaskeffectmobile.dto.request.LimitRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface LimitService {

    LimitDto updateLimit(LimitRequestDto limitRequestDto, boolean isDaily);

    Page<LimitDto> findAll(PageRequest pageRequest);

    LimitDto findByCardId(UUID id);

}
