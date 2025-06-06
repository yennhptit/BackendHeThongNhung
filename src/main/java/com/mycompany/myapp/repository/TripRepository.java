package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.enumeration.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    Optional<Trip> findFirstByDriverAndVehicleAndStatus(Driver driver, Vehicle vehicle, TripStatus tripStatus);

    Page<Trip> findAllByIsDeleteFalse(Pageable pageable);

    List<Trip> findAllByIsDeleteFalse();

    List<Trip> findAllByDriver(Driver driver);

    List<Trip> findAllByVehicle(Vehicle vehicle);
}
