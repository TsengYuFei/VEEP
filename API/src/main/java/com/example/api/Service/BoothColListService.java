package com.example.api.Service;

import com.example.api.Repository.BoothCollaboratorListRepository;
import com.example.api.Repository.BoothStaffListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothColListService {
    @Autowired
    private final BoothCollaboratorListRepository colListRepository;

    Boolean existInList(Integer listID, String account){
        return colListRepository.existsByIdAndCollaborators_UserAccount(listID, account);
    }
}
