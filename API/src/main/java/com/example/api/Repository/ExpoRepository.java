package com.example.api.Repository;

import com.example.api.Entity.Expo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpoRepository extends JpaRepository<Expo, Integer> {
    @EntityGraph(attributePaths = {"collaborator", "collaborator.collaborators"})
    Optional<Expo> findById(@NotNull Integer id);
}
