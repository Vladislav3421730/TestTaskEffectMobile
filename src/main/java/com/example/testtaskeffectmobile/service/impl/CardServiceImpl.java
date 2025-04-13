package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.convertor.CardNumberCryptoConverter;
import com.example.testtaskeffectmobile.dto.CardDto;
import com.example.testtaskeffectmobile.dto.request.AddCardRequestDto;
import com.example.testtaskeffectmobile.dto.request.StatusCardRequestDto;
import com.example.testtaskeffectmobile.exception.CardAlreadyExistException;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.exception.UserNotFoundException;
import com.example.testtaskeffectmobile.mapper.CardMapper;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.Limit;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.model.enums.CardStatus;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.repository.LimitRepository;
import com.example.testtaskeffectmobile.repository.UserRepository;
import com.example.testtaskeffectmobile.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final LimitRepository limitRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public void save(AddCardRequestDto addCardRequestDto) {
        User user = userRepository.findById(addCardRequestDto.getUserId()).orElseThrow(() -> {
            log.error("User with id {} wasn't found", addCardRequestDto.getUserId());
            throw new UserNotFoundException(String.format("User with id %s wasn't found", addCardRequestDto.getUserId()));
        });

        if(cardRepository.existsCardByNumber(addCardRequestDto.getNumber())) {
            log.error("Card with number {} already exist", addCardRequestDto.getNumber());
            throw new CardAlreadyExistException(String.format("Card with number %s already exist", addCardRequestDto.getNumber()));
        }

        Card card = Card.builder()
                .user(user)
                .number(addCardRequestDto.getNumber())
                .build();
        Limit limit = new Limit(card);

        cardRepository.save(card);
        log.info("Card with number {} was successfully saved", addCardRequestDto.getNumber());
        limitRepository.save(limit);
        log.info("Limit for card {} was successfully saved", card.getId());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!cardRepository.existsById(id)) {
            log.error("Card with id {} wasn't founded", id);
            throw new CardNotFoundException(String.format("Card with if %s wasn't found", id));
        }
        cardRepository.deleteById(id);
        log.info("Card with id {} was successfully deleted", id);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, StatusCardRequestDto status) {
        Card card = cardRepository.findById(id).orElseThrow(() -> {
            log.error("Card with id {} wasn't founded", id);
            throw new CardNotFoundException(String.format("Card with if %s wasn't found", id));
        });
        card.setStatus(CardStatus.valueOf(status.getStatus()));
        cardRepository.save(card);
        log.info("Card with id {} was successfully updated with status {}", card.getId(), status.getStatus());
    }

    @Override
    public Page<CardDto> findAll(PageRequest pageRequest) {
        log.info("Get all users page: {}, pageSize: {}", pageRequest.getPageNumber(), pageRequest.getPageSize());
        return cardRepository.findAll(pageRequest)
                .map(cardMapper::toDto);
    }

    @Override
    public Page<CardDto> findAllByUserEmail(String email, PageRequest pageRequest) {
        log.info("Trying get all user's cards with email {}", email);
        return cardRepository.findAllByUserEmail(email, pageRequest)
                .map(cardMapper::toDto);
    }

    @Override
    public CardDto findById(UUID id) {
        return cardRepository.findById(id)
                .map(cardMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Card with id {} wasn't founded", id);
                    throw new CardNotFoundException(String.format("Card with if %s wasn't found", id));
                });
    }


}
