package com.example.api.Repository;

import com.example.api.Entity.BoothLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoothLogRepository extends JpaRepository<BoothLog, Integer> {
    Optional<BoothLog> findBySessionID(String sessionID);
    List<BoothLog> findExpoLogByExpo_ExpoID(Integer expoExpoID);
    List<BoothLog> findBoothLogByBooth_BoothID(Integer boothBoothID);

    List<BoothLog> findBoothLogByUser_UserAccountOrderByEnterAtDesc(String userAccount);
    List<BoothLog> findBoothLogByUser_UserAccountAndExpo_ExpoIDOrderByEnterAtDesc(String userAccount, Integer expoExpoID);
    List<BoothLog> findBoothLogByUser_UserAccountAndBooth_BoothIDOrderByEnterAtDesc(String userAccount, Integer boothBoothID);

    List<BoothLog> findBoothLogByExpo_ExpoIDAndExitAt(Integer expoExpoID, LocalDateTime exitAt);
    List<BoothLog> findBoothLogByBooth_BoothIDAndExitAt(Integer boothBoothID, LocalDateTime exitAt);

    Integer countByBooth_BoothIDAndExitAt(Integer boothBoothID, LocalDateTime exitAt);

    void deleteByExpo_ExpoID(Integer expoExpoID);
    void deleteByBooth_BoothID(Integer boothBoothID);
}
