package com.example.api.Service;


import com.example.api.DTO.Request.ExpoLogUpdateRequest;
import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoLogCreateResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoLog;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ExpoLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class ExpoLogService {
    @Autowired
    private final ExpoLogRepository expoLogRepository;

    @Autowired
    private final SingleExpoService singleExpoService;

    @Autowired
    private final SingleUserService singleUserService;



    @Transactional
    public ExpoLogCreateResponse createExpoLog(Integer expoID, String account){
        System.out.println("ExpoLogService: createExpoLog>> "+expoID+", "+account);
        Expo expo = singleExpoService.getExpoByID(expoID);
        User user = singleUserService.getUserByAccount(account);

        String sessionID = UUID.randomUUID().toString();
        ExpoLog expoLog = new ExpoLog();
        expoLog.setSessionID(sessionID);
        expoLog.setRole(singleUserService.getUserRoleName(account));
        if(expo.getOwner() == user) expoLog.setIsOwner(true);
        if(expo.getCollaborator().getCollaborators().contains(user)) expoLog.setIsCollaborator(true);
        if(expo.getWhitelist().getWhitelistedUsers().contains(user)) expoLog.setIsWhiteListed(true);
        expoLog.setUser(user);
        expoLog.setExpo(expo);

        expoLogRepository.save(expoLog);
        return new ExpoLogCreateResponse(sessionID);
    }


    public ExpoLog getExpoLogBySessionID(String sessionID){
        System.out.println("ExpoLogService: getExpoLog>> "+sessionID);
        return expoLogRepository.findBySessionID(sessionID)
                .orElseThrow(() -> new NotFoundException("找不到Session ID為 < "+ sessionID+" > 的展會log"));

    }


    public ExpoLogResponse getExpoLogResponse(String sessionID){
        System.out.println("ExpoLogService: getExpoLogResponse>> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);
        return ExpoLogResponse.fromExpoLog(expoLog);
    }


    public List<ExpoLogResponse> getAllExpoLogResponse(Integer expoID){
        System.out.println("ExpoLogService: getAllExpoLogResponse >> "+expoID);
        return expoLogRepository.findExpoLogByExpo_ExpoID(expoID)
                .stream()
                .map(ExpoLogResponse::fromExpoLog)
                .toList();
    }


    @Transactional
    public void updateExpoLog(String sessionID, ExpoLogUpdateRequest request){
        System.out.println("ExpoLogService: updateExpoLog >> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);

        if(request.getIsExit()) expoLog.setExitAt(LocalDateTime.now());
        if(request.getIsActive()) expoLog.setLastActiveAt(LocalDateTime.now());
        if(request.getIsUsedAI()) {
            expoLog.setHasUsedAi(true);
            expoLog.setAiMessageCount(expoLog.getAiMessageCount() + 1);
        }
        expoLogRepository.save(expoLog);
    }


    @Transactional
    public void deleteExpoLogBySessionID(String sessionID){
        System.out.println("ExpoLogService: deleteExpoLogBySessionID>> "+sessionID);
        ExpoLog expoLog = getExpoLogBySessionID(sessionID);
        expoLogRepository.delete(expoLog);
    }


    @Transactional
    public void deleteExpoLogByExpoID(Integer expoID){
        System.out.println("ExpoLogService: deleteExpoLogByExpoID>> "+expoID);
        Expo expo = singleExpoService.getExpoByID(expoID);
        expoLogRepository.deleteByExpo_ExpoID(expoID);
    }
}
