package com.example.api.Service;

import com.example.api.DTO.Request.ExpoLogUpdateRequest;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.DTO.Response.UserOverviewResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoLog;
import com.example.api.Entity.User;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ExpoLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpoLogService {
    private final ExpoLogRepository expoLogRepository;
    private final ExpoHelperService expoHelperService;
    private final UserRoleService userRoleService;
    private final UserHelperService userHelperService;



    @Transactional
    public String createExpoLog(Integer expoID, String account){
        System.out.println("ExpoLogService: createExpoLog>> "+expoID+", "+account);
        Expo expo = expoHelperService.getExpoByID(expoID);
        User user = userHelperService.getUserByAccount(account);

        String sessionID = UUID.randomUUID().toString();
        ExpoLog expoLog = new ExpoLog();
        expoLog.setSessionID(sessionID);
        expoLog.setEnterAt(LocalDateTime.now());
        expoLog.setRole(userRoleService.getUserRoleName(account));
        if(expo.getOwner() == user) expoLog.setIsOwner(true);
        if(expo.getCollaborator().getCollaborators().contains(user)) expoLog.setIsCollaborator(true);
        if(expo.getWhitelist().getWhitelistedUsers().contains(user)) expoLog.setIsWhiteListed(true);
        expoLog.setUser(user);
        expoLog.setExpo(expo);

        expoLogRepository.save(expoLog);
        return sessionID;
    }


    public ExpoLog getExpoLogBySessionID(String sessionID){
        System.out.println("ExpoLogService: getExpoLog>> "+sessionID);
        return expoLogRepository.findBySessionID(sessionID)
                .orElseThrow(() -> new NotFoundException("找不到Session ID為 < "+ sessionID+" > 的展會log"));

    }


    public Boolean existExpoLogBySessionID(String sessionID){
        System.out.println("ExpoLogService: existExpoLogBySessionID>> "+sessionID);
        Optional<ExpoLog> log = expoLogRepository.findBySessionID(sessionID);
        return log.isPresent();
    }


    public Boolean isOnlineExpoLogBySessionID(String sessionID){
        System.out.println("ExpoLogService: isOnlineExpoLogBySessionID>> "+sessionID);
        ExpoLog log = getExpoLogBySessionID(sessionID);
        return log.getExitAt() == null;
    }


    public ExpoLogResponse getExpoLogResponse(String sessionID){
        System.out.println("ExpoLogService: getExpoLogResponse>> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);
        return ExpoLogResponse.fromExpoLog(expoLog);
    }


    public List<ExpoLogResponse> getAllExpoLogResponse(Integer expoID){
        System.out.println("ExpoLogService: getAllExpoLogResponse >> "+expoID);
        expoHelperService.getExpoByID(expoID);
        return expoLogRepository.findExpoLogByExpo_ExpoID(expoID)
                .stream()
                .map(ExpoLogResponse::fromExpoLog)
                .toList();
    }


    public List<ExpoLogResponse> getExpoLogResponseByUserAccount(String account){
        System.out.println("ExpoLogService: getExpoLogResponseByUserAccount >> "+account);
        userHelperService.getUserByAccount(account);
        return expoLogRepository.findExpoLogByUser_UserAccountOrderByEnterAtDesc(account)
                .stream()
                .map(ExpoLogResponse::fromExpoLog)
                .toList();
    }


    public List<ExpoLogResponse> getExpoLogResponseByUserAccountAndExpoID(Integer expoID, String account){
        System.out.println("ExpoLogService: getExpoLogResponseByUserAccountAndExpoID >> "+account+", "+expoID);
        userHelperService.getUserByAccount(account);
        expoHelperService.getExpoByID(expoID);
        return expoLogRepository.findExpoLogByUser_UserAccountAndExpo_ExpoIDOrderByEnterAtDesc(account, expoID)
                .stream()
                .map(ExpoLogResponse::fromExpoLog)
                .toList();
    }


    public List<ExpoLogResponse> getOnlineExpoLogResponseByExpoID(Integer expoID){
        System.out.println("ExpoLogService: getOnlineExpoLogResponseByExpoID >> "+expoID);
        expoHelperService.getExpoByID(expoID);
        return expoLogRepository.findExpoLogByExpo_ExpoIDAndExitAt(expoID, null)
                .stream()
                .map(ExpoLogResponse::fromExpoLog)
                .toList();
    }


    public Integer getOnlineNumberByExpoID(Integer expoID){
        System.out.println("ExpoLogService: getOnlineNumberByExpoID >> "+expoID);
        expoHelperService.getExpoByID(expoID);
        return expoLogRepository.countByExpo_ExpoIDAndExitAt(expoID, null);
    }


    public List<UserOverviewResponse> getOnlineUserOverviewByExpoID(Integer expoID){
        System.out.println("ExpoLogService: getOnlineUserOverviewByExpoID >> "+expoID);
        expoHelperService.getExpoByID(expoID);

        List<ExpoLog> expoLogs = expoLogRepository.findExpoLogByExpo_ExpoIDAndExitAt(expoID, null);
        List<User> users = new ArrayList<>();
        for(ExpoLog expoLog : expoLogs){
            users.add(expoLog.getUser());
        }
        return users.stream().map(UserOverviewResponse::fromUser).toList();
    }


    @Transactional
    public void updateExpoLog(String sessionID, ExpoLogUpdateRequest request){
        System.out.println("ExpoLogService: updateExpoLog >> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);

        if(request.getIsExit()) expoLog.setExitAt(LocalDateTime.now());
        if(request.getIsUsedAI()) {
            expoLog.setHasUsedAi(true);
            expoLog.setAiMessageCount(expoLog.getAiMessageCount() + 1);
        }
        expoLogRepository.save(expoLog);
    }


    @Transactional
    public void updateExpoLogLastActivity(String sessionID){
        System.out.println("ExpoLogService: updateExpoLogLastActivity >> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);

        expoLog.setLastActiveAt(LocalDateTime.now());
        expoLogRepository.save(expoLog);
    }


    @Transactional
    public void deleteExpoLogBySessionID(String sessionID, String account){
        System.out.println("ExpoLogService: deleteExpoLogBySessionID>> "+sessionID+", "+account);
        User user = userHelperService.getUserByAccount(account);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);
        Expo expo = expoLog.getExpo();

        if(expo.getOwner() == user || expo.getCollaborator().getCollaborators().contains(user)) expoLogRepository.delete(expoLog);
        else throw new ForibiddenException("權限不足，不可刪除此展會 log");
    }


    @Transactional
    public void deleteExpoLogByExpoID(Integer expoID){
        System.out.println("ExpoLogService: deleteExpoLogByExpoID>> "+expoID);
        expoHelperService.getExpoByID(expoID);
        expoLogRepository.deleteByExpo_ExpoID(expoID);
    }
}
