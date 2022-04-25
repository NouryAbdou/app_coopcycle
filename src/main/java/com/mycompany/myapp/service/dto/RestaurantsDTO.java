package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Restaurants} entity.
 */
public class RestaurantsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String nom;

    @NotNull
    private String carte;

    private String menu;

    private RestaurateursDTO restaurateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCarte() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public RestaurateursDTO getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(RestaurateursDTO restaurateur) {
        this.restaurateur = restaurateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantsDTO)) {
            return false;
        }

        RestaurantsDTO restaurantsDTO = (RestaurantsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantsDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", carte='" + getCarte() + "'" +
            ", menu='" + getMenu() + "'" +
            ", restaurateur=" + getRestaurateur() +
            "}";
    }
}
