package com.example.api.Service;

import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.User;
import com.example.api.Repository.ExpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BatchExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    @Autowired
    private final ExpoService expoService;



    public List<ExpoOverviewResponse> getAllExpoOverview() {
        System.out.println("BatchExpoService: getAllExpoOverview");
        return  expoRepository.findAll()
                .stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }


    public Page<ExpoOverviewResponse> getAllExpoOverviewPage(Integer page, Integer size) {
        System.out.println("BatchExpoService: getAllExpoOverviewPage >> "+page+", "+size);
        Pageable pageable = PageRequest.of(page, size);
        return expoRepository.findAll(pageable).map(ExpoOverviewResponse::fromExpo);
    }


    public List<UserListResponse> getAllCollaborator(Integer expoID){
        System.out.println("BatchExpoService: getAllCollaborator >> "+expoID);
        Expo expo = expoService.getExpoByID(expoID);
        List<UserListResponse> response;

        Set<User> users = expo.getCollaborator().getCollaborators();
        if(users != null && !users.isEmpty()) {
            response = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<UserListResponse> getAllBlack(Integer expoID){
        System.out.println("BatchExpoService: getAllBlack >> "+expoID);
        Expo expo = expoService.getExpoByID(expoID);
        List<UserListResponse> response;

        Set<User> users = expo.getBlacklist().getBlacklistedUsers();
        if(users != null && !users.isEmpty()) {
            response = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<UserListResponse> getAllWhite(Integer expoID){
        System.out.println("BatchExpoService: getAllWhite >> "+expoID);
        Expo expo = expoService.getExpoByID(expoID);
        List<UserListResponse> response;

        Set<User> users = expo.getWhitelist().getWhitelistedUsers();
        if(users != null && !users.isEmpty()) {
            response = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<ExpoOverviewResponse> getTagExpoOverview(String tags){
        System.out.println("BatchExpoService: getTagExpoOverview >> "+tags);
        if(tags == null) return new ArrayList<>();

        return expoRepository.findExposByTagsName(tags)
                .stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }
}
