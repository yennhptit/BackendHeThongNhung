package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.enumeration.ViolationType;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.repository.ViolationRepository;
import com.mycompany.myapp.service.StatsService;
import com.mycompany.myapp.service.dto.MonthlyStatsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final ViolationRepository violationRepository;

    public StatsServiceImpl(
        DriverRepository driverRepository,
        VehicleRepository vehicleRepository,
        ViolationRepository violationRepository
    ) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.violationRepository = violationRepository;
    }

    @Override
    public MonthlyStatsDTO getMonthlyStats(Instant timeNow) {
        ZoneId zone = ZoneOffset.UTC; // hoặc ZoneId.of("Asia/Ho_Chi_Minh")

        LocalDateTime nowLdt = LocalDateTime.ofInstant(timeNow, zone);
        YearMonth thisMonth = YearMonth.from(nowLdt);
        YearMonth lastMonth = thisMonth.minusMonths(1);

        Instant startOfThisMonth = thisMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant startOfLastMonth = lastMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59).atZone(zone).toInstant();

        MonthlyStatsDTO dto = new MonthlyStatsDTO();
        dto.setCurrentMonthDrivers(driverRepository.countByCreatedAtBetween(startOfThisMonth, timeNow));
        dto.setPreviousMonthDrivers(driverRepository.countByCreatedAtBetween(startOfLastMonth, endOfLastMonth));

        dto.setCurrentMonthVehicles(vehicleRepository.countByCreatedAtBetween(startOfThisMonth, timeNow));
        dto.setPreviousMonthVehicles(vehicleRepository.countByCreatedAtBetween(startOfLastMonth, endOfLastMonth));

        dto.setCurrentMonthDrowsinessViolations(violationRepository.countByTypeAndTimestampBetween(ViolationType.DROWSINESS, startOfThisMonth, timeNow));
        dto.setPreviousMonthDrowsinessViolations(violationRepository.countByTypeAndTimestampBetween(ViolationType.DROWSINESS, startOfLastMonth, endOfLastMonth));

        dto.setCurrentMonthAlcoholViolations(violationRepository.countByTypeAndTimestampBetween(ViolationType.ALCOHOL, startOfThisMonth, timeNow));
        dto.setPreviousMonthAlcoholViolations(violationRepository.countByTypeAndTimestampBetween(ViolationType.ALCOHOL, startOfLastMonth, endOfLastMonth));

        return dto;
    }
}
