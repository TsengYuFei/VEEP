package com.example.api.Service;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.Dao.BoothDao;
import com.example.api.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoothService {
    @Autowired
    private BoothDao boothDao;

    public BoothEditDTO getBoothByID(Integer boothID){
        System.out.println("BoothService: getBoothByID >> "+boothID);

        BoothEditDTO booth = boothDao.getBoothByID(boothID);
        if(booth == null) throw new NotFoundException("Can't find expo with expo ID < "+boothID+" >");
        return booth;
    }
}
