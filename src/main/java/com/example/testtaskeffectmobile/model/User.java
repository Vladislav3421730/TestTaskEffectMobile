package com.example.testtaskeffectmobile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @Column(name = "last_name",nullable = false)
    @NotBlank(message = "lastName must be not blank")
    private String lastName;

    @Column(name = "email", unique = true)
    @Email(message = "Email must contain '@'")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "password must be not blank")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Card> cards = new ArrayList<>();
}
