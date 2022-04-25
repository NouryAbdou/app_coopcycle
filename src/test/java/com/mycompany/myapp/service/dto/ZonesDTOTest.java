package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZonesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZonesDTO.class);
        ZonesDTO zonesDTO1 = new ZonesDTO();
        zonesDTO1.setId(1L);
        ZonesDTO zonesDTO2 = new ZonesDTO();
        assertThat(zonesDTO1).isNotEqualTo(zonesDTO2);
        zonesDTO2.setId(zonesDTO1.getId());
        assertThat(zonesDTO1).isEqualTo(zonesDTO2);
        zonesDTO2.setId(2L);
        assertThat(zonesDTO1).isNotEqualTo(zonesDTO2);
        zonesDTO1.setId(null);
        assertThat(zonesDTO1).isNotEqualTo(zonesDTO2);
    }
}
