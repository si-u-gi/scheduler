package com.scheduler.controller;

import com.sidaesaeng.dto.ApiResponse;
import com.sidaesaeng.dto.ScheduleResponse;
import com.sidaesaeng.dto.request.ScheduleCreateRequest;
import com.sidaesaeng.security.UserPrincipal;
import com.sidaesaeng.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "일정 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다")
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        ScheduleResponse response = scheduleService.createSchedule(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("일정이 생성되었습니다", response));
    }

    @GetMapping
    @Operation(summary = "크루별 일정 조회", description = "특정 크루의 일정 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getSchedules(
            @RequestParam Long crewId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal UserPrincipal user) {

        List<ScheduleResponse> schedules;
        if (start != null && end != null) {
            schedules = scheduleService.getSchedulesByCrewAndDateRange(crewId, start, end, user.getId());
        } else {
            schedules = scheduleService.getSchedulesByCrew(crewId, user.getId());
        }
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "일정 상세", description = "일정 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<ScheduleResponse>> getSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal UserPrincipal user) {
        ScheduleResponse response = scheduleService.getScheduleById(scheduleId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "일정 수정", description = "일정을 수정합니다")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        ScheduleResponse response = scheduleService.updateSchedule(scheduleId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("일정이 수정되었습니다", response));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal UserPrincipal user) {
        scheduleService.deleteSchedule(scheduleId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("일정이 삭제되었습니다", null));
    }
}
