package com.scheduler.repository;

import com.sidaesaeng.entity.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

    @Query("SELECT cm FROM CrewMember cm JOIN FETCH cm.user WHERE cm.crew.id = :crewId")
    List<CrewMember> findAllByCrewIdWithUser(@Param("crewId") Long crewId);

    Optional<CrewMember> findByCrewIdAndUserId(Long crewId, Long userId);

    boolean existsByCrewIdAndUserId(Long crewId, Long userId);

    @Query("SELECT COUNT(cm) FROM CrewMember cm WHERE cm.crew.id = :crewId")
    int countByCrewId(@Param("crewId") Long crewId);

    List<CrewMember> findByCrewId(Long crewId);

    Optional<CrewMember> findByCrewIdAndRole(Long crewId, CrewMember.CrewRole role);
}
