package com.example.api.Repository;

import com.example.api.Entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    boolean existsByIdAndBlacklistedUsers_UserAccount(Integer id, String userAccount);
}
