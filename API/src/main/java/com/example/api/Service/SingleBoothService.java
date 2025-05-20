package com.example.api.Service;

import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.TagResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.*;
import lombok.RequiredArgsConstructor;
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
public class SingleBoothService {
    @Autowired
    private final BoothRepository boothRepository;

    @Autowired
    private final BoothColListService colListService;

    @Autowired
    private final SingleUserService singleUserService;

    @Autowired
    private final SingleExpoService singleExpoService;

    @Autowired
    private final BoothStaffListService staffListService;

    @Autowired
    private final TagService tagService;

    @Autowired
    private final ContentService contentService;



    Booth getBoothByID(Integer boothID) {
        System.out.println("BoothService: getBoothByID >> "+boothID);
        return boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));
    }


    public BoothEditResponse getBoothEditByID(Integer boothID) {
        System.out.println("BoothService: getBoothEditByID >> "+boothID);
        Booth booth = getBoothByID(boothID);
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
    public Integer createBooth(String userAccount, Integer expoID, BoothCreateRequest request) {
        System.out.println("BoothService: createBooth >> "+ userAccount+", "+expoID);

        User owner = singleUserService.getUserByAccount(userAccount);
        Expo expo = singleExpoService.getExpoByID(expoID);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Booth booth = new Booth();
        booth.setName(request.getName());
        booth.setExpo(expo);
        booth.setOwner(owner);
        booth.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        booth.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));
        booth.setOpenMode(mode);
        booth.setOpenStatus(status);
        booth.setOpenStart(request.getOpenStart());
        booth.setOpenEnd(request.getOpenEnd());
        booth.setMaxParticipants(request.getMaxParticipants());
        booth.setDisplay(request.getDisplay());

        // Collaborator
        List<String> colUserIDs = request.getCollaborators();
        BoothCollaboratorList collaborator = new BoothCollaboratorList();
        if(colUserIDs != null && !colUserIDs.isEmpty()) {
            List<User> userList = singleUserService.getAllUserByAccount(colUserIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(new HashSet<>());
        booth.setCollaborator(collaborator);

        // Staff
        List<String> staffUserIDs = request.getStaffs();
        BoothStaffList staff = new BoothStaffList();
        if(staffUserIDs != null && !staffUserIDs.isEmpty()) {
            List<User> userList = singleUserService.getAllUserByAccount(staffUserIDs);
            Set<User> users = new HashSet<>(userList);
            staff.setStaffs(users);
        }else staff.setStaffs(new HashSet<>());
        booth.setStaff(staff);

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
        booth.setTags(tags);

        boothRepository.save(booth);

        // Content
        List<Content> contentList = contentService.createDefaultContent(booth);
        booth.setContentList(contentList);

        return booth.getBoothID();
    }


    @Transactional
    public void updateBoothByID(Integer boothID, BoothUpdateRequest request){
        System.out.println("BoothService: updateBoothByID >> "+boothID);

        Booth booth = getBoothByID(boothID);
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
                List<User> userList = singleUserService.getAllUserByAccount(newColAccounts);

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
                List<User> userList = singleUserService.getAllUserByAccount(newStaffAccounts);

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
    public void deleteBoothByID(Integer boothID) {
        System.out.println("BoothService: deleteBoothByID >> "+boothID);
        Booth booth = getBoothByID(boothID);
        boothRepository.delete(booth);
    }
}
