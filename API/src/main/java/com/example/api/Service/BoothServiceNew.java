package com.example.api.Service;

import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.Booth;
import com.example.api.Entity.Expo;
import com.example.api.Entity.OpenMode;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.BoothRepositoryNew;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;

@Service
public class BoothServiceNew {
    @Autowired
    private final BoothRepositoryNew boothRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public BoothServiceNew(BoothRepositoryNew boothRepository, ModelMapper modelMapper) {
        this.boothRepository = boothRepository;
        this.modelMapper = modelMapper;
    }



    public BoothEditResponse getBoothEditByID(Integer boothID) {
        System.out.println("BoothServiceNew: getBoothEditByID >> "+boothID);
        Booth booth = boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的展會"));
        return modelMapper.map(booth, BoothEditResponse.class);
    }


    public Integer createBooth(BoothCreateRequest request) {
        System.out.println("BoothServiceNew: createBooth");
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
        boothRepository.save(booth);
        return booth.getBoothID();
    }
}
