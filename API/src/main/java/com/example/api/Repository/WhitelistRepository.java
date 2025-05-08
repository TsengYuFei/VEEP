package com.example.api.Repository;

import com.example.api.Entity.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, Integer> {
    boolean existsByIdAndWhitelistedUsers_UserAccount(Integer id, String userAccount);
}
