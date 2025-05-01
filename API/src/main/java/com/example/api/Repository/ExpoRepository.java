package com.example.api.Repository;

import com.example.api.Entity.Expo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpoRepository extends JpaRepository<Expo, Integer> { }
