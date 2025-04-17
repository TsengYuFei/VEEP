package com.example.api.Service;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.DTO.UserOverviewDTO;
import com.example.api.Dao.UserDao;
import com.example.api.Exception.ConflictException;
import com.example.api.Exception.NotFoundException;
import com.example.api.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public UserDetailDTO getUserDetailByAccount(String account){
        System.out.println("UserService: getUserDetailByAccount >> "+account);
        UserDetailDTO user = userDao.getUserDetailByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account <"+account+">");
        return user;
    }


    public UserOverviewDTO getUserOverviewByAccount(String account){
        System.out.println("UserService: getUserOverviewByAccount >> "+account);
        UserOverviewDTO user = userDao.getUserOverviewByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account <"+account+">");
        return user;
    }


    public String createUser(UserRequest userRequest){
        System.out.println("UserService: createUser");
        UserOverviewDTO user = userDao.getUserOverviewByAccount(userRequest.getUserAccount());
        if(user != null) throw new ConflictException("User account <"+user.getUserAccount()+"> is already exist.");
        return userDao.createUser(userRequest);
    }
}