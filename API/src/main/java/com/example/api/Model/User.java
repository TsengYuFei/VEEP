package com.example.api.Model;


import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class User {
    private String name;
    private String userAccount;
    private String password;
    private String tel;
    private String mail;
    private String avatar;
    private Date birthday;
    private String bio;
    private String background;
    private Integer followers;
    private Integer following;
    private Integer[] expo;
    private String[] booth;
    private boolean showFollowers;
    private boolean showFollowing;
    private boolean showHistory;
    private boolean showCurrentExpo;
    private boolean showCurrentBooth;
    private Role role;
}
