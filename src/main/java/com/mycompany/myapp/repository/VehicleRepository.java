package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vehicle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    long countByCreatedAtBetween(Instant start, Instant end);
}
