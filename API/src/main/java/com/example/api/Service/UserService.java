package com.example.api.Service;

import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.DTO.Response.UserEditResponse;
import com.example.api.DTO.Response.UserOverviewResponse;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepository;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }



    public UserDetailResponse getUserDetailByAccount(String account){
        System.out.println("UserService: getUserByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return modelMapper.map(user, UserDetailResponse.class);
    }


    public UserOverviewResponse getUserOverviewByAccount(String account){
        System.out.println("UserService: getUserOverviewByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return modelMapper.map(user, UserOverviewResponse.class);
    }


    public UserEditResponse getUserEditByAccount(String account){
        System.out.println("UserService: getUserEditByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return modelMapper.map(user, UserEditResponse.class);
    }


    private User getUserByAccount(String account){
        System.out.println("UserService: getUserByAccount >> "+account);
        return userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
    }


    public String createUser(UserCreateRequest request){
        System.out.println("UserService: createUser");
        request.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        UserOverviewResponse user = null;

        try {
            user = getUserOverviewByAccount(request.getUserAccount());
        }catch (NotFoundException ignored){}
        if(user != null) throw new ClosedOnExpiredPasswordException("已存在使用者帳號為 < "+ request.getUserAccount()+" > 的使用者");


        User newUser = modelMapper.map(request, User.class);
        userRepository.save(newUser);

        return newUser.getUserAccount();
    }


    public void updateUserByAccount(String account, UserUpdateRequest request){
        System.out.println("UserService: updateUserByAccount >> "+account);
        User user = getUserByAccount(account);

        user.setName(updateIfNotBlank(user.getName(), request.getName()));
        user.setTel(updateIfNotBlank(user.getTel(), request.getTel()));
        user.setMail(updateIfNotBlank(user.getMail(), request.getMail()));
        user.setAvatar(updateIfNotBlank(user.getAvatar(), request.getAvatar()));
        user.setBirthday(updateIfNotNull(user.getBirthday(), request.getBirthday()));
        user.setBio(updateIfNotBlank(user.getBio(), request.getBio()));
        user.setBackground(updateIfNotBlank(user.getBackground(), request.getBackground()));
        user.setShowFollowers(updateIfNotNull(user.getShowFollowers(), request.getShowFollowers()));
        user.setShowFollowing(updateIfNotNull(user.getShowFollowing(), request.getShowFollowers()));
        user.setShowHistory(updateIfNotNull(user.getShowHistory(), request.getShowHistory()));
        user.setShowCurrentExpo(updateIfNotNull(user.getShowCurrentExpo(), request.getShowCurrentExpo()));
        user.setShowCurrentBooth(updateIfNotNull(user.getShowCurrentBooth(), request.getShowCurrentBooth()));

        userRepository.save(user);
    }


    public void deleteUserByAccount(String account){
        System.out.println("UserService: deleteUserByAccount >> "+account);
        User user = getUserByAccount(account);
        userRepository.delete(user);
    }
}
