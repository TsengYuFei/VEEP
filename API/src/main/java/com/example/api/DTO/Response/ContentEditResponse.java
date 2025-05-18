package com.example.api.DTO.Response;

import com.example.api.Entity.Content;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ContentEditResponse {
    private Integer boothID;
    private Integer number;
    private String title;
    private String content;
    private String image;

    public static ContentEditResponse fromContent(Content content) {
        ContentEditResponse response = new ContentEditResponse();
        response.setBoothID(content.getBooth().getBoothID());
        response.setNumber(content.getNumber());
        response.setTitle(content.getTitle());
        response.setContent(content.getContent());
        response.setImage(content.getImage());

        return response;
    }
}
