package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Restaurants;
import com.mycompany.myapp.repository.RestaurantsRepository;
import com.mycompany.myapp.service.dto.RestaurantsDTO;
import com.mycompany.myapp.service.mapper.RestaurantsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestaurantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantsResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CARTE = "AAAAAAAAAA";
    private static final String UPDATED_CARTE = "BBBBBBBBBB";

    private static final String DEFAULT_MENU = "AAAAAAAAAA";
    private static final String UPDATED_MENU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Autowired
    private RestaurantsMapper restaurantsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantsMockMvc;

    private Restaurants restaurants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurants createEntity(EntityManager em) {
        Restaurants restaurants = new Restaurants().nom(DEFAULT_NOM).carte(DEFAULT_CARTE).menu(DEFAULT_MENU);
        return restaurants;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurants createUpdatedEntity(EntityManager em) {
        Restaurants restaurants = new Restaurants().nom(UPDATED_NOM).carte(UPDATED_CARTE).menu(UPDATED_MENU);
        return restaurants;
    }

    @BeforeEach
    public void initTest() {
        restaurants = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurants() throws Exception {
        int databaseSizeBeforeCreate = restaurantsRepository.findAll().size();
        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);
        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRestaurants.getCarte()).isEqualTo(DEFAULT_CARTE);
        assertThat(testRestaurants.getMenu()).isEqualTo(DEFAULT_MENU);
    }

    @Test
    @Transactional
    void createRestaurantsWithExistingId() throws Exception {
        // Create the Restaurants with an existing ID
        restaurants.setId(1L);
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        int databaseSizeBeforeCreate = restaurantsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantsRepository.findAll().size();
        // set the field null
        restaurants.setNom(null);

        // Create the Restaurants, which fails.
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCarteIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantsRepository.findAll().size();
        // set the field null
        restaurants.setCarte(null);

        // Create the Restaurants, which fails.
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurants.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].carte").value(hasItem(DEFAULT_CARTE)))
            .andExpect(jsonPath("$.[*].menu").value(hasItem(DEFAULT_MENU)));
    }

    @Test
    @Transactional
    void getRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get the restaurants
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurants.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.carte").value(DEFAULT_CARTE))
            .andExpect(jsonPath("$.menu").value(DEFAULT_MENU));
    }

    @Test
    @Transactional
    void getNonExistingRestaurants() throws Exception {
        // Get the restaurants
        restRestaurantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants
        Restaurants updatedRestaurants = restaurantsRepository.findById(restaurants.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurants are not directly saved in db
        em.detach(updatedRestaurants);
        updatedRestaurants.nom(UPDATED_NOM).carte(UPDATED_CARTE).menu(UPDATED_MENU);
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(updatedRestaurants);

        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRestaurants.getCarte()).isEqualTo(UPDATED_CARTE);
        assertThat(testRestaurants.getMenu()).isEqualTo(UPDATED_MENU);
    }

    @Test
    @Transactional
    void putNonExistingRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantsWithPatch() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants using partial update
        Restaurants partialUpdatedRestaurants = new Restaurants();
        partialUpdatedRestaurants.setId(restaurants.getId());

        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurants))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRestaurants.getCarte()).isEqualTo(DEFAULT_CARTE);
        assertThat(testRestaurants.getMenu()).isEqualTo(DEFAULT_MENU);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantsWithPatch() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants using partial update
        Restaurants partialUpdatedRestaurants = new Restaurants();
        partialUpdatedRestaurants.setId(restaurants.getId());

        partialUpdatedRestaurants.nom(UPDATED_NOM).carte(UPDATED_CARTE).menu(UPDATED_MENU);

        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurants))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRestaurants.getCarte()).isEqualTo(UPDATED_CARTE);
        assertThat(testRestaurants.getMenu()).isEqualTo(UPDATED_MENU);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeDelete = restaurantsRepository.findAll().size();

        // Delete the restaurants
        restRestaurantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
