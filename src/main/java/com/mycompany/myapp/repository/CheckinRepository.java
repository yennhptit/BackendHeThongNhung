package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Checkin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Checkin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long>, JpaSpecificationExecutor<Checkin> {}
