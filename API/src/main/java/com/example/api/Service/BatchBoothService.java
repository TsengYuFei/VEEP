package com.example.api.Service;

import com.example.api.DTO.Response.BoothOverviewReaponse;
import com.example.api.Entity.Booth;
import com.example.api.Repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchBoothService {
    @Autowired
    private final BoothRepository boothRepository;



    public List<BoothOverviewReaponse> getAllBoothOverview() {
        System.out.println("BatchBoothService: getAllBoothOverview");
        List<Booth> booths =  boothRepository.findAll();
        List<BoothOverviewReaponse> response = new ArrayList<>();
        for(Booth b : booths) {
            BoothOverviewReaponse booth = BoothOverviewReaponse.fromBooth(b);

            if(b.getExpo() == null) booth.setExpoID(null);
            else booth.setExpoID(b.getExpo().getExpoID());

            response.add(booth);
        }
        return response;
    }
}
