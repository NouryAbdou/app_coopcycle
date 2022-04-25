package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Commandes;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CommandesRepositoryWithBagRelationshipsImpl implements CommandesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Commandes> fetchBagRelationships(Optional<Commandes> commandes) {
        return commandes.map(this::fetchRestaurants);
    }

    @Override
    public Page<Commandes> fetchBagRelationships(Page<Commandes> commandes) {
        return new PageImpl<>(fetchBagRelationships(commandes.getContent()), commandes.getPageable(), commandes.getTotalElements());
    }

    @Override
    public List<Commandes> fetchBagRelationships(List<Commandes> commandes) {
        return Optional.of(commandes).map(this::fetchRestaurants).orElse(Collections.emptyList());
    }

    Commandes fetchRestaurants(Commandes result) {
        return entityManager
            .createQuery(
                "select commandes from Commandes commandes left join fetch commandes.restaurants where commandes is :commandes",
                Commandes.class
            )
            .setParameter("commandes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Commandes> fetchRestaurants(List<Commandes> commandes) {
        return entityManager
            .createQuery(
                "select distinct commandes from Commandes commandes left join fetch commandes.restaurants where commandes in :commandes",
                Commandes.class
            )
            .setParameter("commandes", commandes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
