package com.example.testtaskeffectmobile.service;

import com.example.testtaskeffectmobile.dto.BlockRequestDto;
import com.example.testtaskeffectmobile.dto.request.BlockCardRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface BlockRequestService {

    BlockRequestDto save(String email, UUID id);

    Page<BlockRequestDto> findAll(PageRequest pageRequest);

    List<BlockRequestDto> findAllByUserId(UUID id);

    List<BlockRequestDto> findAllByUserEmail(String email);

    BlockRequestDto updateStatus(UUID id, BlockCardRequestDto blockCardRequestDto);
}
