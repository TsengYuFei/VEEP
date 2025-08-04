package com.example.api.Service;

import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.TagResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.ForibiddenException;
import com.example.api.Exception.NotFoundException;
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
public class SingleBoothService {
    @Autowired
    private final BoothRepository boothRepository;

    @Autowired
    private final BoothColListService colListService;

    @Autowired
    private final UserHelperService userHelperService;

    @Autowired
    private final ExpoHelperService expoHelperService;

    @Autowired
    private final BoothStaffListService staffListService;

    @Autowired
    private final TagService tagService;

    @Autowired
    private final ContentService contentService;

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final BoothHelperService boothHelperService;

    @Autowired
    private final BoothLogService boothLogService;



    public BoothEditResponse getBoothEditByID(Integer boothID) {
        System.out.println("SingleBoothService: getBoothEditByID >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);
        BoothEditResponse response = BoothEditResponse.fromBooth(booth);
        Expo expo = booth.getExpo();
        if(expo == null) response.setExpoID(null);
        else response.setExpoID(expo.getExpoID());

        // Collaborator
        Set<User> cols = booth.getCollaborator().getCollaborators();
        if(cols != null && !cols.isEmpty()) {
            List<UserListResponse> collaborators = cols.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setCollaborators(collaborators);
        }else response.setCollaborators(null);

        // Staff
        Set<User> stas = booth.getStaff().getStaffs();
        if(stas != null && !stas.isEmpty()) {
            List<UserListResponse> staffs = stas.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setStaffs(staffs);
        }else response.setStaffs(null);

        // Tag
        Set<Tag> tagNames = booth.getTags();
        if(tagNames != null && !tagNames.isEmpty()){
            List<TagResponse> tags = tagNames.stream()
                    .map(TagResponse::fromTag)
                    .toList();
            response.setTags(tags);
        }else response.setTags(null);

        return response;
    }


    @Transactional
    public Integer createBooth(String userAccount, Integer expoID, String ownerAccount) {
        System.out.println("SingleBoothService: createBooth >> "+ userAccount+", "+expoID+", "+ownerAccount);

        User owner = userHelperService.getUserByAccount(ownerAccount);
        Expo expo = expoHelperService.getExpoByID(expoID);

        Booth booth = new Booth();
        booth.setExpo(expo);
        booth.setOwner(owner);
        booth.setCollaborator(new BoothCollaboratorList());
        booth.setStaff(new BoothStaffList());
        booth.setTags(new HashSet<>());
        boothRepository.save(booth);

        // Content
        List<Content> contentList = contentService.createDefaultContent(booth);
        booth.setContentList(contentList);

        return booth.getBoothID();
    }


    @Transactional
    public void updateBoothByID(Integer boothID, BoothUpdateRequest request){
        System.out.println("SingleBoothService: updateBoothByID >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);

        if(booth.getAvatar() != null && request.getAvatar() != null){
            imageService.deleteImageByName(booth.getAvatar());
        }

        booth.setName(updateIfNotBlank(booth.getName(), request.getName()));
        booth.setAvatar(updateIfNotBlank(booth.getAvatar(), request.getAvatar()));
        booth.setIntroduction(updateIfNotBlank(booth.getIntroduction(), request.getIntroduction()));
        booth.setOpenMode(updateIfNotNull(booth.getOpenMode(), request.getOpenMode()));
        booth.setOpenStatus(updateIfNotNull(booth.getOpenStatus(), request.getOpenStatus()));
        booth.setOpenStart(updateIfNotNull(booth.getOpenStart(), request.getOpenStart()));
        booth.setOpenEnd(updateIfNotNull(booth.getOpenEnd(), request.getOpenEnd()));
        booth.setMaxParticipants(updateIfNotNull(booth.getMaxParticipants(), request.getMaxParticipants()));
        booth.setDisplay(updateIfNotNull(booth.getDisplay(), request.getDisplay()));

        // Collaborator
        List<String> newColAccounts = request.getCollaborators();
        BoothCollaboratorList collaborator = booth.getCollaborator();
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

        // Staff
        List<String> newStaffAccounts = request.getStaffs();
        BoothStaffList staff = booth.getStaff();
        if (newStaffAccounts != null) {
            staff.getStaffs().clear();

            if (!newStaffAccounts.isEmpty()) {
                List<User> userList = userHelperService.getAllUserByAccount(newStaffAccounts);

                for (User user : userList) {
                    if (!staffListService.existInList(staff.getId(), user.getUserAccount())) {
                        staff.getStaffs().add(user);
                    }
                }
            }
        }

        // Tag
        List<String> newTagNames = request.getTags();
        Set<Tag> tags = booth.getTags();
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

        boothRepository.save(booth);
    }


    @Transactional
    public void deleteBoothByID(Integer boothID, String account) {
        System.out.println("SingleBoothService: deleteBoothByID >> "+boothID+", "+account);
        User user = userHelperService.getUserByAccount(account);
        Booth booth = boothHelperService.getBoothByID(boothID);
        Expo expo = booth.getExpo();

        if(expo.getOwner() != user && !expo.getCollaborator().getCollaborators().contains(user)) throw new ForibiddenException("權限不足，不可刪除此展會");

        String avatar = booth.getAvatar();
        if (avatar != null) imageService.deleteImageByName(avatar);

        for (Content content : booth.getContentList()) {
            String image = content.getImage();
            if (image != null) imageService.deleteImageByName(image);
        }

        boothLogService.deleteBoothLogByBoothID(boothID);
        boothRepository.delete(booth);

    }


    public List<String> getAllColAccountList(Integer boothID){
        System.out.println("SingleBoothService: getAllColAccountList >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);
        Set<User> users = booth.getCollaborator().getCollaborators();

        return (users != null && !users.isEmpty())?
                users.stream()
                        .map(User::getUserAccount)
                        .toList()
                : new ArrayList<>();
    }


    public List<String> getAllStaffAccountList(Integer boothID){
        System.out.println("SingleBoothService: getAllStaffAccountList >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);
        Set<User> users = booth.getStaff().getStaffs();

        return (users != null && !users.isEmpty())?
                users.stream()
                        .map(User::getUserAccount)
                        .toList()
                : new ArrayList<>();
    }


    public List<UserListResponse> getAllColList(Integer boothID){
        System.out.println("SingleBoothService: getAllColList >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);
        List<UserListResponse> response;

        Set<User> users = booth.getCollaborator().getCollaborators();
        if(users != null && !users.isEmpty()) {
            response = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<UserListResponse> getAllStaff(Integer boothID){
        System.out.println("SingleBoothService: getAllStaff >> "+boothID);
        Booth booth = boothHelperService.getBoothByID(boothID);
        List<UserListResponse> response;

        Set<User> stas = booth.getStaff().getStaffs();
        if(stas != null && !stas.isEmpty()) {
            response = stas.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }
}
