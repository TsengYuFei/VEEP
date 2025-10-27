package com.example.api.DTO.Response;

import com.example.api.Entity.User;
import lombok.Data;

@Data
public class UserListResponse {
    private String name;
    private String userAccount;
    private String avatar;

    public static UserListResponse fromUser(User user) {
        UserListResponse response = new UserListResponse();
        response.setName(user.getName());
        response.setUserAccount(user.getUserAccount());
        response.setAvatar(user.getAvatar());
        return response;
    }
}
