package com.example.api.Controller;

import com.example.api.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user/{userAccount}/general")
    public ResponseEntity<User> getGeneralUser(@PathVariable String userAccount) {
        User user = UserService.getUserById(userAccount);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }
}
