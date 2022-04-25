package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativesDTO.class);
        CooperativesDTO cooperativesDTO1 = new CooperativesDTO();
        cooperativesDTO1.setId(1L);
        CooperativesDTO cooperativesDTO2 = new CooperativesDTO();
        assertThat(cooperativesDTO1).isNotEqualTo(cooperativesDTO2);
        cooperativesDTO2.setId(cooperativesDTO1.getId());
        assertThat(cooperativesDTO1).isEqualTo(cooperativesDTO2);
        cooperativesDTO2.setId(2L);
        assertThat(cooperativesDTO1).isNotEqualTo(cooperativesDTO2);
        cooperativesDTO1.setId(null);
        assertThat(cooperativesDTO1).isNotEqualTo(cooperativesDTO2);
    }
}
