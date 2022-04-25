package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cooperatives;
import com.mycompany.myapp.domain.Livreurs;
import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.service.dto.CooperativesDTO;
import com.mycompany.myapp.service.dto.LivreursDTO;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreurs} and its DTO {@link LivreursDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreursMapper extends EntityMapper<LivreursDTO, Livreurs> {
    @Mapping(target = "restaurateur", source = "restaurateur", qualifiedByName = "restaurateursId")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativesId")
    LivreursDTO toDto(Livreurs s);

    @Named("restaurateursId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurateursDTO toDtoRestaurateursId(Restaurateurs restaurateurs);

    @Named("cooperativesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativesDTO toDtoCooperativesId(Cooperatives cooperatives);
}
