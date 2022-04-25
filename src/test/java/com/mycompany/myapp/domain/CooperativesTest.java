package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cooperatives.class);
        Cooperatives cooperatives1 = new Cooperatives();
        cooperatives1.setId(1L);
        Cooperatives cooperatives2 = new Cooperatives();
        cooperatives2.setId(cooperatives1.getId());
        assertThat(cooperatives1).isEqualTo(cooperatives2);
        cooperatives2.setId(2L);
        assertThat(cooperatives1).isNotEqualTo(cooperatives2);
        cooperatives1.setId(null);
        assertThat(cooperatives1).isNotEqualTo(cooperatives2);
    }
}
