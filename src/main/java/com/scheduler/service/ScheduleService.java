package com.scheduler.service;

import com.sidaesaeng.dto.ScheduleResponse;
import com.sidaesaeng.dto.request.ScheduleCreateRequest;
import com.sidaesaeng.entity.Crew;
import com.sidaesaeng.entity.Schedule;
import com.sidaesaeng.entity.User;
import com.sidaesaeng.exception.ResourceNotFoundException;
import com.sidaesaeng.repository.CrewRepository;
import com.sidaesaeng.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CrewRepository crewRepository;
    private final UserService userService;
    private final CrewService crewService;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request, Long userId) {
        Crew crew = crewRepository.findById(request.getCrewId())
                .orElseThrow(() -> new ResourceNotFoundException("크루를 찾을 수 없습니다"));

        crewService.validateCrewMember(request.getCrewId(), userId);
        User user = userService.getUserEntityById(userId);

        Schedule schedule = Schedule.builder()
                .crew(crew)
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .creator(user)
                .build();

        schedule = scheduleRepository.save(schedule);
        return ScheduleResponse.from(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByCrew(Long crewId, Long userId) {
        crewService.validateCrewMember(crewId, userId);
        return scheduleRepository.findAllByCrewIdOrderByStartTimeAsc(crewId).stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByCrewAndDateRange(
            Long crewId, LocalDateTime start, LocalDateTime end, Long userId) {
        crewService.validateCrewMember(crewId, userId);
        return scheduleRepository.findByCrewIdAndDateRange(crewId, start, end).stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleById(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다"));

        crewService.validateCrewMember(schedule.getCrew().getId(), userId);
        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleCreateRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다"));

        crewService.validateCrewMember(schedule.getCrew().getId(), userId);

        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        schedule = scheduleRepository.save(schedule);
        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("일정을 찾을 수 없습니다"));

        crewService.validateCrewMember(schedule.getCrew().getId(), userId);
        scheduleRepository.delete(schedule);
    }
}
