package com.example.api.Service;

import com.example.api.DTO.Request.ContentUpdateRequest;
import com.example.api.DTO.Response.ContentEditResponse;
import com.example.api.Entity.*;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.BoothRepository;
import com.example.api.Repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.api.Other.UpdateTool.updateIfNotBlank;

@Service
@RequiredArgsConstructor
public class ContentService {
    @Autowired
    private final ContentRepository contentRepository;

    @Autowired
    private final BoothRepository boothRepository;

    @Autowired
    private final ImageService imageService;



    Content getContentByBoothIDAndNumber(Integer boothID, Integer number) {
        System.out.println("ContentService: getContentByBoothAndNumber >> "+boothID+", "+number);
        boothRepository.findById(boothID)
                .orElseThrow(() -> new NotFoundException("找不到攤位ID為 < "+ boothID+" > 的攤位"));
        return contentRepository.findByBooth_BoothIDAndNumber(boothID, number)
                .orElseThrow(() -> new NotFoundException("在攤位ID為 < "+ boothID+" > 的攤位中，找不到內容編號為 < "+ number+" > 的內容"));
    }


    public List<Content> createDefaultContent(Booth booth){
        System.out.println("ContentService: createDefaultContent");

        List<Content> contentList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Content content = new Content();
            content.setNumber(i);
            content.setBooth(booth);
            contentList.add(content);
        }
        return contentRepository.saveAll(contentList);
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

        if(content.getImage() != null && request.getImage() != null){
            imageService.deleteImageByName(content.getImage());
        }

        content.setTitle(updateIfNotBlank(content.getTitle(), request.getTitle()));
        content.setContent(updateIfNotBlank(content.getContent(), request.getContent()));
        content.setImage(updateIfNotBlank(content.getImage(), request.getImage()));

        contentRepository.save(content);
    }
}
