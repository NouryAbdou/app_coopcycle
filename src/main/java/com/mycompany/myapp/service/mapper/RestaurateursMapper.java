package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cooperatives;
import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.service.dto.CooperativesDTO;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurateurs} and its DTO {@link RestaurateursDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurateursMapper extends EntityMapper<RestaurateursDTO, Restaurateurs> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativesId")
    RestaurateursDTO toDto(Restaurateurs s);

    @Named("cooperativesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativesDTO toDtoCooperativesId(Cooperatives cooperatives);
}
