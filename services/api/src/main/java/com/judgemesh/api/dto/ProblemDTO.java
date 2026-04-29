package com.judgemesh.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDTO {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private List<String> tags;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
    /** DRAFT / PUBLISHED / OFFLINE */
    private String status;
}
