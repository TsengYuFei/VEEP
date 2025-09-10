package com.example.api.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @Column(name = "mail", nullable = false, unique = true)
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
    private Boolean showFollowers;

    @Column(name = "showFollowing", nullable = false)
    private Boolean showFollowing;

    @Column(name = "showHistory", nullable = false)
    private Boolean showHistory;

    @Column(name = "showCurrentExpo", nullable = false)
    private Boolean showCurrentExpo;

    @Column(name = "showCurrentBooth", nullable = false)
    private Boolean showCurrentBooth;

    @Column(name = "isVerified", nullable = false)
    private Boolean isVerified;

    @Column(name = "verificationCode")
    private String verificationCode;

    @Column(name = "verifyDateline")
    private LocalDateTime verifyDateline;

    @Column(name = "resetPasswordCode")
    private String resetPasswordCode;

    @Column(name = "resetPasswordDateline")
    private LocalDateTime resetPasswordDateline;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Booth> boothList = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Expo> expoList = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        if(showFollowers == null) showFollowers = true;
        if(showFollowing == null) showFollowing = true;
        if(showHistory == null) showHistory = false;
        if(showCurrentExpo == null) showCurrentExpo = true;
        if(showCurrentBooth == null) showCurrentBooth = true;
        if(isVerified == null) isVerified = false;
    }

}
