package com.example.api.Repository;

import com.example.api.Entity.Booth;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {
    @EntityGraph(attributePaths = {"collaborator", "collaborator.collaborators"})
    Optional<Booth> findById(Integer id);
}
