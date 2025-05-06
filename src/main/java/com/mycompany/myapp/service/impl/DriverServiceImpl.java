package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.enumeration.DriverStatus;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.service.DriverService;
import com.mycompany.myapp.service.GoogleDriveService;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.request.DriverRequest;
import com.mycompany.myapp.service.mapper.DriverMapper;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Driver}.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final GoogleDriveService googleDriveService;

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    public DriverServiceImpl(GoogleDriveService googleDriveService, DriverRepository driverRepository, DriverMapper driverMapper) {
        this.googleDriveService = googleDriveService;
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    private String generateDriverId(Long id, Instant createdAt) {
        String timestamp = DateTimeFormatter.ofPattern("yyMMddHHmmss")
            .withZone(ZoneId.of("UTC"))
            .format(createdAt);
        String idPart = String.format("%04d", id % 10000);
        return timestamp + idPart;
    }

    @Override
    public DriverDTO save(DriverRequest driverRequest) {
        try {
            MultipartFile faceImage = driverRequest.getFaceImage();
            String faceImageUrl = googleDriveService.uploadFile(faceImage);

            Driver driver = new Driver();
            driver.setName(driverRequest.getName());
            driver.setLicenseNumber(driverRequest.getLicenseNumber());
            driver.setFaceData(faceImageUrl);
            driver.setCreatedAt(Instant.now());
            driver.setStatus(DriverStatus.INACTIVE);

            driver = driverRepository.save(driver);

            driver.setDriverId(generateDriverId(driver.getId(), driver.getCreatedAt()));

            return driverMapper.toDto(driver);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Google Drive", e);
        }
    }

    public Optional<Driver> findByDriverId(String driverId) {
        return driverRepository.findByDriverId(driverId);
    }

    @Override
    public DriverDTO save(DriverDTO driverDTO) {
        log.debug("Request to save Driver : {}", driverDTO);
        Driver driver = driverMapper.toEntity(driverDTO);
        driver = driverRepository.save(driver);
        return driverMapper.toDto(driver);
    }

    @Override
    public DriverDTO update(DriverDTO driverDTO) {
        log.debug("Request to update Driver : {}", driverDTO);
        Driver driver = driverMapper.toEntity(driverDTO);
        driver = driverRepository.save(driver);
        return driverMapper.toDto(driver);
    }

    @Override
    public Optional<DriverDTO> partialUpdate(DriverDTO driverDTO) {
        log.debug("Request to partially update Driver : {}", driverDTO);

        return driverRepository
            .findById(driverDTO.getId())
            .map(existingDriver -> {
                driverMapper.partialUpdate(existingDriver, driverDTO);

                return existingDriver;
            })
            .map(driverRepository::save)
            .map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Drivers");
        return driverRepository.findAll(pageable).map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        return driverRepository.findById(id).map(driverMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        driverRepository.deleteById(id);
    }
}
