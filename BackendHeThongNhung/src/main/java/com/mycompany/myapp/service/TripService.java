package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TripDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Trip}.
 */
public interface TripService {
    /**
     * Save a trip.
     *
     * @param tripDTO the entity to save.
     * @return the persisted entity.
     */
    TripDTO save(TripDTO tripDTO);

    /**
     * Updates a trip.
     *
     * @param tripDTO the entity to update.
     * @return the persisted entity.
     */
    TripDTO update(TripDTO tripDTO);

    /**
     * Partially updates a trip.
     *
     * @param tripDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripDTO> partialUpdate(TripDTO tripDTO);

    /**
     * Get all the trips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TripDTO> findAll(Pageable pageable);

    /**
     * Get all the TripDTO where Checkin is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TripDTO> findAllWhereCheckinIsNull();

    /**
     * Get the "id" trip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripDTO> findOne(Long id);

    /**
     * Delete the "id" trip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
