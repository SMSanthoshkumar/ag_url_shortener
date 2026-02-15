package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private Long urlId;
    private String shortCode;
    private Long totalClicks;
    private Map<LocalDate, Long> clicksByDate;
}
