package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.service.DriverQueryService;
import com.mycompany.myapp.service.DriverService;
import com.mycompany.myapp.service.criteria.DriverCriteria;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.request.DriverRequest;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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

    /**
     * {@code POST  /drivers} : Create a new driver.
     */
//    @PostMapping("/drivers")
//    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) throws URISyntaxException {
//        log.debug("REST request to save Driver : {}", driverDTO);
//        if (driverDTO.getId() != null) {
//            throw new BadRequestAlertException("A new driver cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        DriverDTO result = driverService.save(driverDTO);
//        return ResponseEntity
//            .created(new URI("/api/drivers/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PUT  /drivers/:id} : Updates an existing driver.
     */
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

    /**
     * {@code PATCH  /drivers/:id} : Partial updates given fields of an existing driver, field will ignore if it is null
     */
    @PatchMapping(value = "/drivers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DriverDTO> partialUpdateDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DriverDTO driverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Driver partially : {}, {}", id, driverDTO);
        if (driverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DriverDTO> result = driverService.partialUpdate(driverDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, driverDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /drivers} : get all the drivers.
     */
    @GetMapping("/drivers")
    public ResponseEntity<List<DriverDTO>> getAllDrivers(
        DriverCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Drivers by criteria: {}", criteria);

        Page<DriverDTO> page = driverQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drivers/count} : count all the drivers.
     */
    @GetMapping("/drivers/count")
    public ResponseEntity<Long> countDrivers(DriverCriteria criteria) {
        log.debug("REST request to count Drivers by criteria: {}", criteria);
        return ResponseEntity.ok().body(driverQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /drivers/:id} : get the "id" driver.
     */
    @GetMapping("/drivers/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long id) {
        log.debug("REST request to get Driver : {}", id);
        Optional<DriverDTO> driverDTO = driverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driverDTO);
    }

    /**
     * {@code DELETE  /drivers/:id} : delete the "id" driver.
     */
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
