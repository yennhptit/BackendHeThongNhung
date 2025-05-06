package com.mycompany.myapp.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonthlyStatsDTO {
    private long currentMonthDrivers;
    private long previousMonthDrivers;
    private long currentMonthVehicles;
    private long previousMonthVehicles;
    private long currentMonthDrowsinessViolations;
    private long previousMonthDrowsinessViolations;
    private long currentMonthAlcoholViolations;
    private long previousMonthAlcoholViolations;
}
