package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Commandes;
import com.mycompany.myapp.repository.CommandesRepository;
import com.mycompany.myapp.service.CommandesService;
import com.mycompany.myapp.service.dto.CommandesDTO;
import com.mycompany.myapp.service.mapper.CommandesMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommandesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommandesResourceIT {

    private static final Boolean DEFAULT_EST_PRET = false;
    private static final Boolean UPDATED_EST_PRET = true;

    private static final Boolean DEFAULT_EST_PAYE = false;
    private static final Boolean UPDATED_EST_PAYE = true;

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CommandesRepository commandesRepository;

    @Mock
    private CommandesRepository commandesRepositoryMock;

    @Autowired
    private CommandesMapper commandesMapper;

    @Mock
    private CommandesService commandesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandesMockMvc;

    private Commandes commandes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commandes createEntity(EntityManager em) {
        Commandes commandes = new Commandes().estPret(DEFAULT_EST_PRET).estPaye(DEFAULT_EST_PAYE);
        return commandes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commandes createUpdatedEntity(EntityManager em) {
        Commandes commandes = new Commandes().estPret(UPDATED_EST_PRET).estPaye(UPDATED_EST_PAYE);
        return commandes;
    }

    @BeforeEach
    public void initTest() {
        commandes = createEntity(em);
    }

    @Test
    @Transactional
    void createCommandes() throws Exception {
        int databaseSizeBeforeCreate = commandesRepository.findAll().size();
        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);
        restCommandesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandesDTO)))
            .andExpect(status().isCreated());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeCreate + 1);
        Commandes testCommandes = commandesList.get(commandesList.size() - 1);
        assertThat(testCommandes.getEstPret()).isEqualTo(DEFAULT_EST_PRET);
        assertThat(testCommandes.getEstPaye()).isEqualTo(DEFAULT_EST_PAYE);
    }

    @Test
    @Transactional
    void createCommandesWithExistingId() throws Exception {
        // Create the Commandes with an existing ID
        commandes.setId("existing_id");
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        int databaseSizeBeforeCreate = commandesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommandes() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        // Get all the commandesList
        restCommandesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandes.getId())))
            .andExpect(jsonPath("$.[*].estPret").value(hasItem(DEFAULT_EST_PRET.booleanValue())))
            .andExpect(jsonPath("$.[*].estPaye").value(hasItem(DEFAULT_EST_PAYE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(commandesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commandesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(commandesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commandesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCommandes() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        // Get the commandes
        restCommandesMockMvc
            .perform(get(ENTITY_API_URL_ID, commandes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commandes.getId()))
            .andExpect(jsonPath("$.estPret").value(DEFAULT_EST_PRET.booleanValue()))
            .andExpect(jsonPath("$.estPaye").value(DEFAULT_EST_PAYE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCommandes() throws Exception {
        // Get the commandes
        restCommandesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommandes() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();

        // Update the commandes
        Commandes updatedCommandes = commandesRepository.findById(commandes.getId()).get();
        // Disconnect from session so that the updates on updatedCommandes are not directly saved in db
        em.detach(updatedCommandes);
        updatedCommandes.estPret(UPDATED_EST_PRET).estPaye(UPDATED_EST_PAYE);
        CommandesDTO commandesDTO = commandesMapper.toDto(updatedCommandes);

        restCommandesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
        Commandes testCommandes = commandesList.get(commandesList.size() - 1);
        assertThat(testCommandes.getEstPret()).isEqualTo(UPDATED_EST_PRET);
        assertThat(testCommandes.getEstPaye()).isEqualTo(UPDATED_EST_PAYE);
    }

    @Test
    @Transactional
    void putNonExistingCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandesWithPatch() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();

        // Update the commandes using partial update
        Commandes partialUpdatedCommandes = new Commandes();
        partialUpdatedCommandes.setId(commandes.getId());

        partialUpdatedCommandes.estPret(UPDATED_EST_PRET);

        restCommandesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandes))
            )
            .andExpect(status().isOk());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
        Commandes testCommandes = commandesList.get(commandesList.size() - 1);
        assertThat(testCommandes.getEstPret()).isEqualTo(UPDATED_EST_PRET);
        assertThat(testCommandes.getEstPaye()).isEqualTo(DEFAULT_EST_PAYE);
    }

    @Test
    @Transactional
    void fullUpdateCommandesWithPatch() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();

        // Update the commandes using partial update
        Commandes partialUpdatedCommandes = new Commandes();
        partialUpdatedCommandes.setId(commandes.getId());

        partialUpdatedCommandes.estPret(UPDATED_EST_PRET).estPaye(UPDATED_EST_PAYE);

        restCommandesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandes))
            )
            .andExpect(status().isOk());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
        Commandes testCommandes = commandesList.get(commandesList.size() - 1);
        assertThat(testCommandes.getEstPret()).isEqualTo(UPDATED_EST_PRET);
        assertThat(testCommandes.getEstPaye()).isEqualTo(UPDATED_EST_PAYE);
    }

    @Test
    @Transactional
    void patchNonExistingCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommandes() throws Exception {
        int databaseSizeBeforeUpdate = commandesRepository.findAll().size();
        commandes.setId(UUID.randomUUID().toString());

        // Create the Commandes
        CommandesDTO commandesDTO = commandesMapper.toDto(commandes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commandesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commandes in the database
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommandes() throws Exception {
        // Initialize the database
        commandes.setId(UUID.randomUUID().toString());
        commandesRepository.saveAndFlush(commandes);

        int databaseSizeBeforeDelete = commandesRepository.findAll().size();

        // Delete the commandes
        restCommandesMockMvc
            .perform(delete(ENTITY_API_URL_ID, commandes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commandes> commandesList = commandesRepository.findAll();
        assertThat(commandesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
