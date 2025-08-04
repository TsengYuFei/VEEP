package com.example.api.Service;

import com.example.api.DTO.Response.ContentLogResponse;
import com.example.api.Entity.*;
import com.example.api.Repository.ContentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentLogService {
    @Autowired
    private final ContentLogRepository contentLogRepository;

    @Autowired
    private final ContentService contentService;

    @Autowired
    private SingleUserService singleUserService;

    @Autowired
    private final SingleExpoService singleExpoService;

    @Autowired
    private final SingleBoothService singleBoothService;



    @Transactional
    public void createContentLogByBoothIDAndNumber(Integer boothID, Integer number, String account) {
        System.out.println("ContentLogService: createContentLogByContentID >> "+boothID+", "+number+", "+account);
        Content content = contentService.getContentByBoothIDAndNumber(boothID, number);
        Booth booth = content.getBooth();
        Expo expo = booth.getExpo();
        User user = singleUserService.getUserByAccount(account);

        ContentLog contentLog = new ContentLog();
        contentLog.setContentNumber(content.getNumber());
        contentLog.setClickAt(LocalDateTime.now());
        contentLog.setRole(singleUserService.getUserRoleName(account));
        if(booth.getOwner() == user) contentLog.setIsOwner(true);
        if(booth.getCollaborator().getCollaborators().contains(user)) contentLog.setIsCollaborator(true);
        contentLog.setUser(user);
        contentLog.setExpo(expo);
        contentLog.setBooth(booth);
        contentLog.setContent(content);

        contentLogRepository.save(contentLog);
    }


    public List<ContentLogResponse> getAllContentLogByBoothID (Integer boothID){
        System.out.println("ContentLogService: getAllContentLogByBoothID >> "+boothID);
        return contentLogRepository.findContentLogByBooth_BoothID(boothID)
                .stream()
                .map(ContentLogResponse::fromContentLog)
                .toList();
    }


    public List<ContentLogResponse> getAllContentLogByExpoID (Integer expoID){
        System.out.println("ContentLogService: getAllContentLogByExpoID >> "+expoID);
        return contentLogRepository.findContentLogByExpo_ExpoID(expoID)
                .stream()
                .map(ContentLogResponse::fromContentLog)
                .toList();
    }


    public List<ContentLogResponse> getAllContentLogByBoothIDAndNumber (Integer boothID, Integer number){
        System.out.println("ContentLogService: getAllContentLogByBoothIDAndNumber >> "+boothID+", "+number);
        return contentLogRepository.findContentLogByBooth_BoothIDAndContent_Number(boothID, number)
                .stream()
                .map(ContentLogResponse::fromContentLog)
                .toList();
    }


    @Transactional
    public void deleteContentLogByExpoID(Integer expoID){
        System.out.println("ContentLogService: deleteContentLogByExpoID>> "+expoID);
        singleExpoService.getExpoByID(expoID);
        contentLogRepository.deleteByExpo_ExpoID(expoID);
    }


    @Transactional
    public void deleteContentLogByBoothID(Integer boothID){
        System.out.println("ContentLogService: deleteContentLogByBoothID>> "+boothID);
        singleBoothService.getBoothByID(boothID);
        contentLogRepository.deleteByBooth_BoothID(boothID);
    }


    @Transactional
    public void deleteContentLogByBoothIDAndNumber(Integer boothID, Integer number){
        System.out.println("ContentLogService: deleteContentLogByBoothIDAndNumber>> "+boothID+", "+number);
        contentService.getContentByBoothIDAndNumber(boothID, number);
        contentLogRepository.deleteByBooth_BoothIDAndContent_Number(boothID, number);
    }
}
