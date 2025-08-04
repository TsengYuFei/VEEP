package com.example.api.Service;

import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.*;
import com.example.api.Entity.*;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class SingleExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    @Autowired
    private final ExpoHelperService expoHelperService;

    @Autowired
    private final ExpoColListService colListService;

    @Autowired
    private final TagService tagService;

    @Autowired
    private final BlackListService blacklistService;

    @Autowired
    private final WhiteListService whiteListService;

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final ExpoLogService expoLogService;

    @Autowired
    private final UserHelperService userHelperService;



    public ExpoEditResponse getExpoEditByID(Integer expoID) {
        System.out.println("SingleExpoService: getExpoEditByID >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);
        ExpoEditResponse response = ExpoEditResponse.fromExpo(expo);

        // Collaborator
        Set<User> colUsers = expo.getCollaborator().getCollaborators();
        if(colUsers != null && !colUsers.isEmpty()){
            List<UserListResponse> collaborators = colUsers.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setCollaborators(collaborators);
        }else response.setCollaborators(null);


        // Blacklist
        Set<User> blackedUser = expo.getBlacklist().getBlacklistedUsers();
        if(blackedUser != null && !blackedUser.isEmpty()){
            List<UserListResponse> users = blackedUser.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setBlacklist(users);
        }else response.setBlacklist(null);

        // Whitelist
        Set<User> whitedUser = expo.getWhitelist().getWhitelistedUsers();
        if(whitedUser != null && !whitedUser.isEmpty()){
            List<UserListResponse> users = whitedUser.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setWhitelist(users);
        }else response.setWhitelist(null);

        // Tag
        Set<Tag> tagNames = expo.getTags();
        if(tagNames != null && !tagNames.isEmpty()){
            List<TagResponse> tags = tagNames.stream()
                    .map(TagResponse::fromTag)
                    .toList();
            response.setTags(tags);
        }else response.setTags(null);

        return response;
    }


    @Transactional
    public Integer createExpo(String userAccount, ExpoCreateRequest request) {
        System.out.println("SingleExpoService: createExpo >> "+ userAccount);

        User owner = userHelperService.getUserByAccount(userAccount);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Expo expo = new Expo();
        expo.setName(request.getName());
        expo.setOwner(owner);
        expo.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        expo.setPrice(request.getPrice());
        expo.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));
        expo.setOpenMode(mode);
        expo.setOpenStatus(status);
        expo.setOpenStart(request.getOpenStart());
        expo.setOpenEnd(request.getOpenEnd());
        expo.setAccessCode(updateIfNotBlank(null, request.getAccessCode()));
        expo.setMaxParticipants(request.getMaxParticipants());
        expo.setDisplay(request.getDisplay());

        // Collaborator
        List<String> colIDs = request.getCollaborators();
        ExpoCollaboratorList collaborator = new ExpoCollaboratorList();
        if(colIDs != null && !colIDs.isEmpty()){
            List<User> userList = userHelperService.getAllUserByAccount(colIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(new HashSet<>());
        expo.setCollaborator(collaborator);

        // Blacklist
        List<String> blackIDs = request.getBlacklist();
        Blacklist blacklistedUser = new Blacklist();
        if(blackIDs != null && !blackIDs.isEmpty()){
            List<User> userList = userHelperService.getAllUserByAccount(blackIDs);
            Set<User> users = new HashSet<>(userList);
            blacklistedUser.setBlacklistedUsers(users);
        }else blacklistedUser.setBlacklistedUsers(new HashSet<>());
        expo.setBlacklist(blacklistedUser);

        // Whitelist
        List<String> whiteIDs = request.getWhitelist();
        Whitelist whitelistedUser = new Whitelist();
        if(whiteIDs != null && !whiteIDs.isEmpty()){
            List<User> userList = userHelperService.getAllUserByAccount(whiteIDs);
            Set<User> users = new HashSet<>(userList);
            whitelistedUser.setWhitelistedUsers(users);
        }else whitelistedUser.setWhitelistedUsers(new HashSet<>());
        expo.setWhitelist(whitelistedUser);

        // Tag
        List<String> tagNames = request.getTags();
        Set<Tag> tags = new HashSet<>();
        if(tagNames != null && !tagNames.isEmpty()){
            for(String name : tagNames){
                name = name.toLowerCase();
                Tag tag = tagService.addTagIfNotExist(name);
                tags.add(tag);
            }
        }
        expo.setTags(tags);


        expoRepository.save(expo);
        return expo.getExpoID();
    }


    @Transactional
    public void updateExpoByID(Integer expoID, ExpoUpdateRequest request){
        System.out.println("SingleExpoService: updateExpoByID >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);

        if(expo.getAvatar() != null && request.getAvatar() != null){
            imageService.deleteImageByName(expo.getAvatar());
        }

        expo.setName(updateIfNotBlank(expo.getName(), request.getName()));
        expo.setAvatar(updateIfNotBlank(expo.getAvatar(), request.getAvatar()));
        expo.setPrice(updateIfNotNull(expo.getPrice(), request.getPrice()));
        expo.setIntroduction(updateIfNotBlank(expo.getIntroduction(), request.getIntroduction()));
        expo.setOpenMode(updateIfNotNull(expo.getOpenMode(), request.getOpenMode()));
        expo.setOpenStatus(updateIfNotNull(expo.getOpenStatus(), request.getOpenStatus()));
        expo.setOpenStart(updateIfNotNull(expo.getOpenStart(), request.getOpenStart()));
        expo.setOpenEnd(updateIfNotNull(expo.getOpenEnd(), request.getOpenEnd()));
        expo.setAccessCode(updateIfNotBlank(expo.getAccessCode(), request.getAccessCode()));
        expo.setMaxParticipants(updateIfNotNull(expo.getMaxParticipants(), request.getMaxParticipants()));
        expo.setDisplay(updateIfNotNull(expo.getDisplay(), request.getDisplay()));

        // Collaborator
        List<String> newColAccounts = request.getCollaborators();
        ExpoCollaboratorList collaborator = expo.getCollaborator();
        if (newColAccounts != null) {
            collaborator.getCollaborators().clear();

            if (!newColAccounts.isEmpty()) {
                List<User> userList = userHelperService.getAllUserByAccount(newColAccounts);

                for (User user : userList) {
                    if (!colListService.existInList(collaborator.getId(), user.getUserAccount())) {
                        collaborator.getCollaborators().add(user);
                    }
                }
            }
        }

        // Blacklist
        List<String> newBlackAccounts = request.getBlacklist();
        Blacklist blacklistedUser = expo.getBlacklist();
        if (newBlackAccounts != null) {
            blacklistedUser.getBlacklistedUsers().clear();

            if (!newBlackAccounts.isEmpty()) {
                List<User> userList = userHelperService.getAllUserByAccount(newBlackAccounts);

                for (User user : userList) {
                    if (!blacklistService.existInList(blacklistedUser.getId(), user.getUserAccount())) {
                        blacklistedUser.getBlacklistedUsers().add(user);
                    }
                }
            }
        }

        // Whitelist
        List<String> newWhiteAccounts = request.getWhitelist();
        Whitelist whitelistedUser = expo.getWhitelist();
        if (newWhiteAccounts != null) {
            whitelistedUser.getWhitelistedUsers().clear();

            if (!newWhiteAccounts.isEmpty()) {
                List<User> userList = userHelperService.getAllUserByAccount(newWhiteAccounts);

                for (User user : userList) {
                    if (!whiteListService.existInList(whitelistedUser.getId(), user.getUserAccount())) {
                        whitelistedUser.getWhitelistedUsers().add(user);
                    }
                }
            }
        }

        // Tag
        List<String> newTagNames = request.getTags();
        Set<Tag> tags = expo.getTags();
        if (newTagNames != null) {
            tags.clear();

            if (!newTagNames.isEmpty()) {
                for(String name : newTagNames){
                    name = name.toLowerCase();
                    Tag tag = tagService.addTagIfNotExist(name);
                    tags.add(tag);
                }
            }
        }

        expoRepository.save(expo);
    }


    @Transactional
    public void deleteExpoByID(Integer expoID){
        System.out.println("SingleExpoService: deleteExpoByID >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);

        for(Booth booth : expo.getBoothList()){
            booth.setExpo(null);
        }

        String image = expo.getAvatar();
        if(image != null) imageService.deleteImageByName(image);

        expoLogService.deleteExpoLogByExpoID(expoID);

        expoRepository.delete(expo);
    }


    public List<String> getAllColAccountList(Integer expoID){
        System.out.println("SingleExpoService: getAllCollaborator >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);
        Set<User> users = expo.getCollaborator().getCollaborators();

        return (users != null && !users.isEmpty())?
                users.stream()
                        .map(User::getUserAccount)
                        .toList()
                : new ArrayList<>();
    }


    public List<UserListResponse> getAllColList(Integer expoID){
        System.out.println("SingleExpoService: getAllColList >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);
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
        System.out.println("SingleExpoService: getAllBlack >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);
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
        System.out.println("SingleExpoService: getAllWhite >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);
        List<UserListResponse> response;

        Set<User> users = expo.getWhitelist().getWhitelistedUsers();
        if(users != null && !users.isEmpty()) {
            response = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<BoothOverviewResponse> getAllBoothOverview(Integer expoID){
        System.out.println("SingleExpoService: getAllBoothOverview >> "+expoID);
        Expo expo = expoHelperService.getExpoByID(expoID);

        return expo.getBoothList().stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }
}
