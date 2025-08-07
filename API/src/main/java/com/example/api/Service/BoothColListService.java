package com.example.api.Service;

import com.example.api.Repository.BoothCollaboratorListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothColListService {
    private final BoothCollaboratorListRepository colListRepository;



    Boolean existInList(Integer listID, String account){
        return colListRepository.existsByIdAndCollaborators_UserAccount(listID, account);
    }
}
