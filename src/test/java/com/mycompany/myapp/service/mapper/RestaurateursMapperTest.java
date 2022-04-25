package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurateursMapperTest {

    private RestaurateursMapper restaurateursMapper;

    @BeforeEach
    public void setUp() {
        restaurateursMapper = new RestaurateursMapperImpl();
    }
}
