package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cooperatives;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cooperatives entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CooperativesRepository extends JpaRepository<Cooperatives, Long> {}
