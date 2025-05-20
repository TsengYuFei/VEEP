package com.example.api.DTO;

import com.example.api.Model.Role;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class UserAllInformationDTO {
    private String name;
    private String tel;
    private String mail;
    private String avatar;
    private LocalDate birthday;
    private String bio;
    private String background;
    private boolean showFollowers;
    private boolean showFollowing;
    private boolean showHistory;
    private boolean showCurrentExpo;
    private boolean showCurrentBooth;
    private Role role;
}
