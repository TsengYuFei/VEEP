package com.example.api.DTO.Response;

import com.example.api.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOverviewResponse {
    private String name;
    private String userAccount;
    private String avatar;

    public static UserOverviewResponse fromUser(User user) {
        UserOverviewResponse response = new UserOverviewResponse();

        response.name = user.getName();
        response.userAccount = user.getUserAccount();
        response.avatar = user.getAvatar();

        return response;
    }
}
