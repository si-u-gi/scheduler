package com.scheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrewCreateRequest {

    @NotBlank(message = "크루 이름은 필수입니다")
    @Size(min = 2, max = 100, message = "크루 이름은 2자 이상 100자 이하입니다")
    private String name;

    private String description;
}
