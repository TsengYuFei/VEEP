package com.example.api.Repository;

import com.example.api.Entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Optional<Content> findByBooth_BoothIDAndNumber(Integer boothID, Integer number);
}
