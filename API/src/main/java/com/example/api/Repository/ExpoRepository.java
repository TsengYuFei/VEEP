package com.example.api.Repository;

import com.example.api.Entity.Expo;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpoRepository extends JpaRepository<Expo, Integer> {
    @EntityGraph(attributePaths = {"collaborator", "collaborator.collaborators"})
    Optional<Expo> findById(@NotNull Integer id);

    @Query(value = "SELECT expo.* " +
            "FROM expo expo " +
            "JOIN expo_tag et ON expo.expoID = et.expoID " +
            "JOIN tag tag ON et.tagID = tag.tagID " +
            "WHERE LOWER(tag.name) LIKE CONCAT ('%', LOWER(:tagName), '%') " +
            "GROUP BY expo.expoID " +
            "ORDER BY COUNT(tag.tagID) DESC"
            , nativeQuery = true)
    List<Expo> findExposByTagName(@Param("tagName") String tagName);

    @Query(value = "SELECT expo.* " +
            "FROM expo " +
            "WHERE LOWER(expo.name) LIKE CONCAT ('%', LOWER(:keyword), '%') " +
            "OR LOWER(expo.introduction LIKE CONCAT ('%', LOWER(:keyword), '%'))"
            , nativeQuery = true)
    List<Expo> findExposByNameAndIntro(@Param("keyword") String keyword);
}
