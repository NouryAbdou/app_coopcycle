package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientsMapperTest {

    private ClientsMapper clientsMapper;

    @BeforeEach
    public void setUp() {
        clientsMapper = new ClientsMapperImpl();
    }
}
