package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Checkin;
import com.mycompany.myapp.repository.CheckinRepository;
import com.mycompany.myapp.service.criteria.CheckinCriteria;
import com.mycompany.myapp.service.dto.CheckinDTO;
import com.mycompany.myapp.service.mapper.CheckinMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Checkin} entities in the database.
 * The main input is a {@link CheckinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CheckinDTO} or a {@link Page} of {@link CheckinDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CheckinQueryService extends QueryService<Checkin> {

    private final Logger log = LoggerFactory.getLogger(CheckinQueryService.class);

    private final CheckinRepository checkinRepository;

    private final CheckinMapper checkinMapper;

    public CheckinQueryService(CheckinRepository checkinRepository, CheckinMapper checkinMapper) {
        this.checkinRepository = checkinRepository;
        this.checkinMapper = checkinMapper;
    }

    /**
     * Return a {@link List} of {@link CheckinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CheckinDTO> findByCriteria(CheckinCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Checkin> specification = createSpecification(criteria);
        return checkinMapper.toDto(checkinRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CheckinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CheckinDTO> findByCriteria(CheckinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Checkin> specification = createSpecification(criteria);
        return checkinRepository.findAll(specification, page).map(checkinMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CheckinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Checkin> specification = createSpecification(criteria);
        return checkinRepository.count(specification);
    }

    /**
     * Function to convert {@link CheckinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Checkin> createSpecification(CheckinCriteria criteria) {
        Specification<Checkin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Checkin_.id));
            }
            if (criteria.getCheckinTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckinTime(), Checkin_.checkinTime));
            }
            if (criteria.getCheckoutTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckoutTime(), Checkin_.checkoutTime));
            }
            if (criteria.getFaceVerified() != null) {
                specification = specification.and(buildSpecification(criteria.getFaceVerified(), Checkin_.faceVerified));
            }
            if (criteria.getTripId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTripId(), root -> root.join(Checkin_.trip, JoinType.LEFT).get(Trip_.id))
                    );
            }
        }
        return specification;
    }
}
