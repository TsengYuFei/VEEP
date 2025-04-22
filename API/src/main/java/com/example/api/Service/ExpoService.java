package com.example.api.Service;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Dao.ExpoDao;
import com.example.api.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpoService {
    @Autowired
    private ExpoDao expoDao;

    public ExpoEditDTO getExpoEditByID(int expoID){
        System.out.println("ExpoService: getExpoEditByID >> "+expoID);
        ExpoEditDTO expo = expoDao.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find expo with expo ID < "+expoID+" >");
        return expo;
    }
}
