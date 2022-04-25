package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A Commandes.
 */
@Entity
@Table(name = "commandes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commandes implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "est_pret")
    private Boolean estPret;

    @Column(name = "est_paye")
    private Boolean estPaye;

    @Transient
    private boolean isPersisted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "restaurateur", "commandes" }, allowSetters = true)
    private Clients client;

    @ManyToMany
    @JoinTable(
        name = "rel_commandes__restaurant",
        joinColumns = @JoinColumn(name = "commandes_id"),
        inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurateur", "commandes" }, allowSetters = true)
    private Set<Restaurants> restaurants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Commandes id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEstPret() {
        return this.estPret;
    }

    public Commandes estPret(Boolean estPret) {
        this.setEstPret(estPret);
        return this;
    }

    public void setEstPret(Boolean estPret) {
        this.estPret = estPret;
    }

    public Boolean getEstPaye() {
        return this.estPaye;
    }

    public Commandes estPaye(Boolean estPaye) {
        this.setEstPaye(estPaye);
        return this;
    }

    public void setEstPaye(Boolean estPaye) {
        this.estPaye = estPaye;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Commandes setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public Clients getClient() {
        return this.client;
    }

    public void setClient(Clients clients) {
        this.client = clients;
    }

    public Commandes client(Clients clients) {
        this.setClient(clients);
        return this;
    }

    public Set<Restaurants> getRestaurants() {
        return this.restaurants;
    }

    public void setRestaurants(Set<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }

    public Commandes restaurants(Set<Restaurants> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Commandes addRestaurant(Restaurants restaurants) {
        this.restaurants.add(restaurants);
        restaurants.getCommandes().add(this);
        return this;
    }

    public Commandes removeRestaurant(Restaurants restaurants) {
        this.restaurants.remove(restaurants);
        restaurants.getCommandes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commandes)) {
            return false;
        }
        return id != null && id.equals(((Commandes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commandes{" +
            "id=" + getId() +
            ", estPret='" + getEstPret() + "'" +
            ", estPaye='" + getEstPaye() + "'" +
            "}";
    }
}
