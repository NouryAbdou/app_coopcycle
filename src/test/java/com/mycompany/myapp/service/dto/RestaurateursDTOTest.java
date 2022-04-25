package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurateursDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurateursDTO.class);
        RestaurateursDTO restaurateursDTO1 = new RestaurateursDTO();
        restaurateursDTO1.setId(1L);
        RestaurateursDTO restaurateursDTO2 = new RestaurateursDTO();
        assertThat(restaurateursDTO1).isNotEqualTo(restaurateursDTO2);
        restaurateursDTO2.setId(restaurateursDTO1.getId());
        assertThat(restaurateursDTO1).isEqualTo(restaurateursDTO2);
        restaurateursDTO2.setId(2L);
        assertThat(restaurateursDTO1).isNotEqualTo(restaurateursDTO2);
        restaurateursDTO1.setId(null);
        assertThat(restaurateursDTO1).isNotEqualTo(restaurateursDTO2);
    }
}
