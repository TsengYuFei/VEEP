package com.example.api.Repository;

import com.example.api.Entity.ExpoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpoLogRepository extends JpaRepository<ExpoLog, Integer> {
}
