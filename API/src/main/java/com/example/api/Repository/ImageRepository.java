package com.example.api.Repository;

import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {
    public void save(String filename) {
        System.out.println("儲存圖片名稱：" + filename);
    }
}
