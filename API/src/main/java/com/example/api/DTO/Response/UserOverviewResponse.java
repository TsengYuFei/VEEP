package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import com.example.api.Entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserOverviewResponse {
    private String name;
    private String userAccount;
    private String avatar;
    private Role role;

    public static UserOverviewResponse fromUser(User user) {
        UserOverviewResponse response = new UserOverviewResponse();

        response.name = user.getName();
        response.userAccount = user.getUserAccount();
        response.avatar = user.getAvatar();
        response.role = user.getRole();

        return response;
    }
}
