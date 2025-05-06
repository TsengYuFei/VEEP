package com.example.api.Service;

import com.example.api.DTO.Request.ExpoCreateRequest;
import com.example.api.DTO.Request.ExpoUpdateRequest;
import com.example.api.DTO.Response.ExpoEditResponse;
import com.example.api.Entity.Expo;
import com.example.api.Entity.OpenMode;
import com.example.api.Exception.NotFoundException;
import com.example.api.Exception.UnprocessableEntityException;
import com.example.api.Repository.ExpoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;
import static com.example.api.Other.UpdateTool.updateIfNotNull;

@Service
@RequiredArgsConstructor
public class ExpoService {
    @Autowired
    private final ExpoRepository expoRepository;

    @Autowired
    private final ModelMapper modelMapper;



    private Expo getExpoByID(Integer expoID){
        System.out.println("ExpoService: getExpoByID >> "+expoID);
        return expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
    }


    public ExpoEditResponse getExpoEditByID(Integer expoID) {
        System.out.println("ExpoService: getExpoEditByID >> "+expoID);
        Expo expo = expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
        return modelMapper.map(expo, ExpoEditResponse.class);
    }


    public Integer createExpo(ExpoCreateRequest request) {
        System.out.println("ExpoService: createExpo");
        request.setAvatar(updateIfNotBlank(null, request.getAvatar()));
        request.setIntroduction(updateIfNotBlank(null, request.getIntroduction()));
        request.setAccessCode(updateIfNotBlank(null, request.getAvatar()));

        Boolean status = request.getOpenStatus();
        OpenMode mode = request.getOpenMode();
        if(mode == OpenMode.MANUAL && status == null){
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open status 不可為空");
        }else if (mode == OpenMode.AUTO && (request.getOpenStart() == null || request.getOpenEnd() == null)) {
            throw new UnprocessableEntityException("Open mode 為 MANUAL 時，open start/end 不可為空");
        }

        Expo expo = modelMapper.map(request, Expo.class);
        expoRepository.save(expo);
        return expo.getExpoID();
    }


    public void updateExpoByID(Integer expoID, ExpoUpdateRequest request){
        System.out.println("ExpoService: updateExpoByID >> "+expoID);
        Expo expo = getExpoByID(expoID);

        expo.setName(updateIfNotBlank(expo.getName(), request.getName()));
        expo.setAvatar(updateIfNotBlank(expo.getAvatar(), request.getAvatar()));
        expo.setPrice(updateIfNotNull(expo.getPrice(), request.getPrice()));
        expo.setIntroduction(updateIfNotBlank(expo.getIntroduction(), request.getIntroduction()));
        expo.setOpenMode(updateIfNotNull(expo.getOpenMode(), request.getOpenMode()));
        expo.setOpenStatus(updateIfNotNull(expo.getOpenStatus(), request.getOpenStatus()));
        expo.setOpenStart(updateIfNotNull(expo.getOpenStart(), request.getOpenStart()));
        expo.setOpenEnd(updateIfNotNull(expo.getOpenEnd(), request.getOpenEnd()));
        expo.setAccessCode(updateIfNotBlank(expo.getAccessCode(), request.getAccessCode()));
        expo.setMaxParticipants(updateIfNotNull(expo.getMaxParticipants(), request.getMaxParticipants()));
        expo.setDisplay(updateIfNotNull(expo.getDisplay(), request.getDisplay()));

        expoRepository.save(expo);
    }


    public void deleteExpoByID(Integer expoID){
        System.out.println("ExpoService: deleteExpoByID >> "+expoID);
        Expo expo = getExpoByID(expoID);
        expoRepository.delete(expo);
    }
}
