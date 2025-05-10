package com.example.api.Service;

import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.TagResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.BoothCollaboratorListRepository;
import com.example.api.Repository.TagRepository;
import com.example.api.Repository.UserRepository;
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
public class BoothService {
    @Autowired
    private final BoothRepository boothRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BoothCollaboratorListRepository colListRepository;

    @Autowired
    private final TagRepository tagRepository;

    @Autowired
    private final ModelMapper modelMapper;



    private Booth getBoothByID(Integer boothID) {
        System.out.println("BoothService: getBoothByID >> "+boothID);
        return boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));
    }


    public BoothEditResponse getBoothEditByID(Integer boothID) {
        System.out.println("BoothService: getBoothEditByID >> "+boothID);
        Booth booth = getBoothByID(boothID);
        BoothEditResponse response = BoothEditResponse.fromBooth(booth);

        // Collaborator
        Set<User> users = booth.getCollaborator().getCollaborators();
        if(users != null && !users.isEmpty()) {
            List<UserListResponse> collaborators = users.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
            response.setCollaborators(collaborators);
        }else response.setCollaborators(null);

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
    public Integer createBooth(BoothCreateRequest request) {
        System.out.println("BoothService: createBooth");
        request.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        request.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Booth booth = modelMapper.map(request, Booth.class);

        // Collaborator
        List<String> userIDs = request.getCollaborators();
        BoothCollaboratorList collaborator = new BoothCollaboratorList();
        if(userIDs != null && !userIDs.isEmpty()) {
            List<User> userList = userRepository.findAllById(userIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(new HashSet<>());
        booth.setCollaborator(collaborator);

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
        booth.setTags(tags);

        boothRepository.save(booth);
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
        List<String> newUserAccounts = request.getCollaborators();
        BoothCollaboratorList collaborator = booth.getCollaborator();
        if (newUserAccounts != null) {
            collaborator.getCollaborators().clear();

            if (!newUserAccounts.isEmpty()) {
                List<User> userList = userRepository.findAllById(newUserAccounts);

                for (User user : userList) {
                    if (!colListRepository.existsByIdAndCollaborators_UserAccount(collaborator.getId(), user.getUserAccount())) {
                        collaborator.getCollaborators().add(user);
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
                    Tag tag = tagRepository.findByName(name);
                    if(tag == null) tag = tagRepository.save(new Tag(name));
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
