package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.BlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BlockRequestRepository extends JpaRepository<BlockRequest, UUID> {

    List<BlockRequest> findAllByUserId(UUID id);
    List<BlockRequest> findAllByUserEmail(String email);
}
