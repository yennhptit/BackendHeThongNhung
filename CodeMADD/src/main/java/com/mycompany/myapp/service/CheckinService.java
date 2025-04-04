package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CheckinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Checkin}.
 */
public interface CheckinService {
    /**
     * Save a checkin.
     *
     * @param checkinDTO the entity to save.
     * @return the persisted entity.
     */
    CheckinDTO save(CheckinDTO checkinDTO);

    /**
     * Updates a checkin.
     *
     * @param checkinDTO the entity to update.
     * @return the persisted entity.
     */
    CheckinDTO update(CheckinDTO checkinDTO);

    /**
     * Partially updates a checkin.
     *
     * @param checkinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CheckinDTO> partialUpdate(CheckinDTO checkinDTO);

    /**
     * Get all the checkins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CheckinDTO> findAll(Pageable pageable);

    /**
     * Get the "id" checkin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CheckinDTO> findOne(Long id);

    /**
     * Delete the "id" checkin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
