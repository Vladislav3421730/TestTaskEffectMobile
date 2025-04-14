package com.example.testtaskeffectmobile.repository;

import com.example.testtaskeffectmobile.model.Transaction;
import com.example.testtaskeffectmobile.model.enums.OperationResult;
import com.example.testtaskeffectmobile.model.enums.OperationType;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryCustomImpl implements CustomTransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Transaction> findByFilters(BigDecimal minAmount, BigDecimal maxAmount, OperationType operation, OperationResult operationResult, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);

        List<Predicate> predicates = formPredicates(cb, root, minAmount, maxAmount, operation, operationResult);

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Transaction> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        long total = getTotalAmountByFilter(minAmount, maxAmount, operation, operationResult);

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    private int getTotalAmountByFilter(BigDecimal minAmount, BigDecimal maxAmount, OperationType operation, OperationResult operationResult) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Transaction> root = query.from(Transaction.class);

        List<Predicate> predicates = formPredicates(cb, root, minAmount, maxAmount, operation, operationResult);

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.select(cb.count(root));
        return entityManager.createQuery(query)
                .getSingleResult()
                .intValue();
    }

    private List<Predicate> formPredicates(CriteriaBuilder cb, Root<Transaction> root, BigDecimal minAmount, BigDecimal maxAmount, OperationType operation, OperationResult operationResult) {
        List<Predicate> predicates = new ArrayList<>();

        if (minAmount != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
        }
        if (maxAmount != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
        }
        if (operation != null) {
            predicates.add(cb.equal(root.get("operation"), operation));
        }
        if (operationResult != null) {
            predicates.add(cb.equal(root.get("operationResult"), operationResult));
        }
        return predicates;
    }
}
