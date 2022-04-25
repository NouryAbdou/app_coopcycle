package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CooperativesRepository;
import com.mycompany.myapp.service.CooperativesService;
import com.mycompany.myapp.service.dto.CooperativesDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Cooperatives}.
 */
@RestController
@RequestMapping("/api")
public class CooperativesResource {

    private final Logger log = LoggerFactory.getLogger(CooperativesResource.class);

    private static final String ENTITY_NAME = "cooperatives";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CooperativesService cooperativesService;

    private final CooperativesRepository cooperativesRepository;

    public CooperativesResource(CooperativesService cooperativesService, CooperativesRepository cooperativesRepository) {
        this.cooperativesService = cooperativesService;
        this.cooperativesRepository = cooperativesRepository;
    }

    /**
     * {@code POST  /cooperatives} : Create a new cooperatives.
     *
     * @param cooperativesDTO the cooperativesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cooperativesDTO, or with status {@code 400 (Bad Request)} if the cooperatives has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cooperatives")
    public ResponseEntity<CooperativesDTO> createCooperatives(@Valid @RequestBody CooperativesDTO cooperativesDTO)
        throws URISyntaxException {
        log.debug("REST request to save Cooperatives : {}", cooperativesDTO);
        if (cooperativesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cooperatives cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CooperativesDTO result = cooperativesService.save(cooperativesDTO);
        return ResponseEntity
            .created(new URI("/api/cooperatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cooperatives/:id} : Updates an existing cooperatives.
     *
     * @param id the id of the cooperativesDTO to save.
     * @param cooperativesDTO the cooperativesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativesDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cooperativesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cooperatives/{id}")
    public ResponseEntity<CooperativesDTO> updateCooperatives(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CooperativesDTO cooperativesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cooperatives : {}, {}", id, cooperativesDTO);
        if (cooperativesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CooperativesDTO result = cooperativesService.update(cooperativesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cooperatives/:id} : Partial updates given fields of an existing cooperatives, field will ignore if it is null
     *
     * @param id the id of the cooperativesDTO to save.
     * @param cooperativesDTO the cooperativesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativesDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cooperativesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cooperativesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cooperatives/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CooperativesDTO> partialUpdateCooperatives(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CooperativesDTO cooperativesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cooperatives partially : {}, {}", id, cooperativesDTO);
        if (cooperativesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CooperativesDTO> result = cooperativesService.partialUpdate(cooperativesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooperatives} : get all the cooperatives.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cooperatives in body.
     */
    @GetMapping("/cooperatives")
    public List<CooperativesDTO> getAllCooperatives() {
        log.debug("REST request to get all Cooperatives");
        return cooperativesService.findAll();
    }

    /**
     * {@code GET  /cooperatives/:id} : get the "id" cooperatives.
     *
     * @param id the id of the cooperativesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cooperativesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cooperatives/{id}")
    public ResponseEntity<CooperativesDTO> getCooperatives(@PathVariable Long id) {
        log.debug("REST request to get Cooperatives : {}", id);
        Optional<CooperativesDTO> cooperativesDTO = cooperativesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cooperativesDTO);
    }

    /**
     * {@code DELETE  /cooperatives/:id} : delete the "id" cooperatives.
     *
     * @param id the id of the cooperativesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cooperatives/{id}")
    public ResponseEntity<Void> deleteCooperatives(@PathVariable Long id) {
        log.debug("REST request to delete Cooperatives : {}", id);
        cooperativesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
