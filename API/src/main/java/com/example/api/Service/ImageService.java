package com.example.api.Service;

import com.example.api.DTO.Response.ImageUploadResponse;
import com.example.api.Exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${upload.dir}")
    private String uploadDir;



    public ImageUploadResponse uploadImage(MultipartFile image) {
        System.out.println("ImageService: uploadImage");
        if (image.getSize() > 5 * 1024 * 1024) throw new UnprocessableEntityException("圖片大小不得超過 5MB");


        String absoluteUploadPath = Paths.get(uploadDir).toAbsolutePath().toString();
        File dir = new File(absoluteUploadPath);
        if (!dir.exists()) dir.mkdirs();

        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        } else throw new UnprocessableEntityException("檔案缺少副檔名");
        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
            throw new UnprocessableEntityException("只能上傳 .jpg 或 .png 類型的圖片");
        }

        String imageName = UUID.randomUUID() + extension;
        File saveFile = new File(dir, imageName);

        try {
            image.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException("圖片儲存失敗", e);
        }

        ImageUploadResponse response = new ImageUploadResponse();
        response.setName(imageName);
        response.setUrl("/uploads/" + imageName);
        return response;
    }
}
