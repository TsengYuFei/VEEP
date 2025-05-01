package com.example.api.Service;

import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.Expo;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ExpoRepositoryNew;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpoServiceNew {
    @Autowired
    private final ExpoRepositoryNew expoRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public ExpoServiceNew(ExpoRepositoryNew expoRepository, ModelMapper modelMapper) {
        this.expoRepository = expoRepository;
        this.modelMapper = modelMapper;
    }



    public ExpoEditResponse getExpoEditByID(Integer expoID) {
        System.out.println("ExpoServiceNew: getExpoEditByID >> "+expoID);
        Expo expo = expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
        return modelMapper.map(expo, ExpoEditResponse.class);
    }
}
