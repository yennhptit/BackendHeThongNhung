package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enumeration.ViolationType;
import com.mycompany.myapp.service.dto.DriverViolationStatsDTO;
import com.mycompany.myapp.service.dto.MonthlyStatsDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatsService {
    MonthlyStatsDTO getMonthlyStats();

    Map<LocalDate, Long> getDailyViolationCounts(ViolationType type);

    List<DriverViolationStatsDTO> getTop5DriversByViolations();
}
