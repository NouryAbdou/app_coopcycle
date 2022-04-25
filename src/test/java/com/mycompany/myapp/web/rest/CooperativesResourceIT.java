package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cooperatives;
import com.mycompany.myapp.repository.CooperativesRepository;
import com.mycompany.myapp.service.dto.CooperativesDTO;
import com.mycompany.myapp.service.mapper.CooperativesMapper;
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
 * Integration tests for the {@link CooperativesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CooperativesResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cooperatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CooperativesRepository cooperativesRepository;

    @Autowired
    private CooperativesMapper cooperativesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCooperativesMockMvc;

    private Cooperatives cooperatives;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperatives createEntity(EntityManager em) {
        Cooperatives cooperatives = new Cooperatives().nom(DEFAULT_NOM);
        return cooperatives;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperatives createUpdatedEntity(EntityManager em) {
        Cooperatives cooperatives = new Cooperatives().nom(UPDATED_NOM);
        return cooperatives;
    }

    @BeforeEach
    public void initTest() {
        cooperatives = createEntity(em);
    }

    @Test
    @Transactional
    void createCooperatives() throws Exception {
        int databaseSizeBeforeCreate = cooperativesRepository.findAll().size();
        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);
        restCooperativesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeCreate + 1);
        Cooperatives testCooperatives = cooperativesList.get(cooperativesList.size() - 1);
        assertThat(testCooperatives.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createCooperativesWithExistingId() throws Exception {
        // Create the Cooperatives with an existing ID
        cooperatives.setId(1L);
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        int databaseSizeBeforeCreate = cooperativesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperativesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperativesRepository.findAll().size();
        // set the field null
        cooperatives.setNom(null);

        // Create the Cooperatives, which fails.
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        restCooperativesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCooperatives() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        // Get all the cooperativesList
        restCooperativesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperatives.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getCooperatives() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        // Get the cooperatives
        restCooperativesMockMvc
            .perform(get(ENTITY_API_URL_ID, cooperatives.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cooperatives.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingCooperatives() throws Exception {
        // Get the cooperatives
        restCooperativesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCooperatives() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();

        // Update the cooperatives
        Cooperatives updatedCooperatives = cooperativesRepository.findById(cooperatives.getId()).get();
        // Disconnect from session so that the updates on updatedCooperatives are not directly saved in db
        em.detach(updatedCooperatives);
        updatedCooperatives.nom(UPDATED_NOM);
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(updatedCooperatives);

        restCooperativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
        Cooperatives testCooperatives = cooperativesList.get(cooperativesList.size() - 1);
        assertThat(testCooperatives.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCooperativesWithPatch() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();

        // Update the cooperatives using partial update
        Cooperatives partialUpdatedCooperatives = new Cooperatives();
        partialUpdatedCooperatives.setId(cooperatives.getId());

        partialUpdatedCooperatives.nom(UPDATED_NOM);

        restCooperativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperatives.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCooperatives))
            )
            .andExpect(status().isOk());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
        Cooperatives testCooperatives = cooperativesList.get(cooperativesList.size() - 1);
        assertThat(testCooperatives.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCooperativesWithPatch() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();

        // Update the cooperatives using partial update
        Cooperatives partialUpdatedCooperatives = new Cooperatives();
        partialUpdatedCooperatives.setId(cooperatives.getId());

        partialUpdatedCooperatives.nom(UPDATED_NOM);

        restCooperativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperatives.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCooperatives))
            )
            .andExpect(status().isOk());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
        Cooperatives testCooperatives = cooperativesList.get(cooperativesList.size() - 1);
        assertThat(testCooperatives.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cooperativesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCooperatives() throws Exception {
        int databaseSizeBeforeUpdate = cooperativesRepository.findAll().size();
        cooperatives.setId(count.incrementAndGet());

        // Create the Cooperatives
        CooperativesDTO cooperativesDTO = cooperativesMapper.toDto(cooperatives);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cooperativesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cooperatives in the database
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCooperatives() throws Exception {
        // Initialize the database
        cooperativesRepository.saveAndFlush(cooperatives);

        int databaseSizeBeforeDelete = cooperativesRepository.findAll().size();

        // Delete the cooperatives
        restCooperativesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cooperatives.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cooperatives> cooperativesList = cooperativesRepository.findAll();
        assertThat(cooperativesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
