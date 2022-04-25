package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Commandes} entity.
 */
public class CommandesDTO implements Serializable {

    private String id;

    private Boolean estPret;

    private Boolean estPaye;

    private ClientsDTO client;

    private Set<RestaurantsDTO> restaurants = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEstPret() {
        return estPret;
    }

    public void setEstPret(Boolean estPret) {
        this.estPret = estPret;
    }

    public Boolean getEstPaye() {
        return estPaye;
    }

    public void setEstPaye(Boolean estPaye) {
        this.estPaye = estPaye;
    }

    public ClientsDTO getClient() {
        return client;
    }

    public void setClient(ClientsDTO client) {
        this.client = client;
    }

    public Set<RestaurantsDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<RestaurantsDTO> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandesDTO)) {
            return false;
        }

        CommandesDTO commandesDTO = (CommandesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandesDTO{" +
            "id='" + getId() + "'" +
            ", estPret='" + getEstPret() + "'" +
            ", estPaye='" + getEstPaye() + "'" +
            ", client=" + getClient() +
            ", restaurants=" + getRestaurants() +
            "}";
    }
}
