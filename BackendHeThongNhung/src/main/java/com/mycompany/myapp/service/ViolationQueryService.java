package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.repository.ViolationRepository;
import com.mycompany.myapp.service.criteria.ViolationCriteria;
import com.mycompany.myapp.service.dto.ViolationDTO;
import com.mycompany.myapp.service.mapper.ViolationMapper;
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
 * Service for executing complex queries for {@link Violation} entities in the database.
 * The main input is a {@link ViolationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ViolationDTO} or a {@link Page} of {@link ViolationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ViolationQueryService extends QueryService<Violation> {

    private final Logger log = LoggerFactory.getLogger(ViolationQueryService.class);

    private final ViolationRepository violationRepository;

    private final ViolationMapper violationMapper;

    public ViolationQueryService(ViolationRepository violationRepository, ViolationMapper violationMapper) {
        this.violationRepository = violationRepository;
        this.violationMapper = violationMapper;
    }

    /**
     * Return a {@link List} of {@link ViolationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ViolationDTO> findByCriteria(ViolationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Violation> specification = createSpecification(criteria);
        return violationMapper.toDto(violationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ViolationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ViolationDTO> findByCriteria(ViolationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Violation> specification = createSpecification(criteria);
        return violationRepository.findAll(specification, page).map(violationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViolationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Violation> specification = createSpecification(criteria);
        return violationRepository.count(specification);
    }

    /**
     * Function to convert {@link ViolationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Violation> createSpecification(ViolationCriteria criteria) {
        Specification<Violation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Violation_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), Violation_.value));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Violation_.timestamp));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Violation_.type));
            }
            if (criteria.getTripId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTripId(), root -> root.join(Violation_.trip, JoinType.LEFT).get(Driver_.id))
                    );
            }
        }
        return specification;
    }
}
