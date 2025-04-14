package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LimitRepository extends JpaRepository<Limit, UUID> {

    Optional<Limit> findByCardId(UUID id);
}
