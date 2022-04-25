package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Livreurs;
import com.mycompany.myapp.repository.LivreursRepository;
import com.mycompany.myapp.service.dto.LivreursDTO;
import com.mycompany.myapp.service.mapper.LivreursMapper;
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
 * Integration tests for the {@link LivreursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivreursResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreursRepository livreursRepository;

    @Autowired
    private LivreursMapper livreursMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivreursMockMvc;

    private Livreurs livreurs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreurs createEntity(EntityManager em) {
        Livreurs livreurs = new Livreurs().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM).city(DEFAULT_CITY);
        return livreurs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreurs createUpdatedEntity(EntityManager em) {
        Livreurs livreurs = new Livreurs().nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);
        return livreurs;
    }

    @BeforeEach
    public void initTest() {
        livreurs = createEntity(em);
    }

    @Test
    @Transactional
    void createLivreurs() throws Exception {
        int databaseSizeBeforeCreate = livreursRepository.findAll().size();
        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);
        restLivreursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isCreated());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeCreate + 1);
        Livreurs testLivreurs = livreursList.get(livreursList.size() - 1);
        assertThat(testLivreurs.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLivreurs.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testLivreurs.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    void createLivreursWithExistingId() throws Exception {
        // Create the Livreurs with an existing ID
        livreurs.setId(1L);
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        int databaseSizeBeforeCreate = livreursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivreursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreursRepository.findAll().size();
        // set the field null
        livreurs.setNom(null);

        // Create the Livreurs, which fails.
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        restLivreursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isBadRequest());

        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreursRepository.findAll().size();
        // set the field null
        livreurs.setPrenom(null);

        // Create the Livreurs, which fails.
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        restLivreursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isBadRequest());

        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreursRepository.findAll().size();
        // set the field null
        livreurs.setCity(null);

        // Create the Livreurs, which fails.
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        restLivreursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isBadRequest());

        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivreurs() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        // Get all the livreursList
        restLivreursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livreurs.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    @Transactional
    void getLivreurs() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        // Get the livreurs
        restLivreursMockMvc
            .perform(get(ENTITY_API_URL_ID, livreurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livreurs.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    void getNonExistingLivreurs() throws Exception {
        // Get the livreurs
        restLivreursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivreurs() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();

        // Update the livreurs
        Livreurs updatedLivreurs = livreursRepository.findById(livreurs.getId()).get();
        // Disconnect from session so that the updates on updatedLivreurs are not directly saved in db
        em.detach(updatedLivreurs);
        updatedLivreurs.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);
        LivreursDTO livreursDTO = livreursMapper.toDto(updatedLivreurs);

        restLivreursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livreursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
        Livreurs testLivreurs = livreursList.get(livreursList.size() - 1);
        assertThat(testLivreurs.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLivreurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLivreurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void putNonExistingLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livreursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivreursWithPatch() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();

        // Update the livreurs using partial update
        Livreurs partialUpdatedLivreurs = new Livreurs();
        partialUpdatedLivreurs.setId(livreurs.getId());

        partialUpdatedLivreurs.prenom(UPDATED_PRENOM).city(UPDATED_CITY);

        restLivreursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreurs))
            )
            .andExpect(status().isOk());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
        Livreurs testLivreurs = livreursList.get(livreursList.size() - 1);
        assertThat(testLivreurs.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLivreurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLivreurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void fullUpdateLivreursWithPatch() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();

        // Update the livreurs using partial update
        Livreurs partialUpdatedLivreurs = new Livreurs();
        partialUpdatedLivreurs.setId(livreurs.getId());

        partialUpdatedLivreurs.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).city(UPDATED_CITY);

        restLivreursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreurs))
            )
            .andExpect(status().isOk());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
        Livreurs testLivreurs = livreursList.get(livreursList.size() - 1);
        assertThat(testLivreurs.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLivreurs.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLivreurs.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void patchNonExistingLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livreursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivreurs() throws Exception {
        int databaseSizeBeforeUpdate = livreursRepository.findAll().size();
        livreurs.setId(count.incrementAndGet());

        // Create the Livreurs
        LivreursDTO livreursDTO = livreursMapper.toDto(livreurs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreursMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livreursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreurs in the database
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivreurs() throws Exception {
        // Initialize the database
        livreursRepository.saveAndFlush(livreurs);

        int databaseSizeBeforeDelete = livreursRepository.findAll().size();

        // Delete the livreurs
        restLivreursMockMvc
            .perform(delete(ENTITY_API_URL_ID, livreurs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livreurs> livreursList = livreursRepository.findAll();
        assertThat(livreursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
