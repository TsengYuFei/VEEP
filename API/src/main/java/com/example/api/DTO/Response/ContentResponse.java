package com.example.api.DTO.Response;

import com.example.api.Entity.Content;
import lombok.Data;

@Data
public class ContentResponse {
    private Integer boothID;
    private Integer number;
    private String title;
    private String content;
    private String image;

    public static ContentResponse fromContent(Content content) {
        ContentResponse response = new ContentResponse();
        response.setBoothID(content.getBooth().getBoothID());
        response.setNumber(content.getNumber());
        response.setTitle(content.getTitle());
        response.setContent(content.getContent());
        response.setImage(content.getImage());

        return response;
    }
}
