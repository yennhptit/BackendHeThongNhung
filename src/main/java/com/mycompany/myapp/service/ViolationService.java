package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ViolationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Violation}.
 */
public interface ViolationService {
    /**
     * Save a violation.
     *
     * @param violationDTO the entity to save.
     * @return the persisted entity.
     */
    ViolationDTO save(ViolationDTO violationDTO);

    /**
     * Updates a violation.
     *
     * @param violationDTO the entity to update.
     * @return the persisted entity.
     */
    ViolationDTO update(ViolationDTO violationDTO);

    /**
     * Partially updates a violation.
     *
     * @param violationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ViolationDTO> partialUpdate(ViolationDTO violationDTO);

    /**
     * Get all the violations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ViolationDTO> findAll(Pageable pageable);

    Page<ViolationDTO> findAllIncludingDeleted(Pageable pageable);

    /**
     * Get the "id" violation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ViolationDTO> findOne(Long id);

    /**
     * Delete the "id" violation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
