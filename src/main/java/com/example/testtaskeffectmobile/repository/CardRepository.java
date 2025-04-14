package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Boolean existsCardByNumber(String number);

    Optional<Card> findCardByUserEmailAndNumber(String email, String number);
    Optional<Card> findCardByUserEmailAndId(String email, UUID id);

    Page<Card> findAllByUserEmail(String email, PageRequest pageRequest);

    @Query(value = """
            SELECT SUM(t.amount)
            FROM transaction t
            WHERE t.operation = 'WITHDRAWAL' AND t.operation_result = 'SUCCESSFULLY'
             AND DATE(t.timestamp) = CURRENT_DATE AND t.card_id = :cardId""",
            nativeQuery = true)
    Optional<BigDecimal> findTodayWithdrawalsByCardId(@Param("cardId") UUID cardId);

    @Query(value = """
            SELECT SUM(t.amount)
            FROM transaction t
            WHERE t.operation = 'WITHDRAWAL' AND t.operation_result = 'SUCCESSFULLY'
            AND EXTRACT(MONTH FROM t.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE)
            AND EXTRACT(YEAR FROM t.timestamp) = EXTRACT(YEAR FROM CURRENT_DATE)
            AND t.card_id = :cardId""",
            nativeQuery = true)
    Optional<BigDecimal> findTotalWithdrawalsForCurrentMonthByCardId(@Param("cardId") UUID cardId);


}
