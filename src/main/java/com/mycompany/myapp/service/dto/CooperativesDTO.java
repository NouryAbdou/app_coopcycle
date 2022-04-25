package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Cooperatives} entity.
 */
public class CooperativesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String nom;

    private ZonesDTO zone;

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

    public ZonesDTO getZone() {
        return zone;
    }

    public void setZone(ZonesDTO zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativesDTO)) {
            return false;
        }

        CooperativesDTO cooperativesDTO = (CooperativesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cooperativesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativesDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", zone=" + getZone() +
            "}";
    }
}
