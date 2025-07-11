package com.example.api.Repository;

import com.example.api.Entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT m.* "+
            "FROM message m "+
            "WHERE (m.senderAccount = :userAccountA AND m.receiverAccount = :userAccountB) "+
            "OR (m.senderAccount = :userAccountB AND m.receiverAccount = :userAccountA) "
            ,nativeQuery = true)
    Page<Message> findConversation(
            @Param("userAccountA") String userAccountA,
            @Param("userAccountB") String userAccountB,
            Pageable pageable);

    @Query(value = "SELECT count(*) "+
            "FROM message m "+
            "WHERE m.isRead = False "+
            "AND m.senderAccount = :targetAccount " +
            "AND m.receiverAccount = :currentAccount"
            , nativeQuery = true)
    Integer getUnreadCountByAccount(
            @Param("currentAccount") String currentAccount,
            @Param("targetAccount") String targetAccount);
}
