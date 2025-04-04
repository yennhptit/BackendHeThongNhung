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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

    /**
     * {@code POST  /violations} : Create a new violation.
     *
     * @param violationDTO the violationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new violationDTO, or with status {@code 400 (Bad Request)} if the violation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/violations")
    public ResponseEntity<ViolationDTO> createViolation(@RequestBody ViolationDTO violationDTO) throws URISyntaxException {
        log.debug("REST request to save Violation : {}", violationDTO);
        if (violationDTO.getId() != null) {
            throw new BadRequestAlertException("A new violation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViolationDTO result = violationService.save(violationDTO);
        return ResponseEntity
            .created(new URI("/api/violations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /violations/:id} : Updates an existing violation.
     *
     * @param id the id of the violationDTO to save.
     * @param violationDTO the violationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated violationDTO,
     * or with status {@code 400 (Bad Request)} if the violationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the violationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code PATCH  /violations/:id} : Partial updates given fields of an existing violation, field will ignore if it is null
     *
     * @param id the id of the violationDTO to save.
     * @param violationDTO the violationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated violationDTO,
     * or with status {@code 400 (Bad Request)} if the violationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the violationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the violationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/violations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViolationDTO> partialUpdateViolation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ViolationDTO violationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Violation partially : {}, {}", id, violationDTO);
        if (violationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, violationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!violationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViolationDTO> result = violationService.partialUpdate(violationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, violationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /violations} : get all the violations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of violations in body.
     */
    @GetMapping("/violations")
    public ResponseEntity<List<ViolationDTO>> getAllViolations(
        ViolationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Violations by criteria: {}", criteria);

        Page<ViolationDTO> page = violationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /violations/count} : count all the violations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/violations/count")
    public ResponseEntity<Long> countViolations(ViolationCriteria criteria) {
        log.debug("REST request to count Violations by criteria: {}", criteria);
        return ResponseEntity.ok().body(violationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /violations/:id} : get the "id" violation.
     *
     * @param id the id of the violationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the violationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/violations/{id}")
    public ResponseEntity<ViolationDTO> getViolation(@PathVariable Long id) {
        log.debug("REST request to get Violation : {}", id);
        Optional<ViolationDTO> violationDTO = violationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(violationDTO);
    }

    /**
     * {@code DELETE  /violations/:id} : delete the "id" violation.
     *
     * @param id the id of the violationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
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
