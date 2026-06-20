package com.scheduler.controller;

import com.sidaesaeng.dto.ApiResponse;
import com.sidaesaeng.dto.CrewMemberResponse;
import com.sidaesaeng.dto.CrewResponse;
import com.sidaesaeng.dto.request.CrewCreateRequest;
import com.sidaesaeng.dto.request.CrewJoinRequest;
import com.sidaesaeng.security.UserPrincipal;
import com.sidaesaeng.service.CrewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crews")
@RequiredArgsConstructor
@Tag(name = "Crews", description = "크루(동아리) API")
public class CrewController {

    private final CrewService crewService;

    @PostMapping
    @Operation(summary = "크루 생성", description = "새로운 크루를 생성합니다")
    public ResponseEntity<ApiResponse<CrewResponse>> createCrew(
            @Valid @RequestBody CrewCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        CrewResponse response = crewService.createCrew(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("크루가 생성되었습니다", response));
    }

    @GetMapping
    @Operation(summary = "내 크루 목록", description = "내가 가입한 크루 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<CrewResponse>>> getMyCrews(
            @AuthenticationPrincipal UserPrincipal user) {
        List<CrewResponse> crews = crewService.getMyCrews(user.getId());
        return ResponseEntity.ok(ApiResponse.success(crews));
    }

    @GetMapping("/{crewId}")
    @Operation(summary = "크루 상세", description = "크루 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<CrewResponse>> getCrew(@PathVariable Long crewId) {
        CrewResponse response = crewService.getCrewById(crewId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/join")
    @Operation(summary = "크루 가입", description = "초대코드로 크루에 가입합니다")
    public ResponseEntity<ApiResponse<CrewResponse>> joinCrew(
            @Valid @RequestBody CrewJoinRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        CrewResponse response = crewService.joinCrew(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("크루에 가입되었습니다", response));
    }

    @GetMapping("/{crewId}/members")
    @Operation(summary = "크루 회원 목록", description = "크루 회원 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<CrewMemberResponse>>> getCrewMembers(@PathVariable Long crewId) {
        List<CrewMemberResponse> members = crewService.getCrewMembers(crewId);
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @DeleteMapping("/{crewId}")
    @Operation(summary = "크루 삭제", description = "크루를 삭제합니다 (크루장만 가능)")
    public ResponseEntity<ApiResponse<Void>> deleteCrew(
            @PathVariable Long crewId,
            @AuthenticationPrincipal UserPrincipal user) {
        crewService.deleteCrew(crewId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("크루가 삭제되었습니다", null));
    }
}
