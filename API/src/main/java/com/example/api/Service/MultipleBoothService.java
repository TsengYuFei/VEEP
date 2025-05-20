package com.example.api.Service;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.UserListResponse;
import com.example.api.Entity.Booth;
import com.example.api.Entity.User;
import com.example.api.Repository.BoothRepository;
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
public class MultipleBoothService {
    @Autowired
    private final BoothRepository boothRepository;

    @Autowired
    private final SingleBoothService singleBoothService;



    public List<BoothOverviewResponse> getAllBoothOverview() {
        System.out.println("BatchBoothService: getAllBoothOverview");
        return  boothRepository.findAll()
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public Page<BoothOverviewResponse> getAllBoothOverviewPage(Integer page, Integer size) {
        System.out.println("BatchBoothService: getAllBoothOverviewPage >> "+page+", "+size);
        Pageable pageable = PageRequest.of(page, size);
        return boothRepository.findAll(pageable).map(BoothOverviewResponse::fromBooth);
    }


    public List<UserListResponse> getAllCollaborator(Integer boothID){
        System.out.println("BatchBoothService: getAllCollaborator >> "+boothID);
        Booth booth = singleBoothService.getBoothByID(boothID);
        List<UserListResponse> response;

        Set<User> cols = booth.getCollaborator().getCollaborators();
        if(cols != null && !cols.isEmpty()) {
            response = cols.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<UserListResponse> getAllStaff(Integer boothID){
        System.out.println("BatchBoothService: getAllStaff >> "+boothID);
        Booth booth = singleBoothService.getBoothByID(boothID);
        List<UserListResponse> response;

        Set<User> stas = booth.getStaff().getStaffs();
        if(stas != null && !stas.isEmpty()) {
            response = stas.stream()
                    .map(UserListResponse::fromUser)
                    .toList();
        }else response = new ArrayList<>();

        return response;
    }


    public List<BoothOverviewResponse> getTagBoothOverview(String tags){
        System.out.println("BatchBoothService: getTagBoothOverview >> "+tags);
        if(tags == null) return new ArrayList<>();

        return boothRepository.findBoothsByTagsName(tags)
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }
}
