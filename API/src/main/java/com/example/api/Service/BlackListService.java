package com.example.api.Service;

import com.example.api.Repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {
    @Autowired
    private final BlacklistRepository blacklistRepository;

    Boolean existInList(Integer listID, String account){
        return blacklistRepository.existsByIdAndBlacklistedUsers_UserAccount(listID, account);
    }
}
