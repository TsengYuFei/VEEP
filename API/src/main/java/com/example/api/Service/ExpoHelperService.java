package com.example.api.Service;

import com.example.api.Entity.Expo;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ExpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpoHelperService {
    @Autowired
    private final ExpoRepository expoRepository;


    Expo getExpoByID(Integer expoID){
        System.out.println("ExpoHelperService: getExpoByID >> "+expoID);
        return expoRepository.findById(expoID)
                .orElseThrow(() -> new NotFoundException("找不到展會ID為 < "+ expoID+" > 的展會"));
    }
}
