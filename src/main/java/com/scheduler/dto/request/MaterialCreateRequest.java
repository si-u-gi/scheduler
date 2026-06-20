package com.scheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MaterialCreateRequest {

    @NotNull(message = "크루 ID는 필수입니다")
    private Long crewId;

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    private List<String> tags;
}
