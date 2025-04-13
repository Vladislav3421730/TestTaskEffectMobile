package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Boolean existsCardByNumber(String number);

    Optional<Card> findCardByUserEmailAndNumber(String email, String number);

    Page<Card> findAllByUserEmail(String email, PageRequest pageRequest);
}
