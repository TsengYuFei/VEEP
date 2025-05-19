package com.example.api.Service;

import com.example.api.DTO.Response.BoothOverviewResponse;
import com.example.api.Entity.Booth;
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
public class BatchBoothService {
    @Autowired
    private final BoothRepository boothRepository;



    public List<BoothOverviewResponse> getAllBoothOverview() {
        System.out.println("BatchBoothService: getAllBoothOverview");
        List<Booth> booths =  boothRepository.findAll();
        List<BoothOverviewResponse> response = new ArrayList<>();
        for(Booth b : booths) {
            BoothOverviewResponse booth = BoothOverviewResponse.fromBooth(b);

            if(b.getExpo() == null) booth.setExpoID(null);
            else booth.setExpoID(b.getExpo().getExpoID());

            response.add(booth);
        }
        return response;
    }


    public Page<BoothOverviewResponse> getAllBoothOverviewPage(Integer page, Integer size) {
        System.out.println("BatchBoothService: getAllBoothOverviewPage");
        Pageable pageable = PageRequest.of(page, size);
        Page<BoothOverviewResponse> boothPage = boothRepository.findAll(pageable)
                .map(BoothOverviewResponse::fromBooth);
        return boothPage;
    }
}
