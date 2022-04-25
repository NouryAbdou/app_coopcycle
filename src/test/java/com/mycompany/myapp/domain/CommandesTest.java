package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commandes.class);
        Commandes commandes1 = new Commandes();
        commandes1.setId("id1");
        Commandes commandes2 = new Commandes();
        commandes2.setId(commandes1.getId());
        assertThat(commandes1).isEqualTo(commandes2);
        commandes2.setId("id2");
        assertThat(commandes1).isNotEqualTo(commandes2);
        commandes1.setId(null);
        assertThat(commandes1).isNotEqualTo(commandes2);
    }
}
