package com.scheduler.controller;

import com.sidaesaeng.dto.ApiResponse;
import com.sidaesaeng.dto.CommentResponse;
import com.sidaesaeng.dto.request.CommentCreateRequest;
import com.sidaesaeng.security.UserPrincipal;
import com.sidaesaeng.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials/{materialId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "자료에 댓글을 작성합니다")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long materialId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        CommentResponse response = commentService.createComment(materialId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 작성되었습니다", response));
    }

    @GetMapping
    @Operation(summary = "댓글 목록", description = "자료의 댓글 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long materialId,
            @AuthenticationPrincipal UserPrincipal user) {
        List<CommentResponse> comments = commentService.getCommentsByMaterial(materialId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long materialId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        CommentResponse response = commentService.updateComment(commentId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다", response));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long materialId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal user) {
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다", null));
    }
}
