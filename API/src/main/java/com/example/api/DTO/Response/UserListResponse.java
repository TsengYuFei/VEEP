package com.example.api.DTO.Response;

import com.example.api.Entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserListResponse {
    private String name;
    private String userAccount;

    public static UserListResponse fromUser(User user) {
        UserListResponse response = new UserListResponse();
        response.setName(user.getName());
        response.setUserAccount(user.getUserAccount());
        return response;
    }
}
