package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.enumeration.ViolationType;
import com.mycompany.myapp.service.StatsService;
import com.mycompany.myapp.service.dto.DriverViolationStatsDTO;
import com.mycompany.myapp.service.dto.MonthlyStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsResource {

    private final StatsService statsService;

    public StatsResource(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStats(@RequestParam("time") Instant timeNow) {
        MonthlyStatsDTO stats = statsService.getMonthlyStats(timeNow);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/drowsiness/daily")
    public ResponseEntity<Map<LocalDate, Long>> getDrowsinessViolations() {
        return ResponseEntity.ok(statsService.getDailyViolationCounts(ViolationType.DROWSINESS));
    }

    @GetMapping("/alcohol/daily")
    public ResponseEntity<Map<LocalDate, Long>> getAlcoholViolations() {
        return ResponseEntity.ok(statsService.getDailyViolationCounts(ViolationType.ALCOHOL));
    }

    @GetMapping("/top-drivers/violations")
    public ResponseEntity<List<DriverViolationStatsDTO>> getTop5DriversWithViolations() {
        return ResponseEntity.ok(statsService.getTop5DriversByViolations());
    }
}
