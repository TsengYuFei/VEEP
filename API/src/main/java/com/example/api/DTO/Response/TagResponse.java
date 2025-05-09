package com.example.api.DTO.Response;

import com.example.api.Entity.Tag;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TagResponse {
    private String name;

    public static TagResponse fromTag(Tag tag) {
        TagResponse response = new TagResponse();
        response.setName(tag.getName());
        return response;
    }
}
