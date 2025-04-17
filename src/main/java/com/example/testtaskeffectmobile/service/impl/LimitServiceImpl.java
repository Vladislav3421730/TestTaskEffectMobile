package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.LimitDto;
import com.example.testtaskeffectmobile.dto.request.LimitRequestDto;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.exception.LimitNotFoundException;
import com.example.testtaskeffectmobile.mapper.LimitMapper;
import com.example.testtaskeffectmobile.model.Limit;
import com.example.testtaskeffectmobile.repository.LimitRepository;
import com.example.testtaskeffectmobile.service.LimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitServiceImpl implements LimitService {

    private final LimitRepository limitRepository;
    private final LimitMapper limitMapper;

    @Override
    public LimitDto updateLimit(LimitRequestDto limitRequestDto, boolean isDaily) {
        UUID cardId = limitRequestDto.getCardId();
        Limit limit = limitRepository.findByCardId(cardId).orElseThrow(() -> {
            log.error("Limit wasn't found by card id {}", cardId);
            throw new LimitNotFoundException(String.format("Limit wasn't found by card id %s", cardId));
        });
        limit.setUpdatedAt(LocalDateTime.now());
        if (isDaily) {
            limit.setDailyLimit(limitRequestDto.getLimit());
        } else {
            limit.setMonthlyLimit(limitRequestDto.getLimit());
        }
        limitRepository.save(limit);
        log.info("Limit of card was successfully updated, daily limit: {}, monthly limit: {}",
                limit.getDailyLimit(), limit.getMonthlyLimit());
        return limitMapper.toDto(limit);
    }

    @Override
    public Page<LimitDto> findAll(PageRequest pageRequest) {
        return limitRepository.findAll(pageRequest)
                .map(limitMapper::toDto);
    }

    @Override
    public LimitDto findByCardId(UUID id) {
        return limitRepository.findByCardId(id)
                .map(limitMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Limit with card id {} wasn't founded", id);
                    throw new CardNotFoundException(String.format("Limit with card id %s wasn't found", id));
                });
    }

}
