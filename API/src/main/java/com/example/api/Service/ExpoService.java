package com.example.api.Service;

import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Repository.ExpoRepository;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Entity.OpenMode;
import com.example.api.DTO.Request.ExpoCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    public ExpoService(ExpoRepository expoRepository) {
        this.expoRepository = expoRepository;
    }

    public ExpoEditResponse getExpoEditByID(Integer expoID){
        System.out.println("ExpoService: getExpoEditByID >> "+expoID);
        ExpoEditResponse expo = expoRepository.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find expo with expo ID < "+expoID+" >");
        return expo;
    }


    public Integer createExpo(ExpoCreateRequest request){
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

        return expoRepository.createExpo(request);
    }


    public void updateExpoByID(Integer expoID, ExpoCreateRequest request){
        System.out.println("ExpoService: updateExpoByID >> "+expoID);
        ExpoEditResponse expo = expoRepository.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find an expo with ID < "+expoID+" >");

        String avatar = request.getAvatar();
        String code = request.getAccessCode();
        if(avatar != null && avatar.isBlank()) request.setAvatar(null);
        if(code != null && code.isBlank()) request.setAccessCode(null);

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Can't update expo without status");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Can't update expo without open start/end");
        }

        expoRepository.updateExpoByID(expoID, request);
    }


    public void deleteExpoByID(Integer expoID){
        System.out.println("ExpoService: deleteExpoByID >> "+expoID);
        ExpoEditResponse expo = expoRepository.getExpoEditByID(expoID);
        if(expo == null) throw new NotFoundException("Can't find an expo with ID < \"+expoID+\" >\")");
        expoRepository.deleteExpoByID(expoID);
    }
}
