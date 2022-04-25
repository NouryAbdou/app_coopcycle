package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Zones;
import com.mycompany.myapp.service.dto.ZonesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Zones} and its DTO {@link ZonesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ZonesMapper extends EntityMapper<ZonesDTO, Zones> {}
