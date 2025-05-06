package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MonthlyStatsDTO;

import java.time.Instant;

public interface StatsService {
    MonthlyStatsDTO getMonthlyStats(Instant timeNow);
}
