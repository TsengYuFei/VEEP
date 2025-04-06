package com.example.api.Controller;

import com.example.api.Model.User;
import com.example.api.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("Hello World!");
        return "Hello World!";
    }

    @GetMapping("/user/{userAccount}/general")
    public ResponseEntity<User> getGeneralUser(@PathVariable String userAccount) {
        System.out.println("UserController: getGeneralUser >> "+userAccount);
        User user = userService.getBriefUserByAccount(userAccount);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }
}
