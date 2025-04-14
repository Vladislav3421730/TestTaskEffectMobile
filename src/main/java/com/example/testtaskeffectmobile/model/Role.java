package com.example.testtaskeffectmobile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "name must be not null")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
