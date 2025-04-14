package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Card;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CardRepositoryCustomImpl implements CustomCardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Card> findByFilters(BigDecimal minBalance, BigDecimal maxBalance, LocalDate expiredBefore, LocalDate expiredAfter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Card> query = cb.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);

        List<Predicate> predicates = formPredicates(cb, root, minBalance, maxBalance, expiredBefore, expiredAfter);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<Card> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        long total = getTotalAmountByFilter(minBalance, maxBalance, expiredBefore, expiredAfter);

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    private int getTotalAmountByFilter(BigDecimal minBalance, BigDecimal maxBalance, LocalDate expiredBefore, LocalDate expiredAfter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Card> root = query.from(Card.class);

        List<Predicate> predicates = formPredicates(cb, root, minBalance, maxBalance, expiredBefore, expiredAfter);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.select(cb.count(root));
        return entityManager.createQuery(query)
                .getSingleResult()
                .intValue();
    }

    private List<Predicate> formPredicates(CriteriaBuilder cb, Root<Card> root ,BigDecimal minBalance, BigDecimal maxBalance, LocalDate expiredBefore, LocalDate expiredAfter) {
        List<Predicate> predicates = new ArrayList<>();

        if (minBalance != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), minBalance));
        }
        if (maxBalance != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("balance"), maxBalance));
        }
        if (expiredBefore != null) {
            predicates.add(cb.lessThan(root.get("expirationDate"), expiredBefore));
        }
        if (expiredAfter != null) {
            predicates.add(cb.greaterThan(root.get("expirationDate"), expiredAfter));
        }
        return predicates;
    }
}
