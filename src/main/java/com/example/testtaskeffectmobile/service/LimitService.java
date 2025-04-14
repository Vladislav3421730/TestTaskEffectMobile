package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.LimitDto;
import com.example.testtaskeffectmobile.dto.request.LimitRequestDto;

public interface LimitService {

    LimitDto updateLimit(LimitRequestDto limitRequestDto, boolean isDaily);

}
