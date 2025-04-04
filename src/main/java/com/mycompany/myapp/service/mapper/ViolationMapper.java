package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.ViolationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Violation} and its DTO {@link ViolationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ViolationMapper extends EntityMapper<ViolationDTO, Violation> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "driverId")
    ViolationDTO toDto(Violation s);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
