package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TripMapperTest {

    private TripMapper tripMapper;

    @BeforeEach
    public void setUp() {
        tripMapper = new TripMapperImpl();
    }
}
