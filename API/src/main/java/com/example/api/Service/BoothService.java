package com.example.api.Service;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.Dao.BoothDao;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Model.OpenMode;
import com.example.api.Request.BoothRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoothService {
    @Autowired
    private BoothDao boothDao;

    public BoothEditDTO getBoothByID(Integer boothID){
        System.out.println("BoothService: getBoothByID >> "+boothID);

        BoothEditDTO booth = boothDao.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find booth with booth ID < "+boothID+" >");
        return booth;
    }


    public Integer createBooth(BoothRequest request){
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

        return boothDao.createBooth(request);
    }


    public void updateBoothByID(Integer boothID, BoothRequest request){
        System.out.println("BoothService: updateBoothByID >> "+boothID);
        BoothEditDTO booth = boothDao.getBoothByID(boothID);
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

        boothDao.updateBoothByID(boothID, request);
    }


    public void deleteBoothByID(Integer boothID){
        System.out.println("BoothService: deleteBoothByID >> "+boothID);
        BoothEditDTO booth = boothDao.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find an booth with ID < "+boothID+" >");

        boothDao.deleteBoothByID(boothID);
    }
}
