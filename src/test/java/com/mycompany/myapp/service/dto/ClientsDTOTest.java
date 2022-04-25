package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientsDTO.class);
        ClientsDTO clientsDTO1 = new ClientsDTO();
        clientsDTO1.setId(1L);
        ClientsDTO clientsDTO2 = new ClientsDTO();
        assertThat(clientsDTO1).isNotEqualTo(clientsDTO2);
        clientsDTO2.setId(clientsDTO1.getId());
        assertThat(clientsDTO1).isEqualTo(clientsDTO2);
        clientsDTO2.setId(2L);
        assertThat(clientsDTO1).isNotEqualTo(clientsDTO2);
        clientsDTO1.setId(null);
        assertThat(clientsDTO1).isNotEqualTo(clientsDTO2);
    }
}
