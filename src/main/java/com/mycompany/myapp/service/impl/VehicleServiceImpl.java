package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.VehicleService;
import com.mycompany.myapp.service.dto.VehicleDTO;
import com.mycompany.myapp.service.mapper.VehicleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;

    private final TripRepository tripRepository;

    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, TripRepository tripRepository) {
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        log.debug("Request to save Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        log.debug("Request to update Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO) {
        log.debug("Request to partially update Vehicle : {}", vehicleDTO);

        return vehicleRepository
            .findById(vehicleDTO.getId())
            .map(existingVehicle -> {
                vehicleMapper.partialUpdate(existingVehicle, vehicleDTO);

                return existingVehicle;
            })
            .map(vehicleRepository::save)
            .map(vehicleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vehicles");
        return vehicleRepository.findAllByIsDeleteFalse(pageable).map(vehicleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findAllIncludingDeleted(Pageable pageable) {
        log.debug("Request to get all Vehicles including deleted");
        return vehicleRepository.findAll(pageable).map(vehicleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleDTO> findOne(Long id) {
        log.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findById(id).map(vehicleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.findById(id).ifPresent(vehicle -> {
            vehicle.setIsDelete(true);
            vehicleRepository.save(vehicle);

            tripRepository.findAllByVehicle(vehicle).forEach(trip -> {
                trip.setIsDelete(true);
                tripRepository.save(trip);
            });
        });
    }
}
