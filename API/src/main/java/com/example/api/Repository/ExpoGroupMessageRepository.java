package com.example.api.Repository;

import com.example.api.Entity.ExpoGroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpoGroupMessageRepository extends JpaRepository<ExpoGroupMessage, Integer> {
}
