package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.dto.ViolationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Violation} and its DTO {@link ViolationDTO}.
 */
@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface ViolationMapper extends EntityMapper<ViolationDTO, Violation> {

    @Mapping(target = "driver", source = "driver")
    ViolationDTO toDto(Violation s);

    @Mapping(target = "driver", source = "driver")
    Violation toEntity(ViolationDTO dto);
}
