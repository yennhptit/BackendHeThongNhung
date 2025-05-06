package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.domain.enumeration.ViolationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the Violation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long>, JpaSpecificationExecutor<Violation> {
    List<Violation> findAllByDriver(Driver driver);

    long countByTypeAndTimestampBetween(ViolationType type, Instant start, Instant end);
}
