package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurants;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurants, Long> {}
