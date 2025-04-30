package com.example.api.Service;

import com.example.api.DTO.Response.UserEditResponse;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.DTO.Response.UserOverviewResponse;
import com.example.api.Repository.UserRepository;
import com.example.api.Exception.ConflictException;
import com.example.api.Exception.NotFoundException;
import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailResponse getUserDetailByAccount(String account){
        System.out.println("UserService: getUserDetailByAccount >> "+account);
        UserDetailResponse user = userRepository.getUserDetailByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account < "+account+" >");
        return user;
    }   


    public UserOverviewResponse getUserOverviewByAccount(String account){
        System.out.println("UserService: getUserOverviewByAccount >> "+account);
        UserOverviewResponse user = userRepository.getUserOverviewByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account < "+account+" >");
        return user;
    }


    public UserEditResponse getUserEditByAccount(String account){
        System.out.println("UserService: getUserEditByAccount >> "+account);
        UserEditResponse user = userRepository.getUserEditByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account < "+account+" >");
        return user;
    }


    public String createUser(UserCreateRequest request){
        System.out.println("UserService: createUser");
        UserOverviewResponse user = userRepository.getUserOverviewByAccount(request.getUserAccount());
        if(user != null) throw new ConflictException("User account < "+user.getUserAccount()+" > is already exist.");

        if(request.getAvatar().isBlank()) request.setAvatar(null);
        return userRepository.createUser(request);
    }


    public void updateUserByAccount(String account, UserUpdateRequest request){
        System.out.println("UserService: updateUserByAccount >> "+account);
        UserDetailResponse user = userRepository.getUserDetailByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account < "+account+" >");

        String avatar = request.getAvatar();
        String background = request.getBackground();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);
        if(background != null && background.isBlank()) request.setBackground(null);
        userRepository.updateUserByAccount(account, request);
    }


    public void deleteUserByAccount(String account){
        System.out.println("UserService: deleteUserByAccount >> "+account);
        UserDetailResponse user = userRepository.getUserDetailByAccount(account);
        if(user == null) throw new NotFoundException("Can't find a user with the user account < "+account+" >");
        userRepository.deleteUserByAccount(account);
    }
}