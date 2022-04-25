package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ZonesRepository;
import com.mycompany.myapp.service.ZonesService;
import com.mycompany.myapp.service.dto.ZonesDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Zones}.
 */
@RestController
@RequestMapping("/api")
public class ZonesResource {

    private final Logger log = LoggerFactory.getLogger(ZonesResource.class);

    private static final String ENTITY_NAME = "zones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZonesService zonesService;

    private final ZonesRepository zonesRepository;

    public ZonesResource(ZonesService zonesService, ZonesRepository zonesRepository) {
        this.zonesService = zonesService;
        this.zonesRepository = zonesRepository;
    }

    /**
     * {@code POST  /zones} : Create a new zones.
     *
     * @param zonesDTO the zonesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zonesDTO, or with status {@code 400 (Bad Request)} if the zones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zones")
    public ResponseEntity<ZonesDTO> createZones(@Valid @RequestBody ZonesDTO zonesDTO) throws URISyntaxException {
        log.debug("REST request to save Zones : {}", zonesDTO);
        if (zonesDTO.getId() != null) {
            throw new BadRequestAlertException("A new zones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ZonesDTO result = zonesService.save(zonesDTO);
        return ResponseEntity
            .created(new URI("/api/zones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zones/:id} : Updates an existing zones.
     *
     * @param id the id of the zonesDTO to save.
     * @param zonesDTO the zonesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zonesDTO,
     * or with status {@code 400 (Bad Request)} if the zonesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zonesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zones/{id}")
    public ResponseEntity<ZonesDTO> updateZones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ZonesDTO zonesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Zones : {}, {}", id, zonesDTO);
        if (zonesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zonesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ZonesDTO result = zonesService.update(zonesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zonesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /zones/:id} : Partial updates given fields of an existing zones, field will ignore if it is null
     *
     * @param id the id of the zonesDTO to save.
     * @param zonesDTO the zonesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zonesDTO,
     * or with status {@code 400 (Bad Request)} if the zonesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the zonesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the zonesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/zones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ZonesDTO> partialUpdateZones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ZonesDTO zonesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Zones partially : {}, {}", id, zonesDTO);
        if (zonesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zonesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ZonesDTO> result = zonesService.partialUpdate(zonesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zonesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /zones} : get all the zones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zones in body.
     */
    @GetMapping("/zones")
    public List<ZonesDTO> getAllZones() {
        log.debug("REST request to get all Zones");
        return zonesService.findAll();
    }

    /**
     * {@code GET  /zones/:id} : get the "id" zones.
     *
     * @param id the id of the zonesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zonesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zones/{id}")
    public ResponseEntity<ZonesDTO> getZones(@PathVariable Long id) {
        log.debug("REST request to get Zones : {}", id);
        Optional<ZonesDTO> zonesDTO = zonesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zonesDTO);
    }

    /**
     * {@code DELETE  /zones/:id} : delete the "id" zones.
     *
     * @param id the id of the zonesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zones/{id}")
    public ResponseEntity<Void> deleteZones(@PathVariable Long id) {
        log.debug("REST request to delete Zones : {}", id);
        zonesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
