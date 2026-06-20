package com.scheduler.repository;

import com.sidaesaeng.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.crew WHERE s.crew.id = :crewId ORDER BY s.startTime ASC")
    List<Schedule> findAllByCrewIdOrderByStartTimeAsc(@Param("crewId") Long crewId);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.crew JOIN FETCH s.creator WHERE s.crew.id = :crewId AND s.startTime >= :start AND s.startTime <= :end ORDER BY s.startTime ASC")
    List<Schedule> findByCrewIdAndDateRange(
            @Param("crewId") Long crewId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<Schedule> findByCrewId(Long crewId);
}
