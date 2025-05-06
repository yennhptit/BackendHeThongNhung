package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Driver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {

    Optional<Driver> findByDriverId(String driverId);

    long countByCreatedAtBetween(Instant start, Instant end);
}
