package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cooperatives;
import com.mycompany.myapp.domain.Zones;
import com.mycompany.myapp.service.dto.CooperativesDTO;
import com.mycompany.myapp.service.dto.ZonesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cooperatives} and its DTO {@link CooperativesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativesMapper extends EntityMapper<CooperativesDTO, Cooperatives> {
    @Mapping(target = "zone", source = "zone", qualifiedByName = "zonesId")
    CooperativesDTO toDto(Cooperatives s);

    @Named("zonesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ZonesDTO toDtoZonesId(Zones zones);
}
