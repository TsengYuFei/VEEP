package com.example.api.Repository;

import com.example.api.Entity.ContentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentLogRepository extends JpaRepository<ContentLog, Integer> {
    List<ContentLog> findContentLogByExpo_ExpoID(Integer expoExpoID);
    List<ContentLog> findContentLogByBooth_BoothID(Integer boothBoothID);
    List<ContentLog> findContentLogByBooth_BoothIDAndContent_Number(Integer boothBoothID, Integer contentNumber);

    List<ContentLog> findContentLogByUser_UserAccount(String userAccount);
    List<ContentLog> findContentLogByUser_UserAccountAndExpo_ExpoID(String userAccount, Integer expoExpoID);
    List<ContentLog> findContentLogByUser_UserAccountAndBooth_BoothID(String userAccount, Integer boothBoothID);
    List<ContentLog> findContentLogByUser_UserAccountAndBooth_BoothIDAndContent_Number(String userAccount, Integer boothBoothID, Integer contentNumber);

    void deleteByExpo_ExpoID(Integer expoExpoID);
    void deleteByBooth_BoothID(Integer boothBoothID);
    void deleteByBooth_BoothIDAndContent_Number(Integer boothBoothID, Integer contentNumber);


    @Query("""
        SELECT COUNT(DISTINCT b.id)
        FROM BoothLog b
        WHERE b.booth.boothID = :boothID
          AND b.user.userAccount = :account
          AND b.enterAt BETWEEN :start AND :end
    """)
    Integer countDailyByBoothAndUser(
            @Param("boothID") Integer boothID,
            @Param("account") String account,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
