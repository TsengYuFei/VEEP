package com.example.api.Service;

import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.Repository.BoothRepository;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Entity.OpenMode;
import com.example.api.DTO.Request.BoothCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoothService {
    @Autowired
    private final BoothRepository boothRepository;

    public BoothService(BoothRepository boothRepository) {
        this.boothRepository = boothRepository;
    }

    public BoothEditResponse getBoothByID(Integer boothID){
        System.out.println("BoothService: getBoothByID >> "+boothID);

        BoothEditResponse booth = boothRepository.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find booth with booth ID < "+boothID+" >");
        return booth;
    }


    public Integer createBooth(BoothCreateRequest request){
        System.out.println("BoothService: createBooth");
        String avatar = request.getAvatar();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Can't create booth without status");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Can't create booth without open start/end");
        }

        return boothRepository.createBooth(request);
    }


    public void updateBoothByID(Integer boothID, BoothCreateRequest request){
        System.out.println("BoothService: updateBoothByID >> "+boothID);
        BoothEditResponse booth = boothRepository.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find an booth with ID < "+boothID+" >");

        String avatar = request.getAvatar();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Can't update booth without status");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Can't update booth without open start/end");
        }

        boothRepository.updateBoothByID(boothID, request);
    }


    public void deleteBoothByID(Integer boothID){
        System.out.println("BoothService: deleteBoothByID >> "+boothID);
        BoothEditResponse booth = boothRepository.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find an booth with ID < "+boothID+" >");

        boothRepository.deleteBoothByID(boothID);
    }
}
