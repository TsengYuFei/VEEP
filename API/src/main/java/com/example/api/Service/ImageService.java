package com.example.api.Service;

import com.example.api.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Autowired
    private final ImageRepository imageRepository;

    @Value("${upload.dir}")
    private String uploadDir;



    public Map<String, String> uploadImage(MultipartFile file) {
        String absoluteUploadPath = Paths.get(uploadDir).toAbsolutePath().toString();
        File dir = new File(absoluteUploadPath);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + ".jpg";
        File saveFile = new File(dir, fileName);

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException("圖片儲存失敗", e);
        }

        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("url", "/uploads/" + fileName);
        return response;
    }
}
