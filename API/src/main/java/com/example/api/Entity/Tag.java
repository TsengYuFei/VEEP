package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tag")
public class Tag {

    public Tag(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "tagID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Expo> expos = new HashSet<>();
}
