package com.example.api.Repository;

import com.example.api.Entity.Booth;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {
    @EntityGraph(attributePaths = {"collaborator", "collaborator.collaborators"})
    Optional<Booth> findById(Integer id);

    @Query(value = "SELECT booth.* " +
            "FROM booth booth " +
            "JOIN booth_tag bt ON booth.boothID = bt.boothID " +
            "JOIN tag tag ON bt.tagID = tag.tagID " +
            "WHERE LOWER(tag.name) LIKE CONCAT ('%', LOWER(:tagsName), '%') " +
            "GROUP BY booth.boothID " +
            "ORDER BY COUNT(tag.tagID) DESC"
            , nativeQuery = true)
    List<Booth> findBoothsByTagsName(@Param("tagsName") String tagsName);
}
