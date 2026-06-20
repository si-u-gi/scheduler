package com.scheduler.repository;

import com.sidaesaeng.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {

    Optional<Crew> findByInviteCode(String inviteCode);

    @Query("SELECT c FROM Crew c JOIN CrewMember cm ON c.id = cm.crew.id WHERE cm.user.id = :userId")
    List<Crew> findAllByUserId(@Param("userId") Long userId);

    boolean existsByInviteCode(String inviteCode);
}
