package com.example.api.Repository;

import com.example.api.Entity.ContentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentLogRepository extends JpaRepository<ContentLog, Integer> {
    List<ContentLog> findContentLogByExpo_ExpoID(Integer expoExpoID);
    List<ContentLog> findContentLogByBooth_BoothID(Integer boothBoothID);
    List<ContentLog> findContentLogByBooth_BoothIDAndContent_Number(Integer boothBoothID, Integer contentNumber);
    void deleteByExpo_ExpoID(Integer expoExpoID);
    void deleteByBooth_BoothID(Integer boothBoothID);
    void deleteByBooth_BoothIDAndContent_Number(Integer boothBoothID, Integer contentNumber);
}
