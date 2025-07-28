package com.example.api.Service;

import com.example.api.DTO.Request.ChangePasswordRequest;
import com.example.api.DTO.Request.ResetPasswordRequest;
import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Entity.Role;
import com.example.api.Entity.User;
import com.example.api.Entity.UserRole;
import com.example.api.Exception.*;
import com.example.api.Repository.UserRepository;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final RoleService roleService;

    @Autowired
    private final UserRoleService userRoleService;

    @Autowired
    private final EmailService emailService;



    User getUserByAccount(String account){
        System.out.println("SingleUserService: getUserByAccount >> "+account);
        return userRepository.findById(account)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號為 < "+ account+" > 的使用者"));
    }


    User getUserByAccountOrMail(String input){
        System.out.println("SingleUserService: getUserByAccountOrMail >> "+input);
        Optional<User> byAccount = userRepository.findById(input);
        Optional<User> byMail = userRepository.findByMail(input);
        return byAccount.or(() -> byMail)
                .orElseThrow(() -> new NotFoundException("找不到使用者帳號或電子郵箱為 < "+ input+" > 的使用者"));
    }


    User getUserByRestPasswordToken(String token){
        System.out.println("SingleUserService: getUserByRestPasswordToken >> "+token);
        return userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new NotFoundException("找不到reset password token為 < "+ token+" > 的使用者"));
    }


    List<User> getAllUserByAccount(List<String> accounts){
        return userRepository.findAllById(accounts);
    }


    public boolean isNotCurrentUser(String account){
        System.out.println("SingleUserService: isCurrentUser >> "+account);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccount = authentication.getName();
        return !account.equals(currentAccount);
    }


    public boolean isShowCurrentExpo(String account){
        System.out.println("SingleUserService: isShowCurrentExpo >> "+account);
        User user = getUserByAccount(account);
        return user.getShowCurrentExpo();
    }


    public boolean isShowCurrentBooth(String account){
        System.out.println("SingleUserService: isShowCurrentBooth >> "+account);
        User user = getUserByAccount(account);
        return user.getShowCurrentBooth();
    }


    public String getUserRoleName(String account){
        System.out.println("SingleUserService: isShowCurrentBooth >> "+account);
        User user = getUserByAccount(account);
        UserRole userRole = userRoleService.getUserRoleByUser(user);
        return userRole.getRole().getName();
    }


    public UserDetailResponse getUserDetailByAccount(String account){
        System.out.println("SingleUserService: getUserByAccount >> "+account);
        User user = getUserByAccount(account);

        UserDetailResponse response =  UserDetailResponse.fromUser(user);
        UserRole userRole = userRoleService.getUserRoleByUser(user);
        String roleName = userRole.getRole().getName();
        response.setRoleName(roleName);

        return response;
    }


    public UserOverviewResponse getUserOverviewByAccount(String account){
        System.out.println("SingleUserService: getUserOverviewByAccount >> "+account);
        User user = getUserByAccount(account);

        UserOverviewResponse response =  UserOverviewResponse.fromUser(user);
        String roleName = getUserRoleName(account);
        response.setRoleName(roleName);

        return response;
    }


    public UserEditResponse getUserEditByAccount(String account){
        System.out.println("SingleUserService: getUserEditByAccount >> "+account);
        User user = getUserByAccount(account);

        UserEditResponse response =  UserEditResponse.fromUser(user);
        String roleName = getUserRoleName(account);
        response.setRoleName(roleName);

        return response;
    }


    @Transactional
    public String createUser(UserCreateRequest request){
        System.out.println("SingleUserService: createUser");

        User user = null;
        try {
            user = getUserByAccountOrMail(request.getUserAccount());
        }catch (NotFoundException ignored){}
        if(user != null) throw new UserAlreadyExistsException("已存在使用者帳號為 < "+ request.getUserAccount()+" > 的使用者");

        try {
            user = getUserByAccountOrMail(request.getMail());
        }catch (NotFoundException ignored){}
        if(user != null) throw new UserAlreadyExistsException("已存在電子郵箱為< "+request.getMail()+" >的使用者");

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Integer randomCode = (int)(Math.random() * 1000000);

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUserAccount(request.getUserAccount());
        newUser.setPassword(encodedPassword);
        newUser.setTel(request.getTel());
        newUser.setMail(request.getMail());
        newUser.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        newUser.setBirthday(request.getBirthday());
        newUser.setResetPasswordToken(null);
        newUser.setVerificationCode(randomCode);
        newUser.setIsVerified(false);

        userRepository.save(newUser);

        // 設定初始role
        Role generalRole = roleService.getRoleByName("GENERAL");
        UserRole userRole = new UserRole();
        userRole.setRole(generalRole);
        userRole.setUser(newUser);

        userRoleService.saveUserRole(userRole);

        emailService.sendVerificationEmail(newUser.getMail(), randomCode, newUser.getName());
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
        UserRole userRole = userRoleService.getUserRoleByUser(user);

        String roleName = userRole.getRole().getName();
        String newRoleName = roleName.equals("GENERAL")? "FOUNDER": "GENERAL";
        Role newRole = roleService.getRoleByName(newRoleName);

        userRole.setRole(newRole);
        userRoleService.saveUserRole(userRole);
    }


    public void verifyUser(String account, Integer code){
        System.out.println("SingleUserService: verifyUser >> "+account+", "+code);
        User user = getUserByAccount(account);
        if(user.getIsVerified()) throw new ConflictException("使用者帳號 < "+account+" > 已完成信箱驗證");
        else if(user.getVerificationCode().equals(code)) {
            user.setVerificationCode(null);
            user.setIsVerified(true);
            userRepository.save(user);
        }else throw new BadRequestException("XX 驗證碼錯誤 XX");
    }


    public void resetPassword(ResetPasswordRequest request){
        System.out.println("SingleUserService: resetPassword");
        String token = request.getToken();
        String newPassword = request.getPassword();

        User user = getUserByRestPasswordToken(token);
        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new BadRequestException("The new password cannot be the same as the old password.");

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }


    public void changePassword(String account, ChangePasswordRequest request){
        System.out.println("SingleUserService: changePassword >> "+account);
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        User user = getUserByAccount(account);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) throw new UnauthorizedException("XX Wrong password XX");
        if (oldPassword.equals(newPassword)) throw new BadRequestException("The new password cannot be the same as the old password.");

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
