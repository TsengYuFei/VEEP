package com.example.api.Service;

import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepositoryInterface;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceNew {
    @Autowired
    private final UserRepositoryInterface userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public UserServiceNew(UserRepositoryInterface userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    public UserDetailResponse getUserByAccount(String account){
        System.out.println("UserServiceNew: getUserByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return modelMapper.map(user, UserDetailResponse.class);
    }


    public String createUser(UserCreateRequest request){
        System.out.println("UserServiceNew: createUser");
        String account = request.getUserAccount();
        UserDetailResponse user = null;
        try {
            user = getUserByAccount(account);
        }catch (NotFoundException ignored){}
        if(user != null) throw new ClosedOnExpiredPasswordException("已存在使用者帳號為 < "+ account+" > 的使用者");

        User newUser = modelMapper.map(request, User.class);

        userRepository.save(newUser);
        return newUser.getUserAccount();
    }
}
