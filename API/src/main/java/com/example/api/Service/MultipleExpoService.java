package com.example.api.Service;

import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.Repository.ExpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MultipleExpoService {
    @Autowired
    private final ExpoRepository expoRepository;



    public List<ExpoOverviewResponse> getAllExpoOverview() {
        System.out.println("MultipleExpoService: getAllExpoOverview");
        return  expoRepository.findAll()
                .stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }


    public Page<ExpoOverviewResponse> getAllExpoOverviewPage(Integer page, Integer size) {
        System.out.println("MultipleExpoService: getAllExpoOverviewPage >> "+page+", "+size);
        Pageable pageable = PageRequest.of(page, size);
        return expoRepository.findAll(pageable).map(ExpoOverviewResponse::fromExpo);
    }


    public List<ExpoOverviewResponse> getExpoOverviewByTag(String tag){
        System.out.println("MultipleExpoService: getExpoOverviewByTag >> "+tag);
        if(tag == null) return new ArrayList<>();

        return expoRepository.findExposByTagName(tag)
                .stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }


    public List<ExpoOverviewResponse> getExpoOverviewByNameAndIntro(String keyword){
        System.out.println("MultipleExpoService: getExpoOverviewByNameAndIntro >> "+keyword);
        if(keyword == null) return new ArrayList<>();

        return expoRepository.findExposByNameAndIntro(keyword)
                .stream()
                .map(ExpoOverviewResponse::fromExpo)
                .toList();
    }
}
