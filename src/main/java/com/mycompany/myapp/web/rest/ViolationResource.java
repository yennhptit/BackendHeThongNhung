package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ViolationRepository;
import com.mycompany.myapp.service.ViolationQueryService;
import com.mycompany.myapp.service.ViolationService;
import com.mycompany.myapp.service.criteria.ViolationCriteria;
import com.mycompany.myapp.service.dto.ViolationDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Violation}.
 */
@RestController
@RequestMapping("/api")
public class ViolationResource {

    private final Logger log = LoggerFactory.getLogger(ViolationResource.class);

    private static final String ENTITY_NAME = "violation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViolationService violationService;

    private final ViolationRepository violationRepository;

    private final ViolationQueryService violationQueryService;

    public ViolationResource(
        ViolationService violationService,
        ViolationRepository violationRepository,
        ViolationQueryService violationQueryService
    ) {
        this.violationService = violationService;
        this.violationRepository = violationRepository;
        this.violationQueryService = violationQueryService;
    }

    @Operation(summary = "Update an existing violation", description = "Update a violation by its ID")
    @PutMapping("/violations/{id}")
    public ResponseEntity<ViolationDTO> updateViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViolationDTO violationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Violation : {}, {}", id, violationDTO);
        if (violationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, violationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!violationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ViolationDTO result = violationService.update(violationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, violationDTO.getId().toString()))
            .body(result);
    }

    @Operation(summary = "Get all violations with pagination", description = "Retrieve a paginated list of all violations")
    @GetMapping("/violations")
//    public ResponseEntity<List<ViolationDTO>> getAllViolations(
//        @RequestParam(defaultValue = "0") int pageNumber,
//        @RequestParam(defaultValue = "10") int pageSize
//    ) {
//        log.debug("REST request to get all Violations with pagination");
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<ViolationDTO> page = violationService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
    public ResponseEntity<List<ViolationDTO>> getAllViolations() {
        List<ViolationDTO> violationDTOS = violationService.findAll();

        return ResponseEntity.ok().body(violationDTOS);
    }

    @Operation(summary = "Get all violations including deleted", description = "Retrieve all violations including those marked as deleted")
    @GetMapping("/violations/all")
    public ResponseEntity<List<ViolationDTO>> getAllTripsIncludingDeleted(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ViolationDTO> page = violationService.findAllIncludingDeleted(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "Get a violation by ID", description = "Retrieve details of a specific violation by ID")
    @GetMapping("/violations/{id}")
    public ResponseEntity<ViolationDTO> getViolation(@PathVariable Long id) {
        log.debug("REST request to get Violation : {}", id);
        Optional<ViolationDTO> violationDTO = violationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(violationDTO);
    }

    @Operation(summary = "Delete a violation", description = "Delete a violation by its ID")
    @DeleteMapping("/violations/{id}")
    public ResponseEntity<Void> deleteViolation(@PathVariable Long id) {
        log.debug("REST request to delete Violation : {}", id);
        violationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
