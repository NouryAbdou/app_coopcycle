package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Zones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Zones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZonesRepository extends JpaRepository<Zones, Long> {}
