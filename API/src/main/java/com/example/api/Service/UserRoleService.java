package com.example.api.Service;

import com.example.api.Entity.User;
import com.example.api.Entity.UserRole;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    @Autowired
    private final UserRoleRepository userRoleRepository;



    public UserRole getUserRoleByUser(User user) {
        System.out.println("UserRoleService: getUserRoleByUser");
        return userRoleRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 <"+user.getUserAccount()+" > 的角色身分"));
    }


    public void saveUserRole(UserRole userRole) {
        System.out.println("UserRoleService: saveUserRole");
        userRoleRepository.save(userRole);
    }
}
