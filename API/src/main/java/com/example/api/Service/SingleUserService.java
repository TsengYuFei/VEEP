package com.example.api.Service;

import com.example.api.DTO.Request.ChangePasswordRequest;
import com.example.api.DTO.Request.ResetPasswordRequest;
import com.example.api.DTO.Request.UserCreateRequest;
import com.example.api.DTO.Request.UserUpdateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Entity.*;
import com.example.api.Exception.*;
import com.example.api.Other.JwtUtil;
import com.example.api.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.api.Other.GenerateCodeTool.generateRandomCode;
import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class SingleUserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final EmailService emailService;
    private final UserHelperService userHelperService;
    private final SingleExpoService singleExpoService;
    private final SingleBoothService singleBoothService;



    public UserDetailResponse getUserDetailByAccount(String account){
        System.out.println("SingleUserService: getUserByAccount >> "+account);
        User user = userHelperService.getUserByAccount(account);

        UserDetailResponse response =  UserDetailResponse.fromUser(user);
        UserRole userRole = userRoleService.getUserRoleByAccount(account);
        String roleName = userRole.getRole().getName();
        response.setRoleName(roleName);

        return response;
    }


    public UserOverviewResponse getUserOverviewByAccount(String account){
        System.out.println("SingleUserService: getUserOverviewByAccount >> "+account);
        User user = userHelperService.getUserByAccount(account);

        UserOverviewResponse response =  UserOverviewResponse.fromUser(user);
        String roleName = userRoleService.getUserRoleName(account);
        response.setRoleName(roleName);

        return response;
    }


    public UserEditResponse getUserEditByAccount(String account){
        System.out.println("SingleUserService: getUserEditByAccount >> "+account);
        User user = userHelperService.getUserByAccount(account);

        UserEditResponse response =  UserEditResponse.fromUser(user);
        String roleName = userRoleService.getUserRoleName(account);
        response.setRoleName(roleName);

        return response;
    }


    @Transactional
    public String createUser(UserCreateRequest request){
        System.out.println("SingleUserService: createUser");

        User user = null;
        try {
            user = userHelperService.getUserByAccountOrMail(request.getUserAccount());
        }catch (NotFoundException ignored){}
        if(user != null) throw new UserAlreadyExistsException("已存在使用者帳號為 < "+ request.getUserAccount()+" > 的使用者");

        try {
            user = userHelperService.getUserByAccountOrMail(request.getMail());
        }catch (NotFoundException ignored){}
        if(user != null) throw new UserAlreadyExistsException("已存在電子郵箱為< "+request.getMail()+" >的使用者");

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String verifyCode = generateRandomCode(6);
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(JwtUtil.VERIFY_CODE_VALIDITY_MINUTES);

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUserAccount(request.getUserAccount());
        newUser.setPassword(encodedPassword);
        newUser.setTel(request.getTel());
        newUser.setMail(request.getMail());
        newUser.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        newUser.setBirthday(request.getBirthday());
        newUser.setIsVerified(false);
        newUser.setVerificationCode(verifyCode);
        newUser.setVerifyDateline(deadline);
        newUser.setResetPasswordCode(null);
        newUser.setResetPasswordDateline(null);

        userRepository.save(newUser);

        // 設定初始role
        Role generalRole = roleService.getRoleByName("GENERAL");
        UserRole userRole = new UserRole();
        userRole.setRole(generalRole);
        userRole.setUser(newUser);

        userRoleService.saveUserRole(userRole);

        emailService.sendVerificationEmail(newUser.getMail(), verifyCode, newUser.getName());
        return newUser.getUserAccount();
    }


    @Transactional
    public void updateUserByAccount(String account, UserUpdateRequest request){
        System.out.println("SingleUserService: updateUserByAccount >> "+account);
        User user = userHelperService.getUserByAccount(account);

        if(user.getAvatar() != null && request.getAvatar() != null){
            imageService.deleteImageByName(user.getAvatar());
        }

        if(user.getBackground() != null && request.getBackground() != null){
            imageService.deleteImageByName(user.getBackground());
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
        User user = userHelperService.getUserByAccount(account);

        String image = user.getAvatar();
        if(image != null) imageService.deleteImageByName(image);
        image = user.getBackground();
        if(image != null) imageService.deleteImageByName(image);

        for(Expo expo:user.getExpoList()){
            singleExpoService.deleteExpoByID(expo.getExpoID());
        }

        for(Booth booth:user.getBoothList()){
            singleBoothService.deleteBoothByID(booth.getBoothID(), account);
        }

        userRoleService.deleteUserRoleByAccount(account);

        userRepository.delete(user);
    }


    public List<ExpoOverviewResponse> getAllExpoOverview(String account){
        System.out.println("SingleUserService: getAllExpoOverview >> "+account);
        User user = userHelperService.getUserByAccount(account);

        return user.getExpoList().stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }


    public List<BoothOverviewResponse> getAllBoothOverview(String account){
        System.out.println("SingleUserService: getAllBoothOverview >> "+account);
        User user = userHelperService.getUserByAccount(account);

        return user.getBoothList().stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public void switchRole(String account){
        System.out.println("SingleUserService: switchRole >> "+account);
        userHelperService.getUserByAccount(account);
        UserRole userRole = userRoleService.getUserRoleByAccount(account);

        String roleName = userRole.getRole().getName();
        String newRoleName = roleName.equals("GENERAL")? "FOUNDER": "GENERAL";
        Role newRole = roleService.getRoleByName(newRoleName);

        userRole.setRole(newRole);
        userRoleService.saveUserRole(userRole);
    }


    public void verifyUser(String userAccountOrMail, String code){
        System.out.println("SingleUserService: verifyUser >> "+userAccountOrMail+", "+code);
        User user = userHelperService.getUserByAccountOrMail(userAccountOrMail);
        if(user.getIsVerified()) throw new ConflictException("使用者帳號或電子郵箱為 < "+userAccountOrMail+" > 的使用者已完成信箱驗證");
        else if(user.getVerifyDateline().isBefore(LocalDateTime.now())) throw new CodeExpiredException("驗證碼已過期，請重新申請驗證");
        else if(user.getVerificationCode().equals(code)) {
            user.setIsVerified(true);
            user.setVerificationCode(null);
            user.setVerifyDateline(null);
            userRepository.save(user);
        }else throw new BadRequestException("XX 驗證碼錯誤 XX");
    }


    public void resetPassword(String userAccountOrMail, ResetPasswordRequest request){
        System.out.println("SingleUserService: resetPassword");
        User user = userHelperService.getUserByAccountOrMail(userAccountOrMail);
        String code = request.getCode();
        String newPassword = request.getPassword();

        if(user.getResetPasswordDateline().isBefore(LocalDateTime.now())) throw new CodeExpiredException("驗證碼已過期，請重新申請重設密碼");
        else if (user.getResetPasswordCode().equals(code)) {
            if (passwordEncoder.matches(newPassword, user.getPassword())) throw new BadRequestException("The new password cannot be as same as the old password.");

            String encodedPassword = passwordEncoder.encode(newPassword);

            user.setPassword(encodedPassword);
            user.setResetPasswordCode(null);
            user.setResetPasswordDateline(null);

            userRepository.save(user);
        }else throw new BadRequestException("XX 驗證碼錯誤 XX");
    }


    public void changePassword(String account, ChangePasswordRequest request){
        System.out.println("SingleUserService: changePassword >> "+account);
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        User user = userHelperService.getUserByAccount(account);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) throw new UnauthorizedException("XX Wrong password XX");
        if (oldPassword.equals(newPassword)) throw new BadRequestException("The new password cannot be the same as the old password.");

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
