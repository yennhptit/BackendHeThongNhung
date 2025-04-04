package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CheckinRepository;
import com.mycompany.myapp.service.CheckinQueryService;
import com.mycompany.myapp.service.CheckinService;
import com.mycompany.myapp.service.criteria.CheckinCriteria;
import com.mycompany.myapp.service.dto.CheckinDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Checkin}.
 */
@RestController
@RequestMapping("/api")
public class CheckinResource {

    private final Logger log = LoggerFactory.getLogger(CheckinResource.class);

    private static final String ENTITY_NAME = "checkin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckinService checkinService;

    private final CheckinRepository checkinRepository;

    private final CheckinQueryService checkinQueryService;

    public CheckinResource(CheckinService checkinService, CheckinRepository checkinRepository, CheckinQueryService checkinQueryService) {
        this.checkinService = checkinService;
        this.checkinRepository = checkinRepository;
        this.checkinQueryService = checkinQueryService;
    }

    /**
     * {@code POST  /checkins} : Create a new checkin.
     *
     * @param checkinDTO the checkinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkinDTO, or with status {@code 400 (Bad Request)} if the checkin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/checkins")
    public ResponseEntity<CheckinDTO> createCheckin(@RequestBody CheckinDTO checkinDTO) throws URISyntaxException {
        log.debug("REST request to save Checkin : {}", checkinDTO);
        if (checkinDTO.getId() != null) {
            throw new BadRequestAlertException("A new checkin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CheckinDTO result = checkinService.save(checkinDTO);
        return ResponseEntity
            .created(new URI("/api/checkins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /checkins/:id} : Updates an existing checkin.
     *
     * @param id the id of the checkinDTO to save.
     * @param checkinDTO the checkinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkinDTO,
     * or with status {@code 400 (Bad Request)} if the checkinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/checkins/{id}")
    public ResponseEntity<CheckinDTO> updateCheckin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CheckinDTO checkinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Checkin : {}, {}", id, checkinDTO);
        if (checkinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CheckinDTO result = checkinService.update(checkinDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, checkinDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /checkins/:id} : Partial updates given fields of an existing checkin, field will ignore if it is null
     *
     * @param id the id of the checkinDTO to save.
     * @param checkinDTO the checkinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkinDTO,
     * or with status {@code 400 (Bad Request)} if the checkinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the checkinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the checkinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/checkins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CheckinDTO> partialUpdateCheckin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CheckinDTO checkinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Checkin partially : {}, {}", id, checkinDTO);
        if (checkinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CheckinDTO> result = checkinService.partialUpdate(checkinDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, checkinDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /checkins} : get all the checkins.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkins in body.
     */
    @GetMapping("/checkins")
    public ResponseEntity<List<CheckinDTO>> getAllCheckins(
        CheckinCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Checkins by criteria: {}", criteria);

        Page<CheckinDTO> page = checkinQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /checkins/count} : count all the checkins.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/checkins/count")
    public ResponseEntity<Long> countCheckins(CheckinCriteria criteria) {
        log.debug("REST request to count Checkins by criteria: {}", criteria);
        return ResponseEntity.ok().body(checkinQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /checkins/:id} : get the "id" checkin.
     *
     * @param id the id of the checkinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/checkins/{id}")
    public ResponseEntity<CheckinDTO> getCheckin(@PathVariable Long id) {
        log.debug("REST request to get Checkin : {}", id);
        Optional<CheckinDTO> checkinDTO = checkinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(checkinDTO);
    }

    /**
     * {@code DELETE  /checkins/:id} : delete the "id" checkin.
     *
     * @param id the id of the checkinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/checkins/{id}")
    public ResponseEntity<Void> deleteCheckin(@PathVariable Long id) {
        log.debug("REST request to delete Checkin : {}", id);
        checkinService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
