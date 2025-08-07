package com.example.api.Service;

import com.example.api.Entity.Booth;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothHelperService {
    private final BoothRepository boothRepository;



    Booth getBoothByID(Integer boothID) {
        System.out.println("BoothHelperService: getBoothByID >> "+boothID);
        return boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));
    }
}
