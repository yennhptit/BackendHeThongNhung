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

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param tripDTO the tripDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripDTO, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<TripDTO> createTrip(@RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        if (tripDTO.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity
            .created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trip.
     *
     * @param id the id of the tripDTO to save.
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trip, field will ignore if it is null
     */
//    @PatchMapping(value = "/trips/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<TripDTO> partialUpdateTrip(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody TripDTO tripDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update Trip partially : {}, {}", id, tripDTO);
//        if (tripDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, tripDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!tripRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<TripDTO> result = tripService.partialUpdate(tripDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDTO.getId().toString())
//        );
//    }

    /**
     * {@code GET  /trips} : get all the trips.
     */
    @GetMapping("/trips")
    public ResponseEntity<List<TripDTO>> getAllTrips(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        log.debug("REST request to get all Trips with pagination");

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TripDTO> page = tripService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

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

    /**
     * {@code GET  /trips/count} : count all the trips.
     */
//    @GetMapping("/trips/count")
//    public ResponseEntity<Long> countTrips(TripCriteria criteria) {
//        log.debug("REST request to count Trips by criteria: {}", criteria);
//        return ResponseEntity.ok().body(tripQueryService.countByCriteria(criteria));
//    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<TripDTO> tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripDTO);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     */
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
