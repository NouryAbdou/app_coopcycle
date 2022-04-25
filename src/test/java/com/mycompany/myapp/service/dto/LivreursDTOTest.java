package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivreursDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivreursDTO.class);
        LivreursDTO livreursDTO1 = new LivreursDTO();
        livreursDTO1.setId(1L);
        LivreursDTO livreursDTO2 = new LivreursDTO();
        assertThat(livreursDTO1).isNotEqualTo(livreursDTO2);
        livreursDTO2.setId(livreursDTO1.getId());
        assertThat(livreursDTO1).isEqualTo(livreursDTO2);
        livreursDTO2.setId(2L);
        assertThat(livreursDTO1).isNotEqualTo(livreursDTO2);
        livreursDTO1.setId(null);
        assertThat(livreursDTO1).isNotEqualTo(livreursDTO2);
    }
}
