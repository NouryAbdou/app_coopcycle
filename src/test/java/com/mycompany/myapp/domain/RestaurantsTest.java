package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurantsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurants.class);
        Restaurants restaurants1 = new Restaurants();
        restaurants1.setId(1L);
        Restaurants restaurants2 = new Restaurants();
        restaurants2.setId(restaurants1.getId());
        assertThat(restaurants1).isEqualTo(restaurants2);
        restaurants2.setId(2L);
        assertThat(restaurants1).isNotEqualTo(restaurants2);
        restaurants1.setId(null);
        assertThat(restaurants1).isNotEqualTo(restaurants2);
    }
}
