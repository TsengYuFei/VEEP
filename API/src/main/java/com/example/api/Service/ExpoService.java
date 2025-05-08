package com.example.api.Service;

import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.CollaboratorUserResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.ExpoCollaboratorListRepository;
import com.example.api.Repository.ExpoRepository;
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
public class ExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ExpoCollaboratorListRepository colListRepository;

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

        ExpoEditResponse response = modelMapper.map(expo, ExpoEditResponse.class);
        Set<User> users = expo.getCollaborator().getCollaborators();
        if(users != null && !users.isEmpty()){
            List<CollaboratorUserResponse> collaborators = users.stream()
                    .map(user -> modelMapper.map(user, CollaboratorUserResponse.class))
                    .toList();
            response.setCollaborators(collaborators);
        }else response.setCollaborators(null);

        return response;
    }


    @Transactional
    public Integer createExpo(ExpoCreateRequest request) {
        System.out.println("ExpoService: createExpo");
        request.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        request.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));
        request.setAccessCode(updateIfNotBlank(null, request.getAvatar()));

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Expo expo = modelMapper.map(request, Expo.class);

        List<String> userIDs = request.getCollaborators();
        ExpoCollaboratorList collaborator = new ExpoCollaboratorList();
        if(userIDs != null && !userIDs.isEmpty()){
            List<User> userList = userRepository.findAllById(userIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(new HashSet<>());
        expo.setCollaborator(collaborator);

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

        List<String> newUserAccounts = request.getCollaborators();
        ExpoCollaboratorList collaborator = expo.getCollaborator();

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

        expoRepository.save(expo);
    }


    @Transactional
    public void deleteExpoByID(Integer expoID){
        System.out.println("ExpoService: deleteExpoByID >> "+expoID);
        Expo expo = getExpoByID(expoID);
        expoRepository.delete(expo);
    }
}
