package com.example.api.Controller;

import com.example.api.Service.ContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "攤位內容Log相關")
@RequestMapping("/log/content")
@RestController
@RequiredArgsConstructor
public class ContentLogController {
    @Autowired
    private final ContentService contentService;



}
