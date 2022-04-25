package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantsMapperTest {

    private RestaurantsMapper restaurantsMapper;

    @BeforeEach
    public void setUp() {
        restaurantsMapper = new RestaurantsMapperImpl();
    }
}
