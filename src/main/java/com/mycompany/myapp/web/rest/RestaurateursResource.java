package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RestaurateursRepository;
import com.mycompany.myapp.service.RestaurateursService;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Restaurateurs}.
 */
@RestController
@RequestMapping("/api")
public class RestaurateursResource {

    private final Logger log = LoggerFactory.getLogger(RestaurateursResource.class);

    private static final String ENTITY_NAME = "restaurateurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurateursService restaurateursService;

    private final RestaurateursRepository restaurateursRepository;

    public RestaurateursResource(RestaurateursService restaurateursService, RestaurateursRepository restaurateursRepository) {
        this.restaurateursService = restaurateursService;
        this.restaurateursRepository = restaurateursRepository;
    }

    /**
     * {@code POST  /restaurateurs} : Create a new restaurateurs.
     *
     * @param restaurateursDTO the restaurateursDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurateursDTO, or with status {@code 400 (Bad Request)} if the restaurateurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurateurs")
    public ResponseEntity<RestaurateursDTO> createRestaurateurs(@Valid @RequestBody RestaurateursDTO restaurateursDTO)
        throws URISyntaxException {
        log.debug("REST request to save Restaurateurs : {}", restaurateursDTO);
        if (restaurateursDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurateurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurateursDTO result = restaurateursService.save(restaurateursDTO);
        return ResponseEntity
            .created(new URI("/api/restaurateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurateurs/:id} : Updates an existing restaurateurs.
     *
     * @param id the id of the restaurateursDTO to save.
     * @param restaurateursDTO the restaurateursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateursDTO,
     * or with status {@code 400 (Bad Request)} if the restaurateursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurateursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurateurs/{id}")
    public ResponseEntity<RestaurateursDTO> updateRestaurateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RestaurateursDTO restaurateursDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurateurs : {}, {}", id, restaurateursDTO);
        if (restaurateursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurateursDTO result = restaurateursService.update(restaurateursDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateursDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurateurs/:id} : Partial updates given fields of an existing restaurateurs, field will ignore if it is null
     *
     * @param id the id of the restaurateursDTO to save.
     * @param restaurateursDTO the restaurateursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateursDTO,
     * or with status {@code 400 (Bad Request)} if the restaurateursDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurateursDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurateursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurateurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurateursDTO> partialUpdateRestaurateurs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RestaurateursDTO restaurateursDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurateurs partially : {}, {}", id, restaurateursDTO);
        if (restaurateursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurateursDTO> result = restaurateursService.partialUpdate(restaurateursDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateursDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurateurs} : get all the restaurateurs.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurateurs in body.
     */
    @GetMapping("/restaurateurs")
    public List<RestaurateursDTO> getAllRestaurateurs(@RequestParam(required = false) String filter) {
        if ("client-is-null".equals(filter)) {
            log.debug("REST request to get all Restaurateurss where client is null");
            return restaurateursService.findAllWhereClientIsNull();
        }

        if ("livreur-is-null".equals(filter)) {
            log.debug("REST request to get all Restaurateurss where livreur is null");
            return restaurateursService.findAllWhereLivreurIsNull();
        }
        log.debug("REST request to get all Restaurateurs");
        return restaurateursService.findAll();
    }

    /**
     * {@code GET  /restaurateurs/:id} : get the "id" restaurateurs.
     *
     * @param id the id of the restaurateursDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurateursDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurateurs/{id}")
    public ResponseEntity<RestaurateursDTO> getRestaurateurs(@PathVariable Long id) {
        log.debug("REST request to get Restaurateurs : {}", id);
        Optional<RestaurateursDTO> restaurateursDTO = restaurateursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurateursDTO);
    }

    /**
     * {@code DELETE  /restaurateurs/:id} : delete the "id" restaurateurs.
     *
     * @param id the id of the restaurateursDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurateurs/{id}")
    public ResponseEntity<Void> deleteRestaurateurs(@PathVariable Long id) {
        log.debug("REST request to delete Restaurateurs : {}", id);
        restaurateursService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
