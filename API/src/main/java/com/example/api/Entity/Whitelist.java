package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "whitelist")
public class Whitelist {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "whitelist")
    @JsonIgnore
    private Expo expo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "whitelist_user",
            joinColumns = {@JoinColumn(name = "listID")},
            inverseJoinColumns = {@JoinColumn(name = "userAccount")}
    )
    @JsonIgnore
    private Set<User> whitelistedUsers = new HashSet<>();
}
