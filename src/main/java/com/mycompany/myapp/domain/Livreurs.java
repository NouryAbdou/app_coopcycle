package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Livreurs.
 */
@Entity
@Table(name = "livreurs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Livreurs implements Serializable {

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

    @NotNull
    @Size(min = 10)
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Size(min = 3)
    @Column(name = "city", nullable = false)
    private String city;

    @JsonIgnoreProperties(value = { "restaurants", "cooperative", "client", "livreur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Restaurateurs restaurateur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "zone" }, allowSetters = true)
    private Cooperatives cooperative;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livreurs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Livreurs nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Livreurs prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCity() {
        return this.city;
    }

    public Livreurs city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Restaurateurs getRestaurateur() {
        return this.restaurateur;
    }

    public void setRestaurateur(Restaurateurs restaurateurs) {
        this.restaurateur = restaurateurs;
    }

    public Livreurs restaurateur(Restaurateurs restaurateurs) {
        this.setRestaurateur(restaurateurs);
        return this;
    }

    public Cooperatives getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperatives cooperatives) {
        this.cooperative = cooperatives;
    }

    public Livreurs cooperative(Cooperatives cooperatives) {
        this.setCooperative(cooperatives);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livreurs)) {
            return false;
        }
        return id != null && id.equals(((Livreurs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livreurs{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
