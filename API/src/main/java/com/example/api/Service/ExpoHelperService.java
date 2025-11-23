package com.example.api.Service;

import com.example.api.Entity.Expo;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.ExpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpoHelperService {
    private final ExpoRepository expoRepository;
    private final BoothRepository boothRepository;



    public Expo getExpoByID(Integer expoID){
        System.out.println("ExpoHelperService: getExpoByID >> "+expoID);
        return expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
    }

    public Integer getBoothNumber(Integer expoID){
        System.out.println("ExpoHelperService: getBoothNumber >> "+expoID);

        return boothRepository.countByExpo_ExpoID(expoID);
    }
}
