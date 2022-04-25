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
 * A Restaurateurs.
 */
@Entity
@Table(name = "restaurateurs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Restaurateurs implements Serializable {

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
    @Size(min = 5)
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "restaurateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurateur", "commandes" }, allowSetters = true)
    private Set<Restaurants> restaurants = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "zone" }, allowSetters = true)
    private Cooperatives cooperative;

    @JsonIgnoreProperties(value = { "restaurateur", "commandes" }, allowSetters = true)
    @OneToOne(mappedBy = "restaurateur")
    private Clients client;

    @JsonIgnoreProperties(value = { "restaurateur", "cooperative" }, allowSetters = true)
    @OneToOne(mappedBy = "restaurateur")
    private Livreurs livreur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurateurs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Restaurateurs nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Restaurateurs prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCity() {
        return this.city;
    }

    public Restaurateurs city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Restaurants> getRestaurants() {
        return this.restaurants;
    }

    public void setRestaurants(Set<Restaurants> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.setRestaurateur(null));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.setRestaurateur(this));
        }
        this.restaurants = restaurants;
    }

    public Restaurateurs restaurants(Set<Restaurants> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public Restaurateurs addRestaurants(Restaurants restaurants) {
        this.restaurants.add(restaurants);
        restaurants.setRestaurateur(this);
        return this;
    }

    public Restaurateurs removeRestaurants(Restaurants restaurants) {
        this.restaurants.remove(restaurants);
        restaurants.setRestaurateur(null);
        return this;
    }

    public Cooperatives getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperatives cooperatives) {
        this.cooperative = cooperatives;
    }

    public Restaurateurs cooperative(Cooperatives cooperatives) {
        this.setCooperative(cooperatives);
        return this;
    }

    public Clients getClient() {
        return this.client;
    }

    public void setClient(Clients clients) {
        if (this.client != null) {
            this.client.setRestaurateur(null);
        }
        if (clients != null) {
            clients.setRestaurateur(this);
        }
        this.client = clients;
    }

    public Restaurateurs client(Clients clients) {
        this.setClient(clients);
        return this;
    }

    public Livreurs getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreurs livreurs) {
        if (this.livreur != null) {
            this.livreur.setRestaurateur(null);
        }
        if (livreurs != null) {
            livreurs.setRestaurateur(this);
        }
        this.livreur = livreurs;
    }

    public Restaurateurs livreur(Livreurs livreurs) {
        this.setLivreur(livreurs);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurateurs)) {
            return false;
        }
        return id != null && id.equals(((Restaurateurs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurateurs{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
