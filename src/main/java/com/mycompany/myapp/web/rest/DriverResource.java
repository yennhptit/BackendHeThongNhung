package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.service.DriverQueryService;
import com.mycompany.myapp.service.DriverService;
import com.mycompany.myapp.service.criteria.DriverCriteria;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.request.DriverRequest;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Driver}.
 */
@RestController
@RequestMapping("/api")
public class DriverResource {

    private final Logger log = LoggerFactory.getLogger(DriverResource.class);

    private static final String ENTITY_NAME = "driver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    private final DriverQueryService driverQueryService;

    public DriverResource(DriverService driverService, DriverRepository driverRepository, DriverQueryService driverQueryService) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
        this.driverQueryService = driverQueryService;
    }

    @Operation(summary = "Create a new driver", description = "Add a new driver with multipart data")
    @PostMapping(value = "/drivers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DriverDTO> createDriver(@Valid @ModelAttribute DriverRequest driverRequest) {
        try {
            DriverDTO driverDTO = driverService.save(driverRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(driverDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update an existing driver", description = "Update a driver by its ID")
    @PutMapping("/drivers/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DriverDTO driverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Driver : {}, {}", id, driverDTO);
        if (driverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DriverDTO result = driverService.update(driverDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, driverDTO.getId().toString()))
            .body(result);
    }

    @Operation(summary = "Get all drivers with pagination", description = "Retrieve a paginated list of all drivers")
    @GetMapping("/drivers")
    public ResponseEntity<List<DriverDTO>> getAllDrivers(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        log.debug("REST request to get all Drivers with pagination");

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DriverDTO> page = driverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page
        );

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "Get all drivers including deleted", description = "Retrieve all drivers including those marked as deleted")
    @GetMapping("/drivers/all")
    public ResponseEntity<List<DriverDTO>> getAllTripsIncludingDeleted(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DriverDTO> page = driverService.findAllIncludingDeleted(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "Get a driver by ID", description = "Retrieve details of a specific driver by ID")
    @GetMapping("/drivers/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long id) {
        log.debug("REST request to get Driver : {}", id);
        Optional<DriverDTO> driverDTO = driverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driverDTO);
    }

    @Operation(summary = "Delete a driver", description = "Delete a driver by its ID")
    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        log.debug("REST request to delete Driver : {}", id);
        driverService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
