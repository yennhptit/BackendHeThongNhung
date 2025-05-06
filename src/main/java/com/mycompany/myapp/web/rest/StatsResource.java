package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.StatsService;
import com.mycompany.myapp.service.dto.MonthlyStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

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
}
