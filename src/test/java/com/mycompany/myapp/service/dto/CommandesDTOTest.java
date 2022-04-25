package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandesDTO.class);
        CommandesDTO commandesDTO1 = new CommandesDTO();
        commandesDTO1.setId("id1");
        CommandesDTO commandesDTO2 = new CommandesDTO();
        assertThat(commandesDTO1).isNotEqualTo(commandesDTO2);
        commandesDTO2.setId(commandesDTO1.getId());
        assertThat(commandesDTO1).isEqualTo(commandesDTO2);
        commandesDTO2.setId("id2");
        assertThat(commandesDTO1).isNotEqualTo(commandesDTO2);
        commandesDTO1.setId(null);
        assertThat(commandesDTO1).isNotEqualTo(commandesDTO2);
    }
}
