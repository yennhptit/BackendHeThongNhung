package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.service.dto.ViolationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Violation} and its DTO {@link ViolationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ViolationMapper extends EntityMapper<ViolationDTO, Violation> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    ViolationDTO toDto(Violation s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);
}
