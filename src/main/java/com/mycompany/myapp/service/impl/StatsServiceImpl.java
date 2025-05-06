package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.enumeration.ViolationType;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.repository.ViolationRepository;
import com.mycompany.myapp.service.StatsService;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.DriverViolationStatsDTO;
import com.mycompany.myapp.service.dto.MonthlyStatsDTO;
import com.mycompany.myapp.service.mapper.DriverMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final ViolationRepository violationRepository;

    private final DriverMapper driverMapper;

    public StatsServiceImpl(
        DriverRepository driverRepository,
        VehicleRepository vehicleRepository,
        ViolationRepository violationRepository,
        DriverMapper driverMapper
    ) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.violationRepository = violationRepository;
        this.driverMapper = driverMapper;
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

    @Override
    public Map<LocalDate, Long> getDailyViolationCounts(ViolationType type) {
        LocalDate today = LocalDate.now();
        // Lấy ngày đầu tháng và cuối tháng
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        // Tạo danh sách tất cả các ngày trong tháng
        List<LocalDate> allDaysInMonth = firstDayOfMonth.datesUntil(lastDayOfMonth.plusDays(1)).collect(Collectors.toList());

        // Chuyển start, end thành Instant
        Instant start = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = lastDayOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Lấy kết quả từ cơ sở dữ liệu
        List<Object[]> results = violationRepository.countDailyViolationsByType(start, end, type);

        // Tạo Map để lưu kết quả
        Map<LocalDate, Long> data = new TreeMap<>();

        // Dữ liệu từ database (ngày có vi phạm)
        for (Object[] row : results) {
            Date sqlDate = (Date) row[0];
            LocalDate date = sqlDate.toLocalDate();
            Long count = (Long) row[1];
            data.put(date, count);
        }

        // Lấp đầy dữ liệu cho tất cả các ngày trong tháng
        for (LocalDate date : allDaysInMonth) {
            // Nếu ngày không có vi phạm trong database, gán count = 0
            data.putIfAbsent(date, 0L);
        }

        return data;
    }


    @Override
    public List<DriverViolationStatsDTO> getTop5DriversByViolations() {
        Pageable top5 = PageRequest.of(0, 5);
        List<Object[]> rawData = violationRepository.findTop5DriversWithViolationStats(top5);

        return rawData.stream().map(row -> {
            Driver driver = (Driver) row[0];
            Long total = (Long) row[1];
            Long drowsy = (Long) row[2];
            Long alcohol = (Long) row[3];

            DriverDTO driverDTO = driverMapper.toDto(driver);

            DriverViolationStatsDTO dto = new DriverViolationStatsDTO();
            dto.setDriver(driverDTO);
            dto.setTotalViolations(total);
            dto.setDrowsinessCount(drowsy);
            dto.setAlcoholCount(alcohol);
            return dto;
        }).collect(Collectors.toList());
    }
}
