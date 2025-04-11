package com.example.testtaskeffectmobile.model;

import com.example.testtaskeffectmobile.model.enums.CardStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "card")
@Data
public class Card {

    @Id
    private UUID id;

    @Column(name = "number", nullable = false)
    @NotBlank(message = "Card number must not be blank")
    private String number;

    @Column(name = "balance", nullable = false)
    @NotNull(message = "Balance must not be null")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "Status must not be null")
    private CardStatus status;

    @Column(name = "expiration_date", nullable = false)
    @NotNull(message = "Expiration date must not be null")
    private LocalDate expirationDate;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "card")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "card")
    private Limit limit;
}

