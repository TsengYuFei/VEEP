package com.example.api.Service;

import com.example.api.Dao.UserDao;
import com.example.api.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getBriefUserByAccount(String account){
        System.out.println("UserService: getBriefUserByAccount >> "+account);
        return userDao.getBriefUserByAccount(account);
    }
}