package com.scheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleCreateRequest {

    @NotNull(message = "크루 ID는 필수입니다")
    private Long crewId;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    @NotNull(message = "시작 시간은 필수입니다")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간은 필수입니다")
    private LocalDateTime endTime;
}
