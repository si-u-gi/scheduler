package com.scheduler.service;

import com.sidaesaeng.dto.CommentResponse;
import com.sidaesaeng.dto.request.CommentCreateRequest;
import com.sidaesaeng.entity.Comment;
import com.sidaesaeng.entity.Material;
import com.sidaesaeng.entity.User;
import com.sidaesaeng.exception.ResourceNotFoundException;
import com.sidaesaeng.repository.CommentRepository;
import com.sidaesaeng.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MaterialRepository materialRepository;
    private final UserService userService;
    private final CrewService crewService;

    @Transactional
    public CommentResponse createComment(Long materialId, CommentCreateRequest request, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("자료를 찾을 수 없습니다"));

        crewService.validateCrewMember(material.getCrew().getId(), userId);
        User user = userService.getUserEntityById(userId);

        Comment comment = Comment.builder()
                .material(material)
                .user(user)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByMaterial(Long materialId, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("자료를 찾을 수 없습니다"));

        crewService.validateCrewMember(material.getCrew().getId(), userId);

        return commentRepository.findByMaterialIdOrderByCreatedAtAsc(materialId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentCreateRequest request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("댓글을 수정할 권한이 없습니다");
        }

        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("댓글을 삭제할 권한이 없습니다");
        }

        commentRepository.delete(comment);
    }
}
