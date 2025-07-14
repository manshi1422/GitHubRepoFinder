package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "repositories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryEntity {

    @Id
    private Long id;

    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;

    @Column(name = "last_updated")
    private Instant lastUpdated;
}

