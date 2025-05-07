package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.domain.enumeration.ViolationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the Violation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long>, JpaSpecificationExecutor<Violation> {
    List<Violation> findAllByDriver(Driver driver);

    Page<Violation> findAllByIsDeleteFalse(Pageable pageable);

    List<Violation> findAllByIsDeleteFalse();

    long countByTypeAndTimestampBetween(ViolationType type, Instant start, Instant end);

    @Query("SELECT DATE(v.timestamp) as day, COUNT(v) " +
        "FROM Violation v " +
        "WHERE v.isDelete = false " +
        "AND v.timestamp >= :start AND v.timestamp <= :end " +
        "AND v.type = :type " +
        "GROUP BY DATE(v.timestamp)")
    List<Object[]> countDailyViolationsByType(@Param("start") Instant start,
                                              @Param("end") Instant end,
                                              @Param("type") ViolationType type);

    @Query("""
        SELECT v.driver,
               COUNT(v),
               SUM(CASE WHEN v.type = 'DROWSINESS' THEN 1 ELSE 0 END),
               SUM(CASE WHEN v.type = 'ALCOHOL' THEN 1 ELSE 0 END)
        FROM Violation v
        WHERE v.isDelete = false
        GROUP BY v.driver
        ORDER BY COUNT(v) DESC
    """)
    List<Object[]> findTop5DriversWithViolationStats(Pageable pageable);

}

