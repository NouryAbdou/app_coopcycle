package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivreursMapperTest {

    private LivreursMapper livreursMapper;

    @BeforeEach
    public void setUp() {
        livreursMapper = new LivreursMapperImpl();
    }
}
