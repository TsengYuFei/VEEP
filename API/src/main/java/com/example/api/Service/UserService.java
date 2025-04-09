package com.example.api.Service;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.DTO.UserOverviewDTO;
import com.example.api.Dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public UserDetailDTO getUserDetailByAccount(String account){
        System.out.println("UserService: getUserDetailByAccount >> "+account);
        return userDao.getUserDetailByAccount(account);
    }

    public UserOverviewDTO getUserOverviewByAccount(String account){
        System.out.println("UserService: getUserOverviewByAccount >> "+account);
        return userDao.getUserOverviewByAccount(account);
    }
}