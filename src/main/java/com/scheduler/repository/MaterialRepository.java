package com.scheduler.repository;

import com.sidaesaeng.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("SELECT m FROM Material m JOIN FETCH m.crew WHERE m.crew.id = :crewId ORDER BY m.createdAt DESC")
    List<Material> findAllByCrewIdOrderByCreatedAtDesc(@Param("crewId") Long crewId);

    @Query("SELECT DISTINCT m FROM Material m " +
           "JOIN m.tags t " +
           "WHERE m.crew.id = :crewId AND t.name = :tagName " +
           "ORDER BY m.createdAt DESC")
    List<Material> findByCrewIdAndTagName(@Param("crewId") Long crewId, @Param("tagName") String tagName);

    List<Material> findByCrewId(Long crewId);
}
