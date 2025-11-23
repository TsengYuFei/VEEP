package com.example.api.Repository;

import com.example.api.Entity.ExpoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT COUNT(e)
        FROM ExpoLog e
        WHERE e.expo.expoID = :expoID
    """)
    Integer countLogs(@Param("expoID") Integer expoID);


    @Query("""
        SELECT COUNT(e)
        FROM ExpoLog e
        WHERE e.expo.expoID = :expoID
          AND e.enterAt BETWEEN :start AND :end
    """)
    Integer countDailyLogs(
            @Param("expoID") Integer expoID,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    @Query("""
       SELECT e 
       FROM ExpoLog e 
       WHERE e.expo.expoID = :expoID 
       AND e.enterAt BETWEEN :start AND :end
    """)
    List<ExpoLog> findByExpoIDAndEnterAtBetween(
            @Param("expoID") Integer expoID,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
