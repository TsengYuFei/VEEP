package com.example.api.Service;

import com.example.api.Repository.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhiteListService {
    private final WhitelistRepository whitelistRepository;



    Boolean existInList(Integer listID, String account){
        return whitelistRepository.existsByIdAndWhitelistedUsers_UserAccount(listID, account);
    }
}
