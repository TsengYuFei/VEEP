package com.example.api.Service;


import com.example.api.DTO.Request.BoothLogUpdateRequest;
import com.example.api.DTO.Response.BoothLogResponse;
import com.example.api.DTO.Response.LogCreateResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoothLogService {
    @Autowired
    private final BoothLogRepository boothLogRepository;

    @Autowired
    private final SingleExpoService singleExpoService;

    @Autowired
    private final SingleBoothService singleBoothService;

    @Autowired
    private final SingleUserService singleUserService;



    @Transactional
    public LogCreateResponse createBoothLog(Integer expoID, Integer boothID, String account){
        System.out.println("BoothLogService: createBoothLog>> "+expoID+", "+boothID+", "+account);
        Expo expo = singleExpoService.getExpoByID(expoID);
        Booth booth = singleBoothService.getBoothByID(boothID);
        User user = singleUserService.getUserByAccount(account);

        String sessionID = UUID.randomUUID().toString();
        BoothLog boothLog = new BoothLog();
        boothLog.setSessionID(sessionID);
        boothLog.setEnterAt(LocalDateTime.now());
        boothLog.setRole(singleUserService.getUserRoleName(account));
        if(booth.getOwner() == user) boothLog.setIsOwner(true);
        if(booth.getCollaborator().getCollaborators().contains(user)) boothLog.setIsCollaborator(true);
        boothLog.setUser(user);
        boothLog.setExpo(expo);
        boothLog.setBooth(booth);

        boothLogRepository.save(boothLog);
        return new LogCreateResponse(sessionID);
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


    @Transactional
    public void updateBoothLog(String sessionID, BoothLogUpdateRequest request){
        System.out.println("BoothLogService: updateBoothLog >> "+sessionID);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);

        if(request.getIsExit()) boothLog.setExitAt(LocalDateTime.now());
        if(request.getIsActive()) boothLog.setLastActiveAt(LocalDateTime.now());
        if(request.getIsUsedAI()) {
            boothLog.setHasUsedAi(true);
            boothLog.setAiMessageCount(boothLog.getAiMessageCount() + 1);
        }
        boothLogRepository.save(boothLog);
    }


    @Transactional
    public void deleteBoothLogBySessionID(String sessionID, String account){
        System.out.println("BoothLogService: deleteBoothLogBySessionID>> "+sessionID+", "+account);
        User user = singleUserService.getUserByAccount(account);
        BoothLog boothLog = getBoothLogBySessionID(sessionID);
        Booth booth = boothLog.getBooth();
        Expo expo = boothLog.getExpo();

        if(booth.getOwner() == user || expo.getOwner() == user || expo.getCollaborator().getCollaborators().contains(user)) boothLogRepository.delete(boothLog);
        else throw new ForibiddenException("權限不足，不可刪除此攤位 log");
    }


    @Transactional
    public void deleteBoothLogByExpoID(Integer expoID){
        System.out.println("BoothLogService: deleteBoothLogByExpoID>> "+expoID);
        singleExpoService.getExpoByID(expoID);
        boothLogRepository.deleteByExpo_ExpoID(expoID);
    }


    @Transactional
    public void deleteBoothLogByBoothID(Integer boothID){
        System.out.println("BoothLogService: deleteBoothLogByBoothID>> "+boothID);
        singleBoothService.getBoothByID(boothID);
        boothLogRepository.deleteByBooth_BoothID(boothID);
    }
}
