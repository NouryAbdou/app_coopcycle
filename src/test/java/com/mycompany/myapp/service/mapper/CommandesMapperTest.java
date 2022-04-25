package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandesMapperTest {

    private CommandesMapper commandesMapper;

    @BeforeEach
    public void setUp() {
        commandesMapper = new CommandesMapperImpl();
    }
}
