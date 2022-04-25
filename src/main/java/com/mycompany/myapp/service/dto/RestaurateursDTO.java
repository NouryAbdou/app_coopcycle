package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Restaurateurs} entity.
 */
public class RestaurateursDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String nom;

    @NotNull
    @Size(min = 5)
    private String prenom;

    private String city;

    private CooperativesDTO cooperative;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CooperativesDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativesDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurateursDTO)) {
            return false;
        }

        RestaurateursDTO restaurateursDTO = (RestaurateursDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurateursDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurateursDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", city='" + getCity() + "'" +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
