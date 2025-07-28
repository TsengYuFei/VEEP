package com.example.api.Service;


import com.example.api.DTO.Response.ExpoLogCreateResponse;
import com.example.api.DTO.Response.ExpoLogResponse;
import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoLog;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ExpoLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
}
