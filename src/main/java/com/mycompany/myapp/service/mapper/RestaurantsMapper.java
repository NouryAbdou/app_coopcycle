package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Restaurants;
import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.service.dto.RestaurantsDTO;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurants} and its DTO {@link RestaurantsDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurantsMapper extends EntityMapper<RestaurantsDTO, Restaurants> {
    @Mapping(target = "restaurateur", source = "restaurateur", qualifiedByName = "restaurateursId")
    RestaurantsDTO toDto(Restaurants s);

    @Named("restaurateursId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurateursDTO toDtoRestaurateursId(Restaurateurs restaurateurs);
}
