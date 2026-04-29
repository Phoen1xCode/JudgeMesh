package com.judgemesh.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitDTO {
    private Long id;
    private Long userId;
    private Long problemId;
    private String language;
    /** PENDING / JUDGING / AC / WA / TLE / MLE / RE / CE / SE */
    private String status;
    private Integer timeUsedMs;
    private Integer memoryUsedKb;
    private Instant createdAt;
}
