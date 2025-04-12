package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
