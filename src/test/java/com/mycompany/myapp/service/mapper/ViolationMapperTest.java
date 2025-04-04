package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ViolationMapperTest {

    private ViolationMapper violationMapper;

    @BeforeEach
    public void setUp() {
        violationMapper = new ViolationMapperImpl();
    }
}
