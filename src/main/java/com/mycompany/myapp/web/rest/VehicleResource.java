package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.VehicleQueryService;
import com.mycompany.myapp.service.VehicleService;
import com.mycompany.myapp.service.criteria.VehicleCriteria;
import com.mycompany.myapp.service.dto.VehicleDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Vehicle}.
 */
@RestController
@RequestMapping("/api")
public class VehicleResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);

    private static final String ENTITY_NAME = "vehicle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleService vehicleService;

    private final VehicleRepository vehicleRepository;

    private final VehicleQueryService vehicleQueryService;

    public VehicleResource(VehicleService vehicleService, VehicleRepository vehicleRepository, VehicleQueryService vehicleQueryService) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Operation(summary = "Create a new vehicle", description = "Add a new vehicle to the system")
    @PostMapping("/vehicles")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) throws URISyntaxException {
        log.debug("REST request to save Vehicle : {}", vehicleDTO);
        if (vehicleDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleDTO result = vehicleService.save(vehicleDTO);
        return ResponseEntity
            .created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @Operation(summary = "Update an existing vehicle", description = "Update a vehicle by its ID")
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleDTO vehicleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vehicle : {}, {}", id, vehicleDTO);
        if (vehicleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VehicleDTO result = vehicleService.update(vehicleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleDTO.getId().toString()))
            .body(result);
    }

    @Operation(summary = "Get all vehicles with pagination", description = "Retrieve a paginated list of all vehicles")
    @GetMapping("/vehicles")
//    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
//        @RequestParam(defaultValue = "0") int pageNumber,
//        @RequestParam(defaultValue = "10") int pageSize
//    ) {
//        log.debug("REST request to get all Vehicles with pagination");
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<VehicleDTO> page = vehicleService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {

        List<VehicleDTO> vehicleDTOS = vehicleService.findAll();

        return ResponseEntity.ok().body(vehicleDTOS);
    }

    @Operation(summary = "Get all vehicles including deleted", description = "Retrieve all vehicles including those marked as deleted")
    @GetMapping("/vehicles/all")
    public ResponseEntity<List<VehicleDTO>> getAllTripsIncludingDeleted(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VehicleDTO> page = vehicleService.findAllIncludingDeleted(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "Get a vehicle by ID", description = "Retrieve details of a specific vehicle by its ID")
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) {
        log.debug("REST request to get Vehicle : {}", id);
        Optional<VehicleDTO> vehicleDTO = vehicleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleDTO);
    }

    @Operation(summary = "Delete a vehicle", description = "Delete a vehicle by its ID")
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete Vehicle : {}", id);
        vehicleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
