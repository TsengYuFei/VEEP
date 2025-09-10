package com.example.api.Repository;

import com.example.api.Entity.ExpoGroupMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpoGroupMessageRepository extends JpaRepository<ExpoGroupMessage, Integer> {
    @Query(value = "SELECT m.* "+
            "FROM expo_group_message m "+
            "WHERE (m.expoID = :expoID) "
            ,nativeQuery = true)
    Page<ExpoGroupMessage> findConversation(
            @Param("expoID") Integer expoID,
            Pageable pageable);

    void deleteByExpo_ExpoID(Integer expoExpoID);
}
