package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`user`")
public class User{

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Id
    @Column(name = "userAccount", nullable = false, length = 20, unique = true)
    private String userAccount;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "tel", nullable = false, length = 10)
    private String tel;

    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Lob
    @Column(name = "bio")
    private String bio;

    @Column(name = "background")
    private String background;

    @Column(name = "showFollowers", nullable = false)
    private Boolean showFollowers = true;

    @Column(name = "showFollowing", nullable = false)
    private Boolean showFollowing = true;

    @Column(name = "showHistory", nullable = false)
    private Boolean showHistory = false;

    @Column(name = "showCurrentExpo", nullable = false)
    private Boolean showCurrentExpo = true;

    @Column(name = "showCurrentBooth", nullable = false)
    private Boolean showCurrentBooth = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.GENERAL;

    @ManyToMany(mappedBy = "collaborators")
    private Set<CollaboratorList> collaboratorLists = new HashSet<>();
}
