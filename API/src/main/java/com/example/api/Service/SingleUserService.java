package com.example.api.Service;

import com.example.api.DTO.Request.LoginRequest;
import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Entity.Role;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnauthorizedException;
import com.example.api.Other.JwtUtil;
import com.example.api.Repository.UserRepository;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class SingleUserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;



    User getUserByAccount(String account){
        System.out.println("SingleUserService: getUserByAccount >> "+account);
        return userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
    }


    List<User> getAllUserByAccount(List<String> accounts){
        return userRepository.findAllById(accounts);
    }


    public UserDetailResponse getUserDetailByAccount(String account){
        System.out.println("SingleUserService: getUserByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return UserDetailResponse.fromUser(user);
    }


    public UserOverviewResponse getUserOverviewByAccount(String account){
        System.out.println("SingleUserService: getUserOverviewByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return UserOverviewResponse.fromUser(user);
    }


    public UserEditResponse getUserEditByAccount(String account){
        System.out.println("SingleUserService: getUserEditByAccount >> "+account);
        User user = userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
        return UserEditResponse.fromUser(user);
    }


    @Transactional
    public String createUser(UserCreateRequest request){
        System.out.println("SingleUserService: createUser");

        UserOverviewResponse user = null;
        try {
            user = getUserOverviewByAccount(request.getUserAccount());
        }catch (NotFoundException ignored){}
        if(user != null) throw new ClosedOnExpiredPasswordException("已存在使用者帳號為 < "+ request.getUserAccount()+" > 的使用者");

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUserAccount(request.getUserAccount());
        newUser.setPassword(encodedPassword);
        newUser.setTel(request.getTel());
        newUser.setMail(request.getMail());
        newUser.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        newUser.setBirthday(request.getBirthday());
        newUser.setRole(request.getRole());

        userRepository.save(newUser);
        return newUser.getUserAccount();
    }


    @Transactional
    public void updateUserByAccount(String account, UserUpdateRequest request){
        System.out.println("SingleUserService: updateUserByAccount >> "+account);
        User user = getUserByAccount(account);

        if(user.getAvatar() != null && request.getAvatar() != null){
            imageService.deleteImageByName(user.getAvatar());
        }

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


    @Transactional
    public void deleteUserByAccount(String account){
        System.out.println("SingleUserService: deleteUserByAccount >> "+account);
        User user = getUserByAccount(account);
        String image = user.getAvatar();
        if(image != null) imageService.deleteImageByName(image);
        userRepository.delete(user);
    }


    public List<ExpoOverviewResponse> getAllExpoOverview(String account){
        System.out.println("SingleUserService: getAllExpoOverview >> "+account);
        User user = getUserByAccount(account);

        return user.getExpoList().stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }


    public List<BoothOverviewResponse> getAllBoothOverview(String account){
        System.out.println("SingleUserService: getAllBoothOverview >> "+account);
        User user = getUserByAccount(account);

        return user.getBoothList().stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public void switchRole(String account){
        System.out.println("SingleUserService: switchRole >> "+account);
        User user = getUserByAccount(account);

        if(user.getRole() == Role.GENERAL) user.setRole(Role.FOUNDER);
        else user.setRole(Role.GENERAL);
    }


    public LoginResponse login(LoginRequest request) {
        System.out.println("SingleUserService: login");

        User user = userRepository.findByMail(request.getUserAccountOrMail());
        if(user == null) user = userRepository.findByUserAccount(request.getUserAccountOrMail());

        if(user == null) throw new NotFoundException("Can't find the user with email or user account "+request.getUserAccountOrMail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new UnauthorizedException("XX Wrong password XX");

        String token = jwtUtil.generateToken(user.getUserAccount());
        return new LoginResponse(user.getUserAccount(), token, Role.GENERAL);
    }
}
