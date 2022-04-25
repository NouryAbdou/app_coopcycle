package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurantsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantsDTO.class);
        RestaurantsDTO restaurantsDTO1 = new RestaurantsDTO();
        restaurantsDTO1.setId(1L);
        RestaurantsDTO restaurantsDTO2 = new RestaurantsDTO();
        assertThat(restaurantsDTO1).isNotEqualTo(restaurantsDTO2);
        restaurantsDTO2.setId(restaurantsDTO1.getId());
        assertThat(restaurantsDTO1).isEqualTo(restaurantsDTO2);
        restaurantsDTO2.setId(2L);
        assertThat(restaurantsDTO1).isNotEqualTo(restaurantsDTO2);
        restaurantsDTO1.setId(null);
        assertThat(restaurantsDTO1).isNotEqualTo(restaurantsDTO2);
    }
}
