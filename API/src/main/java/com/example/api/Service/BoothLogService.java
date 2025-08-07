package com.example.api.Service;

import com.example.api.DTO.Request.BoothLogUpdateRequest;
import com.example.api.DTO.Response.BoothLogResponse;
import com.example.api.DTO.Response.ExpoEnterResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoothLogService {
    @Autowired
    private final BoothLogRepository boothLogRepository;

    @Autowired
    private final ExpoHelperService expoHelperService;

    @Autowired
    private final BoothHelperService boothHelperService;

    @Autowired
    private final UserRoleService userRoleService;

    @Autowired
    private final UserHelperService userHelperService;

    @Autowired
    private final ContentLogService contentLogService;



    @Transactional
    public ExpoEnterResponse createBoothLog(Integer boothID, String account){
        System.out.println("BoothLogService: createBoothLog>> "+boothID+", "+account);
        Booth booth = boothHelperService.getBoothByID(boothID);
        Expo expo = booth.getExpo();
        User user = userHelperService.getUserByAccount(account);

        String sessionID = UUID.randomUUID().toString();
        BoothLog boothLog = new BoothLog();
        boothLog.setSessionID(sessionID);
        boothLog.setEnterAt(LocalDateTime.now());
        boothLog.setRole(userRoleService.getUserRoleName(account));
        if(booth.getOwner() == user) boothLog.setIsOwner(true);
        if(booth.getCollaborator().getCollaborators().contains(user)) boothLog.setIsCollaborator(true);
        boothLog.setUser(user);
        boothLog.setExpo(expo);
        boothLog.setBooth(booth);

        boothLogRepository.save(boothLog);
        return new ExpoEnterResponse(sessionID);
    }


    public Boolean existBoothLogBySessionID(String sessionID){
        System.out.println("BoothLogService: existBoothLogBySessionID>> "+sessionID);
        Optional<BoothLog> log = boothLogRepository.findBySessionID(sessionID);
        return log.isPresent();
    }


    public Boolean isOnlineBoothLogBySessionID(String sessionID){
        System.out.println("BoothLogService: isOnlineBoothLogBySessionID>> "+sessionID);
        BoothLog log = getBoothLogBySessionID(sessionID);
        return log.getExitAt() == null;
    }


    public BoothLog getBoothLogBySessionID(String sessionID){
        System.out.println("BoothLogService: getBoothLogBySessionID>> "+sessionID);
        return boothLogRepository.findBySessionID(sessionID)
                .orElseThrow(() -> new NotFoundException("找不到Session ID為 < "+ sessionID+" > 的攤位log"));

    }


    public BoothLogResponse getBoothLogResponse(String sessionID){
        System.out.println("BoothLogService: getBoothLogResponse>> "+sessionID);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);
        return BoothLogResponse.fromBoothLog(boothLog);
    }


    public List<BoothLogResponse> getAllBoothLogResponseByExpoID(Integer expoID){
        System.out.println("BoothLogService: getAllBoothLogResponseByExpoID >> "+expoID);
        return boothLogRepository.findExpoLogByExpo_ExpoID(expoID)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getAllBoothLogResponseByBoothID(Integer boothID){
        System.out.println("BoothLogService: getAllBoothLogResponseByBoothID >> "+boothID);
        return boothLogRepository.findBoothLogByBooth_BoothID(boothID)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getBoothLogResponseByUserAccount(String account){
        System.out.println("BoothLogService: getBoothLogResponseByUserAccount >> "+account);
        return boothLogRepository.findBoothLogByUser_UserAccountOrderByEnterAtDesc(account)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getBoothLogResponseByUserAccountAndExpoID(Integer expoID, String account){
        System.out.println("BoothLogService: getBoothLogResponseByUserAccountAndExpoID >> "+account+", "+expoID);
        return boothLogRepository.findBoothLogByUser_UserAccountAndExpo_ExpoIDOrderByEnterAtDesc(account, expoID)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getBoothLogResponseByUserAccountAndBoothID(Integer boothID, String account){
        System.out.println("BoothLogService: getBoothLogResponseByUserAccountAndBoothID >> "+account+", "+boothID);
        return boothLogRepository.findBoothLogByUser_UserAccountAndBooth_BoothIDOrderByEnterAtDesc(account, boothID)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getOnlineBoothLogResponseByExpoID(Integer expoID){
        System.out.println("BoothLogService: getOnlineBoothLogResponseByExpoID >> "+expoID);
        return boothLogRepository.findBoothLogByExpo_ExpoIDAndExitAt(expoID, null)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public List<BoothLogResponse> getOnlineBoothLogResponseByBoothID(Integer boothID){
        System.out.println("BoothLogService: getOnlineBoothLogResponseByBoothID >> "+boothID);
        return boothLogRepository.findBoothLogByBooth_BoothIDAndExitAt(boothID, null)
                .stream()
                .map(BoothLogResponse::fromBoothLog)
                .toList();
    }


    public Integer getOnlineNumberByBoothID(Integer boothID){
        System.out.println("BoothLogService: getOnlineNumberByBoothID >> "+boothID);
        return boothLogRepository.countByBooth_BoothIDAndExitAt(boothID, null);
    }


    @Transactional
    public void updateBoothLog(String sessionID, BoothLogUpdateRequest request){
        System.out.println("BoothLogService: updateBoothLog >> "+sessionID);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);

        if(request.getIsExit()) boothLog.setExitAt(LocalDateTime.now());
        if(request.getIsUsedAI()) {
            boothLog.setHasUsedAi(true);
            boothLog.setAiMessageCount(boothLog.getAiMessageCount() + 1);
        }
        boothLogRepository.save(boothLog);
    }


    @Transactional
    public void updateBoothLogLastActivity(String sessionID){
        System.out.println("BoothLogService: updateBoothLogLastActivity >> "+sessionID);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);

        boothLog.setLastActiveAt(LocalDateTime.now());
        boothLogRepository.save(boothLog);
    }


    @Transactional
    public void deleteBoothLogBySessionID(String sessionID, String account){
        System.out.println("BoothLogService: deleteBoothLogBySessionID>> "+sessionID+", "+account);
        User user = userHelperService.getUserByAccount(account);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);
        Booth booth = boothLog.getBooth();
        Expo expo = boothLog.getExpo();

        if(booth.getOwner() == user || expo.getOwner() == user || expo.getCollaborator().getCollaborators().contains(user)) {
            boothLogRepository.delete(boothLog);
            contentLogService.deleteContentLogByBoothID(booth.getBoothID());
        }
        else throw new ForibiddenException("權限不足，不可刪除此攤位 log");
    }


    @Transactional
    public void deleteBoothLogByExpoID(Integer expoID){
        System.out.println("BoothLogService: deleteBoothLogByExpoID>> "+expoID);
        expoHelperService.getExpoByID(expoID);
        boothLogRepository.deleteByExpo_ExpoID(expoID);
        contentLogService.deleteContentLogByExpoID(expoID);
    }


    @Transactional
    public void deleteBoothLogByBoothID(Integer boothID){
        System.out.println("BoothLogService: deleteBoothLogByBoothID>> "+boothID);
        boothHelperService.getBoothByID(boothID);
        boothLogRepository.deleteByBooth_BoothID(boothID);
        contentLogService.deleteContentLogByBoothID(boothID);
    }
}
