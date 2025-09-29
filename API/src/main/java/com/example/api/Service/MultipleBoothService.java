package com.example.api.Service;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.DTO.Response.ExpoHotResponse;
import com.example.api.DTO.Response.ExpoOverviewResponse;
import com.example.api.Entity.Booth;
import com.example.api.Entity.Expo;
import com.example.api.Repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MultipleBoothService {
    private final BoothRepository boothRepository;
    private final SingleBoothService singleBoothService;
    private final BoothLogService boothLogService;



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


    public List<BoothOverviewResponse> getDisplayBoothOverview(){
        System.out.println("MultipleBoothService: getDisplayBoothOverview");
        return  boothRepository.findBoothsAreDisplay()
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();
    }


    public Page<BoothOverviewResponse> getDisplayBoothOverviewPage(Integer page, Integer size){
        System.out.println("MultipleBoothService: getDisplayBoothOverviewPage");
        Pageable pageable = PageRequest.of(page, size);

        List<BoothOverviewResponse> booths =  boothRepository.findBoothsAreDisplay()
                .stream()
                .map(BoothOverviewResponse::fromBooth)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), booths.size());
        List<BoothOverviewResponse> pageContent = booths.subList(start, end);

        return new PageImpl<>(pageContent, pageable, booths.size());
    }


    public Page<BoothOverviewResponse> getHottestBoothOverviewPage(Integer page, Integer size){
        System.out.println("MultipleBoothService: getHottestBoothOverviewPage >> "+page+", "+size);
        Pageable pageable = PageRequest.of(page, size);

        List<Map.Entry<Booth, Integer>> boothWithOnline = boothRepository.findBoothsAreDisplay()
                .stream()
                .map(booth -> Map.entry(booth, boothLogService.getOnlineNumberByBoothID(booth.getBoothID())))
                // .filter(entry -> entry.getValue() > 0)  // //如果不想要回傳在線人數=0時加這行
                .sorted(Map.Entry.<Booth, Integer>comparingByValue().reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), boothWithOnline.size());

        List<BoothOverviewResponse> pageContent = boothWithOnline.subList(start, end)
                .stream()
                .map(entry -> {
                    Booth booth = entry.getKey();
                    return BoothOverviewResponse.fromBooth(booth);
                })
                .toList();

        return new PageImpl<>(pageContent, pageable, boothWithOnline.size());
    }
}
