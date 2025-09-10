package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expo_collaborator_list")
public class ExpoCollaboratorList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "collaborator")  // 被控方加mappingBy
    @JsonIgnore
    private Expo expo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "expo_collaborator_list_user",  // 中間關聯表名稱
            joinColumns = {@JoinColumn(name = "listID")},  //this table's foreigner key in database
            inverseJoinColumns = {@JoinColumn(name = "userAccount")}  //User table's foreigner key in database
    )
    @JsonIgnore
    private Set<User> collaborators = new HashSet<>();
}
