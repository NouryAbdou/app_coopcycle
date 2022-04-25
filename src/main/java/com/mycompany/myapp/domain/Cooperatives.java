package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cooperatives.
 */
@Entity
@Table(name = "cooperatives")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cooperatives implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cooperatives" }, allowSetters = true)
    private Zones zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cooperatives id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Cooperatives nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Zones getZone() {
        return this.zone;
    }

    public void setZone(Zones zones) {
        this.zone = zones;
    }

    public Cooperatives zone(Zones zones) {
        this.setZone(zones);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperatives)) {
            return false;
        }
        return id != null && id.equals(((Cooperatives) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperatives{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
