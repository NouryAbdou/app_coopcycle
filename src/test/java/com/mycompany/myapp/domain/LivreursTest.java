package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivreursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livreurs.class);
        Livreurs livreurs1 = new Livreurs();
        livreurs1.setId(1L);
        Livreurs livreurs2 = new Livreurs();
        livreurs2.setId(livreurs1.getId());
        assertThat(livreurs1).isEqualTo(livreurs2);
        livreurs2.setId(2L);
        assertThat(livreurs1).isNotEqualTo(livreurs2);
        livreurs1.setId(null);
        assertThat(livreurs1).isNotEqualTo(livreurs2);
    }
}
