package com.example.api.Service;

import com.example.api.Repository.ExpoCollaboratorListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpoColListService {
    @Autowired
    private final ExpoCollaboratorListRepository colListRepository;

    Boolean existInList(Integer listID, String account){
        return colListRepository.existsByIdAndCollaborators_UserAccount(listID, account);
    }
}
