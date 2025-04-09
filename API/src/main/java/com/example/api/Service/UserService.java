package com.example.api.Service;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.Dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public UserDetailDTO getDetailUserByAccount(String account){
        System.out.println("UserService: getDetailUserByAccount >> "+account);
        return userDao.getDetailUserByAccount(account);
    }
}