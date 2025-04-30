package com.example.api.Service;

import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepositoryInterface;
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
}
