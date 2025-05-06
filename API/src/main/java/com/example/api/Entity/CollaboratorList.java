package com.example.api.Entity;

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
@Table(name = "collaborator_list")
public class CollaboratorList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "collaborator")
    private Booth booth;

    @ManyToMany
    @JoinTable(
            name = "collaborator_list_user",
            joinColumns = {@JoinColumn(name = "listID")},  //this table's foreigner key
            inverseJoinColumns = {@JoinColumn(name = "userAccount")}  //User table's foreigner key
    )
    private Set<User> collaborators = new HashSet<>();
}
