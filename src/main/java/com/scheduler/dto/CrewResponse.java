package com.scheduler.dto;

import com.sidaesaeng.entity.Crew;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewResponse {
    private Long id;
    private String name;
    private String description;
    private String inviteCode;
    private LocalDateTime createdAt;
    private CrewMemberResponse leader;
    private Integer memberCount;

    public static CrewResponse from(Crew crew) {
        return CrewResponse.builder()
                .id(crew.getId())
                .name(crew.getName())
                .description(crew.getDescription())
                .inviteCode(crew.getInviteCode())
                .createdAt(crew.getCreatedAt())
                .build();
    }
}
