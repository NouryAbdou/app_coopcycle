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
 * A Restaurants.
 */
@Entity
@Table(name = "restaurants")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Restaurants implements Serializable {

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
    @Column(name = "carte", nullable = false)
    private String carte;

    @Column(name = "menu")
    private String menu;

    @ManyToOne
    @JsonIgnoreProperties(value = { "restaurants", "cooperative", "client", "livreur" }, allowSetters = true)
    private Restaurateurs restaurateur;

    @ManyToMany(mappedBy = "restaurants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "restaurants" }, allowSetters = true)
    private Set<Commandes> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurants id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Restaurants nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCarte() {
        return this.carte;
    }

    public Restaurants carte(String carte) {
        this.setCarte(carte);
        return this;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    public String getMenu() {
        return this.menu;
    }

    public Restaurants menu(String menu) {
        this.setMenu(menu);
        return this;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Restaurateurs getRestaurateur() {
        return this.restaurateur;
    }

    public void setRestaurateur(Restaurateurs restaurateurs) {
        this.restaurateur = restaurateurs;
    }

    public Restaurants restaurateur(Restaurateurs restaurateurs) {
        this.setRestaurateur(restaurateurs);
        return this;
    }

    public Set<Commandes> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commandes> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.removeRestaurant(this));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.addRestaurant(this));
        }
        this.commandes = commandes;
    }

    public Restaurants commandes(Set<Commandes> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Restaurants addCommande(Commandes commandes) {
        this.commandes.add(commandes);
        commandes.getRestaurants().add(this);
        return this;
    }

    public Restaurants removeCommande(Commandes commandes) {
        this.commandes.remove(commandes);
        commandes.getRestaurants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurants)) {
            return false;
        }
        return id != null && id.equals(((Restaurants) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurants{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", carte='" + getCarte() + "'" +
            ", menu='" + getMenu() + "'" +
            "}";
    }
}
