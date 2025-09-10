package com.example.api.Service;

import com.example.api.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MultipleUserService {

    public List<String> getUsersAccount(List<User> users){
        System.out.println("SingleExpoController: getUsersAccount");
        List<String> usersAccount = new ArrayList<>();
        for(User user : users){
            usersAccount.add(user.getUserAccount());
        }
        return usersAccount;
    }
}
