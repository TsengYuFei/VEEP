package com.example.api.Repository;

import com.example.api.DTO.Response.MessageListView;
import com.example.api.Entity.UserMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    @Query(value = "SELECT m.* "+
            "FROM user_message m "+
            "WHERE (m.senderAccount = :userAccountA AND m.receiverAccount = :userAccountB) "+
            "OR (m.senderAccount = :userAccountB AND m.receiverAccount = :userAccountA) "
            ,nativeQuery = true)
    Page<UserMessage> findConversation(
            @Param("userAccountA") String userAccountA,
            @Param("userAccountB") String userAccountB,
            Pageable pageable);


    @Query(value = "SELECT count(*) "+
            "FROM user_message m "+
            "WHERE m.isRead = False "+
            "AND m.senderAccount = :targetAccount " +
            "AND m.receiverAccount = :currentAccount"
            , nativeQuery = true)
    Integer getUnreadCountByAccount(
            @Param("currentAccount") String currentAccount,
            @Param("targetAccount") String targetAccount);


    @Modifying
    @Transactional
    @Query(value = "UPDATE user_message "+
            "SET isRead = True "+
            "WHERE isRead = False "+
            "AND senderAccount = :targetAccount " +
            "AND receiverAccount = :currentAccount"
            , nativeQuery = true)
    int readUnreadByAccount(
            @Param("currentAccount") String currentAccount,
            @Param("targetAccount") String targetAccount);


    @Query(
        value = """
            SELECT
                CASE
                    WHEN m.senderAccount = :currentAccount THEN m.receiverAccount
                    ELSE m.senderAccount
                END AS targetAccount,
    
                CASE
                    WHEN m.senderAccount = :currentAccount THEN ru.name
                    ELSE su.name
                END AS targetName,
    
                CASE
                    WHEN m.senderAccount = :currentAccount THEN ru.avatar
                    ELSE su.avatar
                END AS targetAvatar,
    
                MAX(m.send_at) AS latestTime,
    
                SUBSTRING_INDEX(
                    SUBSTRING_INDEX(
                        GROUP_CONCAT(m.message ORDER BY m.send_at DESC SEPARATOR '||'), '||', 1
                    ), '||', -1
                ) AS latestMessage,
    
                SUM(CASE
                    WHEN m.isRead = false AND m.receiverAccount = :currentAccount THEN 1
                    ELSE 0
                END) AS unreadCount
    
            FROM user_message m
            JOIN user su ON m.senderAccount = su.userAccount
            JOIN user ru ON m.receiverAccount = ru.userAccount
    
            WHERE m.senderAccount = :currentAccount OR m.receiverAccount = :currentAccount
    
            GROUP BY targetAccount, targetName, targetAvatar
            ORDER BY latestTime DESC
            """,
                countQuery = """
            SELECT COUNT(*) FROM (
                SELECT 1
                FROM user_message m
                JOIN user su ON m.senderAccount = su.userAccount
                JOIN user ru ON m.receiverAccount = ru.userAccount
                WHERE m.senderAccount = :currentAccount OR m.receiverAccount = :currentAccount
                GROUP BY
                    CASE WHEN m.senderAccount = :currentAccount THEN m.receiverAccount ELSE m.senderAccount END,
                    CASE WHEN m.senderAccount = :currentAccount THEN ru.name ELSE su.name END,
                    CASE WHEN m.senderAccount = :currentAccount THEN ru.avatar ELSE su.avatar END
            ) AS count_table
        """,nativeQuery = true)
    Page<MessageListView> getChatList(
            @Param("currentAccount") String currentAccount,
            Pageable pageable
    );
}
