package com.judgemesh.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO {
    private Long id;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private Integer freezeBeforeMin;
    private List<Long> problemIds;
    private Boolean frozen;
    private Boolean registered;
}
