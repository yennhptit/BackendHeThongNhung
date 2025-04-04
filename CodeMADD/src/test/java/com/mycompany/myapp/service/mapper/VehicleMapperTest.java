package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    @BeforeEach
    public void setUp() {
        vehicleMapper = new VehicleMapperImpl();
    }
}
