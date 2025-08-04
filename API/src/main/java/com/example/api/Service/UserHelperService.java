package com.example.api.Service;

import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserHelperService {
    @Autowired
    private final UserRepository userRepository;



    User getUserByAccount(String account){
        System.out.println("UserHelperService: getUserByAccount >> "+account);
        return userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
    }


    User getUserByAccountOrMail(String input){
        System.out.println("UserHelperService: getUserByAccountOrMail >> "+input);
        Optional<User> byAccount = userRepository.findById(input);
        Optional<User> byMail = userRepository.findByMail(input);
        return byAccount.or(() -> byMail)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號或電子郵箱為 < "+ input+" > 的使用者"));
    }


    User getUserByRestPasswordToken(String token){
        System.out.println("UserHelperService: getUserByRestPasswordToken >> "+token);
        return userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new NotFoundException("找不到reset password token為 < "+ token+" > 的使用者"));
    }


    List<User> getAllUserByAccount(List<String> accounts){
        System.out.println("UserHelperService: getAllUserByAccount");
        return userRepository.findAllById(accounts);
    }
}
