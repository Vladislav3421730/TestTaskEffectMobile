package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {



}
