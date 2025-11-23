package com.example.api.Repository;

import com.example.api.Entity.BoothLog;
import com.example.api.Entity.ExpoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("""
       SELECT b 
       FROM BoothLog b 
       WHERE b.expo.expoID = :expoID 
       AND b.enterAt BETWEEN :start AND :end
    """)
    List<BoothLog> findByExpoIDAndEnterAtBetween(
            @Param("expoID") Integer expoID,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
        SELECT COUNT(DISTINCT e.id)
        FROM ExpoLog e
        WHERE e.expo.expoID = :expoID
          AND e.user.userAccount = :account
          AND e.enterAt BETWEEN :start AND :end
    """)
    Integer countDailyByExpoAndUser(
            @Param("expoID") Integer expoID,
            @Param("account") String account,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT COUNT(b)
        FROM BoothLog b
        WHERE b.booth.boothID = :boothID
    """)
    Integer countLogs(@Param("boothID") Integer boothID);


    @Query("""
        SELECT COUNT(b)
        FROM BoothLog b
        WHERE b.booth.boothID = :boothID
          AND b.enterAt BETWEEN :start AND :end
    """)
    Integer countDailyLogs(
            @Param("boothID") Integer boothID,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    @Query("""
       SELECT b
       FROM BoothLog b 
       WHERE b.booth.boothID = :boothID 
       AND b.enterAt BETWEEN :start AND :end
    """)
    List<BoothLog> findByBoothIDAndEnterAtBetween(
            @Param("boothID") Integer boothID,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
