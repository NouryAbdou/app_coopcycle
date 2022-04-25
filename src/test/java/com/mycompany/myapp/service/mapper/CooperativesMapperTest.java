package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativesMapperTest {

    private CooperativesMapper cooperativesMapper;

    @BeforeEach
    public void setUp() {
        cooperativesMapper = new CooperativesMapperImpl();
    }
}
