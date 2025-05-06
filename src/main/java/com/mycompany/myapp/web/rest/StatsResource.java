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

import io.swagger.v3.oas.annotations.Operation;

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

    @Operation(summary = "Get monthly statistics",
        description = "Retrieve monthly statistics for drivers, vehicles, and violations.")
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStats(@RequestParam("time") Instant timeNow) {
        MonthlyStatsDTO stats = statsService.getMonthlyStats(timeNow);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get daily drowsiness violations",
        description = "Retrieve daily violation counts related to driver drowsiness.")
    @GetMapping("/drowsiness/daily")
    public ResponseEntity<Map<LocalDate, Long>> getDrowsinessViolations() {
        return ResponseEntity.ok(statsService.getDailyViolationCounts(ViolationType.DROWSINESS));
    }

    @Operation(summary = "Get daily alcohol violations",
        description = "Retrieve daily violation counts related to alcohol consumption by drivers.")
    @GetMapping("/alcohol/daily")
    public ResponseEntity<Map<LocalDate, Long>> getAlcoholViolations() {
        return ResponseEntity.ok(statsService.getDailyViolationCounts(ViolationType.ALCOHOL));
    }

    @Operation(summary = "Get top 5 drivers with violations",
        description = "Retrieve the top 5 drivers who have the highest violation counts.")
    @GetMapping("/top-drivers/violations")
    public ResponseEntity<List<DriverViolationStatsDTO>> getTop5DriversWithViolations() {
        return ResponseEntity.ok(statsService.getTop5DriversByViolations());
    }
}
