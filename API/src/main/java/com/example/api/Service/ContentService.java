package com.example.api.Service;

import com.example.api.DTO.Request.ContentUpdateRequest;
import com.example.api.DTO.Response.ContentEditResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;

@Service
@Repository
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;



    private Content getContentByBoothIDAndNumber(Integer boothID, Integer number) {
        System.out.println("ContentService: getContentByBoothAndNumber >> "+boothID+", "+number);
        return contentRepository.findByBooth_BoothIDAndNumber(boothID, number)
                .orElseThrow(() -> new NotFoundException("在攤位ID為 < "+ boothID+" > 的攤位中，找不到內容編號為 < "+ number+" > 的內容"));
    }


    public ContentEditResponse getContentEditByBoothIDAndNumber(Integer boothID, Integer number){
        System.out.println("ContentService: getContentEditByBoothIDAndNumber >> "+boothID+", "+number);
        Content content = getContentByBoothIDAndNumber(boothID, number);
        return ContentEditResponse.fromContent(content);
    }


    @Transactional
    public void updateContentByBoothIDAndNumber(Integer boothID, Integer number, ContentUpdateRequest request){
        System.out.println("ContentService: updateContentByBoothIDAndNumber >> "+boothID+", "+number);

        Content content = getContentByBoothIDAndNumber(boothID, number);
        content.setTitle(updateIfNotBlank(content.getTitle(), request.getTitle()));
        content.setContent(updateIfNotBlank(content.getContent(), request.getContent()));
        content.setImage(updateIfNotBlank(content.getImage(), request.getImage()));

        contentRepository.save(content);
    }
}
