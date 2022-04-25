package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Clients;
import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.service.dto.ClientsDTO;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Clients} and its DTO {@link ClientsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientsMapper extends EntityMapper<ClientsDTO, Clients> {
    @Mapping(target = "restaurateur", source = "restaurateur", qualifiedByName = "restaurateursId")
    ClientsDTO toDto(Clients s);

    @Named("restaurateursId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurateursDTO toDtoRestaurateursId(Restaurateurs restaurateurs);
}
