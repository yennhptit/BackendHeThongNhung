package com.mycompany.myapp.service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DriverViolationStatsDTO {

    private DriverDTO driver;
    private Long totalViolations;
    private Long drowsinessCount;
    private Long alcoholCount;

}
