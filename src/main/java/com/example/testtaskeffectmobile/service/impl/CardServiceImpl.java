package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.request.AddCardRequestDto;
import com.example.testtaskeffectmobile.exception.CardNotFoundException;
import com.example.testtaskeffectmobile.model.Card;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.repository.CardRepository;
import com.example.testtaskeffectmobile.service.CardService;
import com.example.testtaskeffectmobile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserService userService;

    @Override
    public void save(AddCardRequestDto addCardRequestDto) {
        User user = userService.findById(addCardRequestDto.getUserId());

        Card card = Card.builder()
                .user(user)
                .number(addCardRequestDto.getNumber())
                .build();

        cardRepository.save(card);
        log.info("Card with number {} was successfully saved", addCardRequestDto.getNumber());
    }

    @Override
    public void delete(UUID id) {
        if (!cardRepository.existsById(id)) {
            log.error("Card with id {} wasn't founded", id);
            throw new CardNotFoundException(String.format("Card with if %s wasn't found", id));
        }
        cardRepository.deleteById(id);
        log.info("Card with id {} was successfully deleted", id);
    }


}
