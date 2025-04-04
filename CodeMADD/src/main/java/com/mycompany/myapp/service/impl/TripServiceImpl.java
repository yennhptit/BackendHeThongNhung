package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.service.TripService;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.service.mapper.TripMapper;
import java.util.Optional;
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

    private final TripMapper tripMapper;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
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
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable).map(tripMapper::toDto);
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
        tripRepository.deleteById(id);
    }
}
