package com.scheduler.dto;

import com.sidaesaeng.entity.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {
    private Long id;
    private Long crewId;
    private String crewName;
    private String title;
    private String description;
    private String originalFilename;
    private Long fileSize;
    private Integer downloadCount;
    private LocalDateTime createdAt;
    private UserResponse creator;
    private List<String> tags;

    public static MaterialResponse from(Material material) {
        return MaterialResponse.builder()
                .id(material.getId())
                .crewId(material.getCrew().getId())
                .crewName(material.getCrew().getName())
                .title(material.getTitle())
                .description(material.getDescription())
                .originalFilename(material.getOriginalFilename())
                .fileSize(material.getFileSize())
                .downloadCount(material.getDownloadCount())
                .createdAt(material.getCreatedAt())
                .creator(material.getCreator() != null ? UserResponse.from(material.getCreator()) : null)
                .tags(material.getTags().stream()
                        .map(tag -> tag.getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
