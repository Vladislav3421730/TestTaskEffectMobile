package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.BlockRequestDto;
import com.example.testtaskeffectmobile.dto.request.BlockCardRequestDto;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.mapper.BlockRequestMapper;
import com.example.testtaskeffectmobile.model.BlockRequest;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.enums.BlockStatusType;
import com.example.testtaskeffectmobile.repository.BlockRequestRepository;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.service.BlockRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockRequestServiceImpl implements BlockRequestService {

    private final BlockRequestRepository blockRequestRepository;
    private final CardRepository cardRepository;
    private final BlockRequestMapper blockRequestMapper;

    @Override
    @Transactional
    public BlockRequestDto save(String email, UUID id) {
        Card card = cardRepository.findCardByUserEmailAndId(email, id).orElseThrow(() -> {
            log.error("Card with id {} wasn't founded", id);
            throw new CardNotFoundException(String.format("Card with id %s wasn't found", id));
        });
        BlockRequest blockRequest = new BlockRequest();
        blockRequest.setCard(card);
        blockRequest.setUser(card.getUser());

        BlockRequest saveBlockRequest = blockRequestRepository.save(blockRequest);
        log.info("Request on card {} was created successfully", card.getId());
        return blockRequestMapper.toDto(saveBlockRequest);
    }

    @Override
    public Page<BlockRequestDto> findAll(PageRequest pageRequest) {
        return blockRequestRepository.findAll(pageRequest)
                .map(blockRequestMapper::toDto);
    }

    @Override
    public List<BlockRequestDto> findAllByUserId(UUID id) {
        return blockRequestRepository.findAllByUserId(id).stream()
                .map(blockRequestMapper::toDto)
                .toList();
    }

    @Override
    public List<BlockRequestDto> findAllByUserEmail(String email) {
        return blockRequestRepository.findAllByUserEmail(email).stream()
                .map(blockRequestMapper::toDto)
                .toList();
    }

    @Override
    public BlockRequestDto updateStatus(UUID id, BlockCardRequestDto blockCardRequestDto) {
        BlockRequest blockRequest = blockRequestRepository.findById(id).orElseThrow(() -> {
            log.error("BlockRequest with id {} wasn't founded", id);
            throw new CardNotFoundException(String.format("BlockRequest with id %s wasn't found", id));
        });

        BlockStatusType blockStatusType = BlockStatusType.valueOf(blockCardRequestDto.getStatus());
        blockRequest.setBlockStatusType(blockStatusType);
        blockRequest.setUpdatedAt(LocalDateTime.now());
        BlockRequest updatedBlockRequest = blockRequestRepository.save(blockRequest);

        log.info("Request with id {} was updated with status {}", id, blockStatusType.name());
        return blockRequestMapper.toDto(updatedBlockRequest);
    }
}
