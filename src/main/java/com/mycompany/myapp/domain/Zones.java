package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Zones.
 */
@Entity
@Table(name = "zones")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Zones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "metropole")
    private String metropole;

    @Column(name = "communaute")
    private String communaute;

    @OneToMany(mappedBy = "zone")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "zone" }, allowSetters = true)
    private Set<Cooperatives> cooperatives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Zones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVille() {
        return this.ville;
    }

    public Zones ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMetropole() {
        return this.metropole;
    }

    public Zones metropole(String metropole) {
        this.setMetropole(metropole);
        return this;
    }

    public void setMetropole(String metropole) {
        this.metropole = metropole;
    }

    public String getCommunaute() {
        return this.communaute;
    }

    public Zones communaute(String communaute) {
        this.setCommunaute(communaute);
        return this;
    }

    public void setCommunaute(String communaute) {
        this.communaute = communaute;
    }

    public Set<Cooperatives> getCooperatives() {
        return this.cooperatives;
    }

    public void setCooperatives(Set<Cooperatives> cooperatives) {
        if (this.cooperatives != null) {
            this.cooperatives.forEach(i -> i.setZone(null));
        }
        if (cooperatives != null) {
            cooperatives.forEach(i -> i.setZone(this));
        }
        this.cooperatives = cooperatives;
    }

    public Zones cooperatives(Set<Cooperatives> cooperatives) {
        this.setCooperatives(cooperatives);
        return this;
    }

    public Zones addCooperatives(Cooperatives cooperatives) {
        this.cooperatives.add(cooperatives);
        cooperatives.setZone(this);
        return this;
    }

    public Zones removeCooperatives(Cooperatives cooperatives) {
        this.cooperatives.remove(cooperatives);
        cooperatives.setZone(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zones)) {
            return false;
        }
        return id != null && id.equals(((Zones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Zones{" +
            "id=" + getId() +
            ", ville='" + getVille() + "'" +
            ", metropole='" + getMetropole() + "'" +
            ", communaute='" + getCommunaute() + "'" +
            "}";
    }
}
