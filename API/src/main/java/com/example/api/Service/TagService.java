package com.example.api.Service;

import com.example.api.Entity.Tag;
import com.example.api.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    @Autowired
    private final TagRepository tagRepository;

    Tag getTagByName(String name){
        return tagRepository.findByName(name);
    }

    Tag addTagIfNotExist(String name){
        Tag tag = getTagByName(name);
        if(tag == null) tag = tagRepository.save(new Tag(name));
        return tag;
    }
}
