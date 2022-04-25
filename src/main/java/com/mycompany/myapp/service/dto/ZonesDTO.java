package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Zones} entity.
 */
public class ZonesDTO implements Serializable {

    private Long id;

    @NotNull
    private String ville;

    private String metropole;

    private String communaute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMetropole() {
        return metropole;
    }

    public void setMetropole(String metropole) {
        this.metropole = metropole;
    }

    public String getCommunaute() {
        return communaute;
    }

    public void setCommunaute(String communaute) {
        this.communaute = communaute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZonesDTO)) {
            return false;
        }

        ZonesDTO zonesDTO = (ZonesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, zonesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZonesDTO{" +
            "id=" + getId() +
            ", ville='" + getVille() + "'" +
            ", metropole='" + getMetropole() + "'" +
            ", communaute='" + getCommunaute() + "'" +
            "}";
    }
}
