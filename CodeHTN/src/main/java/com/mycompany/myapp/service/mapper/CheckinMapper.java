package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Checkin;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.service.dto.CheckinDTO;
import com.mycompany.myapp.service.dto.TripDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Checkin} and its DTO {@link CheckinDTO}.
 */
@Mapper(componentModel = "spring")
public interface CheckinMapper extends EntityMapper<CheckinDTO, Checkin> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    CheckinDTO toDto(Checkin s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);
}
