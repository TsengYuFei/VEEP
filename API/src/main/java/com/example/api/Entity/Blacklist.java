package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "blacklist")
public class Blacklist {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "blacklist")
    @JsonBackReference
    private Expo expo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "blacklist_user",
            joinColumns = {@JoinColumn(name = "listID")},
            inverseJoinColumns = {@JoinColumn(name = "userAccount")}
    )
    @JsonIgnore
    private Set<User> blacklistedUsers = new HashSet<>();
}
