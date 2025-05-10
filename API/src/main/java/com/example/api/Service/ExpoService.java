package com.example.api.Service;

import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.TagResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class ExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ExpoCollaboratorListRepository colListRepository;

    @Autowired
    private final BlacklistRepository blacklistRepository;

    @Autowired
    private final WhitelistRepository whitelistRepository;

    @Autowired
    private final TagRepository tagRepository;

    @Autowired
    private final ModelMapper modelMapper;



    private Expo getExpoByID(Integer expoID){
        System.out.println("ExpoService: getExpoByID >> "+expoID);
        return expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
    }


    public ExpoEditResponse getExpoEditByID(Integer expoID) {
        System.out.println("ExpoService: getExpoEditByID >> "+expoID);
        Expo expo = getExpoByID(expoID);
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
    public Integer createExpo(ExpoCreateRequest request) {
        System.out.println("ExpoService: createExpo");
        request.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        request.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));
        request.setAccessCode(updateIfNotBlank(null, request.getAccessCode()));

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Expo expo = modelMapper.map(request, Expo.class);

        // Collaborator
        List<String> colIDs = request.getCollaborators();
        ExpoCollaboratorList collaborator = new ExpoCollaboratorList();
        if(colIDs != null && !colIDs.isEmpty()){
            List<User> userList = userRepository.findAllById(colIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(new HashSet<>());
        expo.setCollaborator(collaborator);

        // Blacklist
        List<String> blackIDs = request.getBlacklist();
        Blacklist blacklistedUser = new Blacklist();
        if(blackIDs != null && !blackIDs.isEmpty()){
            List<User> userList = userRepository.findAllById(blackIDs);
            Set<User> users = new HashSet<>(userList);
            blacklistedUser.setBlacklistedUsers(users);
        }else blacklistedUser.setBlacklistedUsers(new HashSet<>());
        expo.setBlacklist(blacklistedUser);

        // Whitelist
        List<String> whiteIDs = request.getWhitelist();
        Whitelist whitelistedUser = new Whitelist();
        if(whiteIDs != null && !whiteIDs.isEmpty()){
            List<User> userList = userRepository.findAllById(whiteIDs);
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
                Tag tag = tagRepository.findByName(name);
                if(tag == null) tag = tagRepository.save(new Tag(name));
                tags.add(tag);
            }
        }
        expo.setTags(tags);


        expoRepository.save(expo);
        return expo.getExpoID();
    }


    @Transactional
    public void updateExpoByID(Integer expoID, ExpoUpdateRequest request){
        System.out.println("ExpoService: updateExpoByID >> "+expoID);

        Expo expo = getExpoByID(expoID);
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
                List<User> userList = userRepository.findAllById(newColAccounts);

                for (User user : userList) {
                    if (!colListRepository.existsByIdAndCollaborators_UserAccount(collaborator.getId(), user.getUserAccount())) {
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
                List<User> userList = userRepository.findAllById(newBlackAccounts);

                for (User user : userList) {
                    if (!blacklistRepository.existsByIdAndBlacklistedUsers_UserAccount(blacklistedUser.getId(), user.getUserAccount())) {
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
                List<User> userList = userRepository.findAllById(newWhiteAccounts);

                for (User user : userList) {
                    if (!whitelistRepository.existsByIdAndWhitelistedUsers_UserAccount(whitelistedUser.getId(), user.getUserAccount())) {
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
                    Tag tag = tagRepository.findByName(name);
                    if(tag == null) tag = tagRepository.save(new Tag(name));
                    tags.add(tag);
                }
            }
        }

        expoRepository.save(expo);
    }


    @Transactional
    public void deleteExpoByID(Integer expoID){
        System.out.println("ExpoService: deleteExpoByID >> "+expoID);
        Expo expo = getExpoByID(expoID);
        expoRepository.delete(expo);
    }
}
