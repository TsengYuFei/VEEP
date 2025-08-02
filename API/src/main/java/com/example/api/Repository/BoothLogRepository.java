package com.example.api.Repository;

import com.example.api.Entity.BoothLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoothLogRepository extends JpaRepository<BoothLog, Integer> {
    Optional<BoothLog> findBySessionID(String sessionID);
    void deleteByExpo_ExpoID(Integer expoExpoID);
    void deleteByBooth_BoothID(Integer boothBoothID);
    List<BoothLog> findExpoLogByExpo_ExpoID(Integer expoExpoID);
    List<BoothLog> findBoothLogByBooth_BoothID(Integer boothBoothID);
}
