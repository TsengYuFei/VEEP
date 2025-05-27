package com.example.api.Service;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.Repository.BoothRepository;
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
public class MultipleBoothService {
    @Autowired
    private final BoothRepository boothRepository;



    public List<BoothOverviewResponse> getAllBoothOverview() {
        System.out.println("MultipleBoothService: getAllBoothOverview");
        return  boothRepository.findAll()
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public Page<BoothOverviewResponse> getAllBoothOverviewPage(Integer page, Integer size) {
        System.out.println("MultipleBoothService: getAllBoothOverviewPage >> "+page+", "+size);
        Pageable pageable = PageRequest.of(page, size);
        return boothRepository.findAll(pageable).map(BoothOverviewResponse::fromBooth);
    }


    public List<BoothOverviewResponse> getBoothOverviewByTag(String tag){
        System.out.println("MultipleBoothService: getBoothOverviewByTag >> "+tag);
        if(tag == null) return new ArrayList<>();

        return boothRepository.findBoothsByTagsName(tag)
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public List<BoothOverviewResponse> getBoothOverviewByNameAndIntro(String keyword){
        System.out.println("MultipleBoothService: getBoothOverviewByNameAndIntro >> "+keyword);
        if(keyword == null) return new ArrayList<>();

        return boothRepository.findBoothsByNameAndIntro(keyword)
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }
}
