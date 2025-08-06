package com.example.api.Repository;

import com.example.api.Entity.ExpoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpoLogRepository extends JpaRepository<ExpoLog, Integer> {
    Optional<ExpoLog> findBySessionID(String sessionID);
    List<ExpoLog> findExpoLogByExpo_ExpoID(Integer expoExpoID);

    List<ExpoLog> findExpoLogByUser_UserAccountOrderByEnterAtDesc(String userAccount);
    List<ExpoLog> findExpoLogByUser_UserAccountAndExpo_ExpoIDOrderByEnterAtDesc(String userAccount, Integer expoExpoID);
    List<ExpoLog> findExpoLogByExpo_ExpoIDAndExitAt(Integer expo_expoID, LocalDateTime exitAt);
    Integer countByExpo_ExpoIDAndExitAt(Integer expoExpoID, LocalDateTime exitAt);

    void deleteByExpo_ExpoID(Integer expoExpoID);
}
