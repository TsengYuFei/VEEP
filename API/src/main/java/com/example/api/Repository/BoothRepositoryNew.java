package com.example.api.Repository;

import com.example.api.Entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepositoryNew extends JpaRepository<Booth, Integer> { }
