package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZonesMapperTest {

    private ZonesMapper zonesMapper;

    @BeforeEach
    public void setUp() {
        zonesMapper = new ZonesMapperImpl();
    }
}
