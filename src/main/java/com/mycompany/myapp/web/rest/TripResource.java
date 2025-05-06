package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.service.TripQueryService;
import com.mycompany.myapp.service.TripService;
import com.mycompany.myapp.service.criteria.TripCriteria;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Trip}.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripService tripService;

    private final TripRepository tripRepository;

    private final TripQueryService tripQueryService;

    public TripResource(TripService tripService, TripRepository tripRepository, TripQueryService tripQueryService) {
        this.tripService = tripService;
        this.tripRepository = tripRepository;
        this.tripQueryService = tripQueryService;
    }

    @Operation(summary = "Update an existing trip", description = "Update a trip by its ID")
    @PutMapping("/trips/{id}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable(value = "id", required = false) final Long id, @RequestBody TripDTO tripDTO)
        throws URISyntaxException {
        log.debug("REST request to update Trip : {}, {}", id, tripDTO);
        if (tripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripDTO result = tripService.update(tripDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDTO.getId().toString()))
            .body(result);
    }

    @Operation(summary = "Get all trips with pagination", description = "Retrieve a paginated list of all trips")
    @GetMapping("/trips")
//    public ResponseEntity<List<TripDTO>> getAllTrips(
//        @RequestParam(defaultValue = "0") int pageNumber,
//        @RequestParam(defaultValue = "10") int pageSize
//    ) {
//        log.debug("REST request to get all Trips with pagination");
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<TripDTO> page = tripService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        List<TripDTO> tripDTOS = tripService.findAll();

        return ResponseEntity.ok().body(tripDTOS);
    }

    @Operation(summary = "Get all trips including deleted", description = "Retrieve all trips including those marked as deleted")
    @GetMapping("/trips/all")
    public ResponseEntity<List<TripDTO>> getAllTripsIncludingDeleted(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TripDTO> page = tripService.findAllIncludingDeleted(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "Get a trip by ID", description = "Retrieve details of a specific trip by ID")
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<TripDTO> tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripDTO);
    }

    @Operation(summary = "Delete a trip", description = "Delete a trip by its ID")
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
