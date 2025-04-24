package com.example.api.Service;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Dao.ExpoDao;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Model.OpenMode;
import com.example.api.Request.ExpoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpoService {
    @Autowired
    private ExpoDao expoDao;

    public ExpoEditDTO getExpoEditByID(Integer expoID){
        System.out.println("ExpoService: getExpoEditByID >> "+expoID);
        ExpoEditDTO expo = expoDao.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find expo with expo ID < "+expoID+" >");
        return expo;
    }


    public Integer createExpo(ExpoRequest request){
        System.out.println("ExpoService: createExpo");
        String avatar = request.getAvatar();
        String code = request.getAccessCode();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);
        if(code != null && code.isBlank()) request.setAccessCode(null);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Can't create expo without status");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Can't create expo without open start/end");
        }

        return expoDao.createExpo(request);
    }

    public void updateExpoByID(Integer expoID, ExpoRequest request){
        System.out.println("ExpoService: updateExpoByID >> "+expoID);
        ExpoEditDTO expo = expoDao.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find an expo with ID < \"+expoID+\" >\")");

        String avatar = request.getAvatar();
        String code = request.getAccessCode();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);
        if(code != null && code.isBlank()) request.setAccessCode(null);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Can't create expo without status");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Can't create expo without open start/end");
        }

        expoDao.updateExpoByID(expoID, request);
    }
}
