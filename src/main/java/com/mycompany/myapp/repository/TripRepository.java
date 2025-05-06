package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.enumeration.TripStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    Optional<Trip> findFirstByDriverAndVehicleAndStatus(Driver driver, Vehicle vehicle, TripStatus tripStatus);
}
