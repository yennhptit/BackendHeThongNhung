package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.domain.enumeration.ViolationType;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.repository.ViolationRepository;
import com.mycompany.myapp.service.ViolationService;
import com.mycompany.myapp.service.dto.ViolationDTO;
import com.mycompany.myapp.service.mapper.ViolationMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Violation}.
 */
@Service
@Transactional
public class ViolationServiceImpl implements ViolationService {

    private final Logger log = LoggerFactory.getLogger(ViolationServiceImpl.class);

    private final ViolationRepository violationRepository;

    private final ViolationMapper violationMapper;

    private final DriverRepository driverRepository;


    public ViolationServiceImpl(ViolationRepository violationRepository, ViolationMapper violationMapper, DriverRepository driverRepository) {
        this.violationRepository = violationRepository;
        this.violationMapper = violationMapper;
        this.driverRepository = driverRepository;
    }

    public boolean saveAlcoholViolation(String driverId, float value) {

        Optional<Driver> driverOtp = driverRepository.findByDriverId(driverId);

        if (!driverOtp.isPresent()) {
            System.out.println("Driver not found.");
            return false;
        }

        Violation violation = new Violation();
        Driver driver = driverOtp.get();

        violation.setType(ViolationType.ALCOHOL);
        violation.setValue(value);
        violation.setTimestamp(Instant.now());

        violation.setDriver(driver);
        violation = violationRepository.save(violation);
        System.out.println("Saved alcohol violation: " + violation);

        return true;
    }

    public boolean saveDrowsinessViolation(String driverId) {

        Optional<Driver> driverOtp = driverRepository.findByDriverId(driverId);

        if (!driverOtp.isPresent()) {
            System.out.println("Driver not found.");
            return false;
        }

        Violation violation = new Violation();
        Driver driver = driverOtp.get();

        violation.setType(ViolationType.DROWSINESS);
        violation.setValue(null);
        violation.setTimestamp(Instant.now());

        violation.setDriver(driver);
        violation = violationRepository.save(violation);
        System.out.println("Saved drowsiness violation: " + violation);

        return true;
    }

    @Override
    public ViolationDTO save(ViolationDTO violationDTO) {
        log.debug("Request to save Violation : {}", violationDTO);
        Violation violation = violationMapper.toEntity(violationDTO);
        violation = violationRepository.save(violation);
        return violationMapper.toDto(violation);
    }

    @Override
    public ViolationDTO update(ViolationDTO violationDTO) {
        log.debug("Request to update Violation : {}", violationDTO);
        Violation violation = violationMapper.toEntity(violationDTO);
        violation = violationRepository.save(violation);
        return violationMapper.toDto(violation);
    }

    @Override
    public Optional<ViolationDTO> partialUpdate(ViolationDTO violationDTO) {
        log.debug("Request to partially update Violation : {}", violationDTO);

        return violationRepository
            .findById(violationDTO.getId())
            .map(existingViolation -> {
                violationMapper.partialUpdate(existingViolation, violationDTO);

                return existingViolation;
            })
            .map(violationRepository::save)
            .map(violationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViolationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Violations");
        return violationRepository.findAllByIsDeleteFalse(pageable).map(violationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViolationDTO> findAllIncludingDeleted(Pageable pageable) {
        log.debug("Request to get all Violations including deleted");
        return violationRepository.findAll(pageable).map(violationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViolationDTO> findAll() {
        return violationRepository.findAllByIsDeleteFalse()
            .stream()
            .map(violationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViolationDTO> findOne(Long id) {
        log.debug("Request to get Violation : {}", id);
        return violationRepository.findById(id).map(violationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Violation : {}", id);
//        violationRepository.deleteById(id);
        violationRepository.findById(id).ifPresent(violation -> {
            violation.setIsDelete(true);
            violationRepository.save(violation);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViolationDTO> getTop5LatestViolations() {
        return violationRepository.findTop5ByIsDeleteFalseOrderByTimestampDesc()
            .stream()
            .map(violationMapper::toDto)
            .collect(Collectors.toList());
    }
}
