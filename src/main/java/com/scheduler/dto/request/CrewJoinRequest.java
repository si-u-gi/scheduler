package com.scheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrewJoinRequest {

    @NotBlank(message = "초대코드는 필수입니다")
    private String inviteCode;
}
