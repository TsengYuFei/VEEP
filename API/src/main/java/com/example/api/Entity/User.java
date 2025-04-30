package com.example.api.Entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class User {
    private String name;
    private String userAccount;
    private String password;
    private String tel;
    private String mail;
    private String avatar;
    private LocalDate birthday;
    private String bio;
    private String background;
    private Integer followers;
    private Integer following;
    private boolean showFollowers;
    private boolean showFollowing;
    private boolean showHistory;
    private boolean showCurrentExpo;
    private boolean showCurrentBooth;
    private Role role;
}
