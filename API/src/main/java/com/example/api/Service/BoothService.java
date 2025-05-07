package com.example.api.Service;

import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Request.BoothUpdateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.CollaboratorUserResponse;
import com.example.api.Entity.Booth;
import com.example.api.Entity.CollaboratorList;
import com.example.api.Entity.OpenMode;
import com.example.api.Entity.User;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.CollaboratorListRepository;
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
    private final CollaboratorListRepository colListRepository;

    @Autowired
    private final ModelMapper modelMapper;



    private Booth getBoothByID(Integer boothID) {
        System.out.println("BoothService: getBoothByID >> "+boothID);
        return boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));
    }


    public BoothEditResponse getBoothEditByID(Integer boothID) {
        System.out.println("BoothService: getBoothEditByID >> "+boothID);
        Booth booth = boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));

        BoothEditResponse response = modelMapper.map(booth, BoothEditResponse.class);
        Set<User> users = booth.getCollaborator().getCollaborators();
        if(users != null && !users.isEmpty()) {
            List<CollaboratorUserResponse> collaborators = users.stream()
                    .map(user -> modelMapper.map(user, CollaboratorUserResponse.class))
                    .toList();
            response.setCollaborators(collaborators);
        }else response.setCollaborators(null);

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

        List<String> userIDs = request.getCollaborators();
        CollaboratorList collaborator = new CollaboratorList();
        if(userIDs != null && !userIDs.isEmpty()) {
            List<User> userList = userRepository.findAllById(userIDs);
            Set<User> users = new HashSet<>(userList);
            collaborator.setCollaborators(users);
        }else collaborator.setCollaborators(null);

        collaborator = colListRepository.save(collaborator);
        booth.setCollaborator(collaborator);

        boothRepository.save(booth);
        return booth.getBoothID();
    }


    public void updateBoothByID(Integer boothID, BoothUpdateRequest request){
        System.out.println("BoothService: updateBoothByID >> "+boothID);

        CollaboratorList collaborator = new CollaboratorList();
        List<User> userList = userRepository.findAllById(request.getCollaborators());
        Set<User> users = new HashSet<>(userList);
        collaborator.setCollaborators(users);
        collaborator = colListRepository.save(collaborator);

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
        booth.setCollaborator(collaborator);

        boothRepository.save(booth);
    }


    public void deleteBoothByID(Integer boothID){
        System.out.println("BoothService: deleteBoothByID >> "+boothID);
        Booth booth = getBoothByID(boothID);
        boothRepository.delete(booth);
    }
}
