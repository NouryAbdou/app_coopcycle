package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Livreurs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Livreurs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivreursRepository extends JpaRepository<Livreurs, Long> {}
