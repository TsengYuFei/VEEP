package com.example.api.Repository;

import com.example.api.Entity.CollaboratorList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorListRepository extends JpaRepository<CollaboratorList, Integer> { }
