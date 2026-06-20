package com.scheduler.dto;

import com.sidaesaeng.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private Long crewId;
    private String crewName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private UserResponse creator;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .crewId(schedule.getCrew().getId())
                .crewName(schedule.getCrew().getName())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .createdAt(schedule.getCreatedAt())
                .creator(schedule.getCreator() != null ? UserResponse.from(schedule.getCreator()) : null)
                .build();
    }
}
