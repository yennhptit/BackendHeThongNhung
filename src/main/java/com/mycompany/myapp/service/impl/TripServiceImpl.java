package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.enumeration.DriverStatus;
import com.mycompany.myapp.domain.enumeration.TripStatus;
import com.mycompany.myapp.domain.enumeration.VehicleStatus;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.TripService;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.service.mapper.TripMapper;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Trip}.
 */
@Service
@Transactional
public class TripServiceImpl implements TripService {

    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    private final TripMapper tripMapper;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper,
                           DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public boolean saveTrip(String driverId, Long vehicleId, boolean running) {
        Optional<Driver> driverOtp = driverRepository.findByDriverId(driverId);
        Optional<Vehicle> vehicleOtp = vehicleRepository.findById(vehicleId);

        if (!driverOtp.isPresent() || !vehicleOtp.isPresent()) {
            System.err.println("Driver or Vehicle not found");
            return false;
        }

        Driver driver = driverOtp.get();
        Vehicle vehicle = vehicleOtp.get();

        if (running) {
            // Bắt đầu chuyến đi mới
            Trip trip = new Trip();
            trip.setDriver(driver);
            trip.setVehicle(vehicle);
            trip.setStartTime(Instant.now());
            trip.setStatus(TripStatus.ONGOING);

            driver.setStatus(DriverStatus.ACTIVE);
            vehicle.setStatus(VehicleStatus.RUNNING);

            tripRepository.save(trip);
            System.out.println("Saved trip successful.");
            return true;

        } else {
            // Kết thúc chuyến đi hiện tại
            Optional<Trip> tripOtp = tripRepository.findFirstByDriverAndVehicleAndStatus(driver, vehicle, TripStatus.ONGOING);
            if (tripOtp.isPresent()) {
                Trip trip = tripOtp.get();
                trip.setEndTime(Instant.now());
                trip.setStatus(TripStatus.COMPLETED);

                driver.setStatus(DriverStatus.INACTIVE);
                vehicle.setStatus(VehicleStatus.AVAILABLE);

                tripRepository.save(trip);
                System.out.println("Completed trip successful.");
                return true;

            } else {
                System.out.println("Trip is already completed.");
                return false;
            }
        }
    }

    @Override
    public TripDTO save(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public TripDTO update(TripDTO tripDTO) {
        log.debug("Request to update Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public Optional<TripDTO> partialUpdate(TripDTO tripDTO) {
        log.debug("Request to partially update Trip : {}", tripDTO);

        return tripRepository
            .findById(tripDTO.getId())
            .map(existingTrip -> {
                tripMapper.partialUpdate(existingTrip, tripDTO);

                return existingTrip;
            })
            .map(tripRepository::save)
            .map(tripMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Trips without deleted");
        return tripRepository.findAllByIsDeleteFalse(pageable).map(tripMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> findAllIncludingDeleted(Pageable pageable) {
        log.debug("Request to get all Trips including deleted");
        return tripRepository.findAll(pageable).map(tripMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDTO> findAll() {
        return tripRepository.findAllByIsDeleteFalse()
            .stream()
            .map(tripMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripDTO> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id).map(tripMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
//        tripRepository.deleteById(id);
        tripRepository.findById(id).ifPresent(trip -> {
            trip.setIsDelete(true);
            tripRepository.save(trip);
        });
    }
}
