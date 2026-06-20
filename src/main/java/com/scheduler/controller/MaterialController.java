package com.scheduler.controller;

import com.sidaesaeng.dto.ApiResponse;
import com.sidaesaeng.dto.MaterialResponse;
import com.sidaesaeng.dto.request.MaterialCreateRequest;
import com.sidaesaeng.security.UserPrincipal;
import com.sidaesaeng.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@Tag(name = "Materials", description = "자료 공유 API")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @Operation(summary = "자료 등록", description = "새로운 자료를 등록합니다")
    public ResponseEntity<ApiResponse<MaterialResponse>> createMaterial(
            @Valid @RequestBody MaterialCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        MaterialResponse response = materialService.createMaterial(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("자료가 등록되었습니다", response));
    }

    @GetMapping
    @Operation(summary = "자료 목록 조회", description = "크루별 자료 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getMaterials(
            @RequestParam Long crewId,
            @RequestParam(required = false) String tag,
            @AuthenticationPrincipal UserPrincipal user) {

        List<MaterialResponse> materials;
        if (tag != null && !tag.isBlank()) {
            materials = materialService.getMaterialsByCrewAndTag(crewId, tag, user.getId());
        } else {
            materials = materialService.getMaterialsByCrew(crewId, user.getId());
        }
        return ResponseEntity.ok(ApiResponse.success(materials));
    }

    @GetMapping("/{materialId}")
    @Operation(summary = "자료 상세", description = "자료 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterial(
            @PathVariable Long materialId,
            @AuthenticationPrincipal UserPrincipal user) {
        MaterialResponse response = materialService.getMaterialById(materialId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{materialId}")
    @Operation(summary = "자료 삭제", description = "자료를 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(
            @PathVariable Long materialId,
            @AuthenticationPrincipal UserPrincipal user) {
        materialService.deleteMaterial(materialId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("자료가 삭제되었습니다", null));
    }

    @GetMapping("/{materialId}/download")
    @Operation(summary = "자료 다운로드", description = "자료 다운로드 횟수를 증가시킵니다")
    public ResponseEntity<ApiResponse<MaterialResponse>> downloadMaterial(
            @PathVariable Long materialId,
            @AuthenticationPrincipal UserPrincipal user) {
        MaterialResponse response = materialService.incrementDownloadCount(materialId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
