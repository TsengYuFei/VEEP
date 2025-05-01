package com.example.api.Service;

import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.Booth;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.BoothRepositoryNew;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
