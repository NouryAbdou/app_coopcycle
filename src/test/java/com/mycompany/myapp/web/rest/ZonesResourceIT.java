package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Zones;
import com.mycompany.myapp.repository.ZonesRepository;
import com.mycompany.myapp.service.dto.ZonesDTO;
import com.mycompany.myapp.service.mapper.ZonesMapper;
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
 * Integration tests for the {@link ZonesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZonesResourceIT {

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_METROPOLE = "AAAAAAAAAA";
    private static final String UPDATED_METROPOLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNAUTE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNAUTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZonesRepository zonesRepository;

    @Autowired
    private ZonesMapper zonesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZonesMockMvc;

    private Zones zones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zones createEntity(EntityManager em) {
        Zones zones = new Zones().ville(DEFAULT_VILLE).metropole(DEFAULT_METROPOLE).communaute(DEFAULT_COMMUNAUTE);
        return zones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zones createUpdatedEntity(EntityManager em) {
        Zones zones = new Zones().ville(UPDATED_VILLE).metropole(UPDATED_METROPOLE).communaute(UPDATED_COMMUNAUTE);
        return zones;
    }

    @BeforeEach
    public void initTest() {
        zones = createEntity(em);
    }

    @Test
    @Transactional
    void createZones() throws Exception {
        int databaseSizeBeforeCreate = zonesRepository.findAll().size();
        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);
        restZonesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonesDTO)))
            .andExpect(status().isCreated());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeCreate + 1);
        Zones testZones = zonesList.get(zonesList.size() - 1);
        assertThat(testZones.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testZones.getMetropole()).isEqualTo(DEFAULT_METROPOLE);
        assertThat(testZones.getCommunaute()).isEqualTo(DEFAULT_COMMUNAUTE);
    }

    @Test
    @Transactional
    void createZonesWithExistingId() throws Exception {
        // Create the Zones with an existing ID
        zones.setId(1L);
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        int databaseSizeBeforeCreate = zonesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZonesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = zonesRepository.findAll().size();
        // set the field null
        zones.setVille(null);

        // Create the Zones, which fails.
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        restZonesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonesDTO)))
            .andExpect(status().isBadRequest());

        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllZones() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        // Get all the zonesList
        restZonesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zones.getId().intValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].metropole").value(hasItem(DEFAULT_METROPOLE)))
            .andExpect(jsonPath("$.[*].communaute").value(hasItem(DEFAULT_COMMUNAUTE)));
    }

    @Test
    @Transactional
    void getZones() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        // Get the zones
        restZonesMockMvc
            .perform(get(ENTITY_API_URL_ID, zones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zones.getId().intValue()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.metropole").value(DEFAULT_METROPOLE))
            .andExpect(jsonPath("$.communaute").value(DEFAULT_COMMUNAUTE));
    }

    @Test
    @Transactional
    void getNonExistingZones() throws Exception {
        // Get the zones
        restZonesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewZones() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();

        // Update the zones
        Zones updatedZones = zonesRepository.findById(zones.getId()).get();
        // Disconnect from session so that the updates on updatedZones are not directly saved in db
        em.detach(updatedZones);
        updatedZones.ville(UPDATED_VILLE).metropole(UPDATED_METROPOLE).communaute(UPDATED_COMMUNAUTE);
        ZonesDTO zonesDTO = zonesMapper.toDto(updatedZones);

        restZonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zonesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
        Zones testZones = zonesList.get(zonesList.size() - 1);
        assertThat(testZones.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testZones.getMetropole()).isEqualTo(UPDATED_METROPOLE);
        assertThat(testZones.getCommunaute()).isEqualTo(UPDATED_COMMUNAUTE);
    }

    @Test
    @Transactional
    void putNonExistingZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zonesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZonesWithPatch() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();

        // Update the zones using partial update
        Zones partialUpdatedZones = new Zones();
        partialUpdatedZones.setId(zones.getId());

        partialUpdatedZones.communaute(UPDATED_COMMUNAUTE);

        restZonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZones))
            )
            .andExpect(status().isOk());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
        Zones testZones = zonesList.get(zonesList.size() - 1);
        assertThat(testZones.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testZones.getMetropole()).isEqualTo(DEFAULT_METROPOLE);
        assertThat(testZones.getCommunaute()).isEqualTo(UPDATED_COMMUNAUTE);
    }

    @Test
    @Transactional
    void fullUpdateZonesWithPatch() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();

        // Update the zones using partial update
        Zones partialUpdatedZones = new Zones();
        partialUpdatedZones.setId(zones.getId());

        partialUpdatedZones.ville(UPDATED_VILLE).metropole(UPDATED_METROPOLE).communaute(UPDATED_COMMUNAUTE);

        restZonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZones))
            )
            .andExpect(status().isOk());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
        Zones testZones = zonesList.get(zonesList.size() - 1);
        assertThat(testZones.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testZones.getMetropole()).isEqualTo(UPDATED_METROPOLE);
        assertThat(testZones.getCommunaute()).isEqualTo(UPDATED_COMMUNAUTE);
    }

    @Test
    @Transactional
    void patchNonExistingZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zonesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZones() throws Exception {
        int databaseSizeBeforeUpdate = zonesRepository.findAll().size();
        zones.setId(count.incrementAndGet());

        // Create the Zones
        ZonesDTO zonesDTO = zonesMapper.toDto(zones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(zonesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zones in the database
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZones() throws Exception {
        // Initialize the database
        zonesRepository.saveAndFlush(zones);

        int databaseSizeBeforeDelete = zonesRepository.findAll().size();

        // Delete the zones
        restZonesMockMvc
            .perform(delete(ENTITY_API_URL_ID, zones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zones> zonesList = zonesRepository.findAll();
        assertThat(zonesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
