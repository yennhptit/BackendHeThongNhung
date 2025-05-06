package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring", uses = {DriverMapper.class, VehicleMapper.class})
public interface TripMapper extends EntityMapper<TripDTO, Trip> {

    @Mapping(source = "driver", target = "driver")
    @Mapping(source = "vehicle", target = "vehicle")
    TripDTO toDto(Trip s);

    @Mapping(source = "driver", target = "driver")
    @Mapping(source = "vehicle", target = "vehicle")
    Trip toEntity(TripDTO tripDTO);
}
