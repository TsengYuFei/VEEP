package com.example.api.Service;

import com.example.api.Repository.BoothStaffListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothStaffListService {
    @Autowired
    private final BoothStaffListRepository staffListRepository;

    Boolean existInList(Integer listID, String account){
        return staffListRepository.existsByIdAndStaffs_UserAccount(listID, account);
    }
}
