package com.example.api.Service;

import com.example.api.Entity.UserRole;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserHelperService userHelperService;



    public UserRole getUserRoleByAccount(String account) {
        System.out.println("UserRoleService: getUserRoleByUser");
        return userRoleRepository.findUserRoleByUser_UserAccount(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+account+" > 的角色身分"));
    }

    public String getUserRoleName(String account){
        System.out.println("UserRoleService: getUserRoleName >> "+account);
        userHelperService.getUserByAccount(account);
        UserRole userRole = getUserRoleByAccount(account);
        return userRole.getRole().getName();
    }


    public void saveUserRole(UserRole userRole) {
        System.out.println("UserRoleService: saveUserRole");
        userRoleRepository.save(userRole);
    }


    public void deleteUserRoleByAccount(String account) {
        System.out.println("UserRoleService: deleteUserRoleByAccount >> "+account);
        UserRole userRole = getUserRoleByAccount(account);
        userRoleRepository.delete(userRole);
    }
}
