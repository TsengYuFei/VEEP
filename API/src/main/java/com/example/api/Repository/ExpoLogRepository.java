package com.example.api.Repository;

import com.example.api.Entity.ExpoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpoLogRepository extends JpaRepository<ExpoLog, Integer> {
    Optional<ExpoLog> findBySessionID(String sessionID);
}
