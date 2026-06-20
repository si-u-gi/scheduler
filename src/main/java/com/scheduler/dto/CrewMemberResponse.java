package com.scheduler.dto;

import com.sidaesaeng.entity.CrewMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMemberResponse {
    private Long id;
    private UserResponse user;
    private String role;
    private LocalDateTime joinedAt;

    public static CrewMemberResponse from(CrewMember member) {
        return CrewMemberResponse.builder()
                .id(member.getId())
                .user(UserResponse.from(member.getUser()))
                .role(member.getRole().name())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
