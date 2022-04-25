package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurateursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurateurs.class);
        Restaurateurs restaurateurs1 = new Restaurateurs();
        restaurateurs1.setId(1L);
        Restaurateurs restaurateurs2 = new Restaurateurs();
        restaurateurs2.setId(restaurateurs1.getId());
        assertThat(restaurateurs1).isEqualTo(restaurateurs2);
        restaurateurs2.setId(2L);
        assertThat(restaurateurs1).isNotEqualTo(restaurateurs2);
        restaurateurs1.setId(null);
        assertThat(restaurateurs1).isNotEqualTo(restaurateurs2);
    }
}
