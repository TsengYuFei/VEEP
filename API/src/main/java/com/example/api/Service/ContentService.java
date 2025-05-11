package com.example.api.Service;

import com.example.api.DTO.Response.ContentEditResponse;
import com.example.api.Entity.Content;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;



    private Content getContentByBoothIDAndNumber(Integer boothID, Integer number) {
        System.out.println("ContentService: getContentByBoothAndNumber >> "+boothID+", "+number);
        return contentRepository.findByBoothIDAndNumber(boothID, number)
                .orElseThrow(() -> new NotFoundException("在攤位ID為 < "+ boothID+" > 的攤位中，找不到內容編號為 < "+ number+" > 的內容"));
    }


    public ContentEditResponse getContentEditByBoothIDAndNumber(Integer boothID, Integer number){
        System.out.println("ContentService: getContentEditByBoothIDAndNumber >> "+boothID+", "+number);
        Content content = getContentByBoothIDAndNumber(boothID, number);
        return ContentEditResponse.fromContent(content);
    }
}
