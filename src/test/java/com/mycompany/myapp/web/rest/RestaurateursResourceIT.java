package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.repository.RestaurateursRepository;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import com.mycompany.myapp.service.mapper.RestaurateursMapper;
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
 * Integration tests for the {@link RestaurateursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurateursResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurateursRepository restaurateursRepository;

    @Autowired
    private RestaurateursMapper restaurateursMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurateursMockMvc;

    private Restaurateurs restaurateurs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateurs createEntity(EntityManager em) {
        Restaurateurs restaurateurs = new Restaurateurs().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM).city(DEFAULT_CITY);
        return restaurateurs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateurs createUpdatedEntity(EntityManager em) {
        Restaurateurs restaurateurs = new Restaurateurs().nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);
        return restaurateurs;
    }

    @BeforeEach
    public void initTest() {
        restaurateurs = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurateurs() throws Exception {
        int databaseSizeBeforeCreate = restaurateursRepository.findAll().size();
        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);
        restRestaurateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurateurs testRestaurateurs = restaurateursList.get(restaurateursList.size() - 1);
        assertThat(testRestaurateurs.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRestaurateurs.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testRestaurateurs.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    void createRestaurateursWithExistingId() throws Exception {
        // Create the Restaurateurs with an existing ID
        restaurateurs.setId(1L);
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        int databaseSizeBeforeCreate = restaurateursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurateursRepository.findAll().size();
        // set the field null
        restaurateurs.setNom(null);

        // Create the Restaurateurs, which fails.
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        restRestaurateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurateursRepository.findAll().size();
        // set the field null
        restaurateurs.setPrenom(null);

        // Create the Restaurateurs, which fails.
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        restRestaurateursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurateurs() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        // Get all the restaurateursList
        restRestaurateursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurateurs.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    @Transactional
    void getRestaurateurs() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        // Get the restaurateurs
        restRestaurateursMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurateurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurateurs.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    void getNonExistingRestaurateurs() throws Exception {
        // Get the restaurateurs
        restRestaurateursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurateurs() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();

        // Update the restaurateurs
        Restaurateurs updatedRestaurateurs = restaurateursRepository.findById(restaurateurs.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurateurs are not directly saved in db
        em.detach(updatedRestaurateurs);
        updatedRestaurateurs.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(updatedRestaurateurs);

        restRestaurateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurateursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
        Restaurateurs testRestaurateurs = restaurateursList.get(restaurateursList.size() - 1);
        assertThat(testRestaurateurs.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRestaurateurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testRestaurateurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void putNonExistingRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurateursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurateursWithPatch() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();

        // Update the restaurateurs using partial update
        Restaurateurs partialUpdatedRestaurateurs = new Restaurateurs();
        partialUpdatedRestaurateurs.setId(restaurateurs.getId());

        partialUpdatedRestaurateurs.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);

        restRestaurateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateurs))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
        Restaurateurs testRestaurateurs = restaurateursList.get(restaurateursList.size() - 1);
        assertThat(testRestaurateurs.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRestaurateurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testRestaurateurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void fullUpdateRestaurateursWithPatch() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();

        // Update the restaurateurs using partial update
        Restaurateurs partialUpdatedRestaurateurs = new Restaurateurs();
        partialUpdatedRestaurateurs.setId(restaurateurs.getId());

        partialUpdatedRestaurateurs.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);

        restRestaurateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateurs))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
        Restaurateurs testRestaurateurs = restaurateursList.get(restaurateursList.size() - 1);
        assertThat(testRestaurateurs.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRestaurateurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testRestaurateurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurateursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurateurs() throws Exception {
        int databaseSizeBeforeUpdate = restaurateursRepository.findAll().size();
        restaurateurs.setId(count.incrementAndGet());

        // Create the Restaurateurs
        RestaurateursDTO restaurateursDTO = restaurateursMapper.toDto(restaurateurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateurs in the database
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurateurs() throws Exception {
        // Initialize the database
        restaurateursRepository.saveAndFlush(restaurateurs);

        int databaseSizeBeforeDelete = restaurateursRepository.findAll().size();

        // Delete the restaurateurs
        restRestaurateursMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurateurs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurateurs> restaurateursList = restaurateursRepository.findAll();
        assertThat(restaurateursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
