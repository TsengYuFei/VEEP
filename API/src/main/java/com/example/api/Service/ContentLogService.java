package com.example.api.Service;

import com.example.api.DTO.Response.LogCreateResponse;
import com.example.api.Entity.Content;
import com.example.api.Repository.ContentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentLogService {
    @Autowired
    private final ContentLogRepository contentLogRepository;

    @Autowired
    private final ContentService contentService;


//    @Transactional
//    public LogCreateResponse createContentLogByContentID(Integer contentID, String account) {
//        System.out.println("ContentLogService: createContentLogByContentID >> "+contentID+", "+account);
//    }
}
