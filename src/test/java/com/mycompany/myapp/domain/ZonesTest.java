package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZonesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zones.class);
        Zones zones1 = new Zones();
        zones1.setId(1L);
        Zones zones2 = new Zones();
        zones2.setId(zones1.getId());
        assertThat(zones1).isEqualTo(zones2);
        zones2.setId(2L);
        assertThat(zones1).isNotEqualTo(zones2);
        zones1.setId(null);
        assertThat(zones1).isNotEqualTo(zones2);
    }
}
