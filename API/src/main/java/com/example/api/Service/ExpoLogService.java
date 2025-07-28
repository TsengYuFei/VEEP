package com.example.api.Service;


import com.example.api.DTO.Response.ExpoLogCreateResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.ExpoLog;
import com.example.api.Entity.User;
import com.example.api.Repository.ExpoLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
