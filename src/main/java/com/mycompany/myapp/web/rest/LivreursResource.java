package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LivreursRepository;
import com.mycompany.myapp.service.LivreursService;
import com.mycompany.myapp.service.dto.LivreursDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Livreurs}.
 */
@RestController
@RequestMapping("/api")
public class LivreursResource {

    private final Logger log = LoggerFactory.getLogger(LivreursResource.class);

    private static final String ENTITY_NAME = "livreurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivreursService livreursService;

    private final LivreursRepository livreursRepository;

    public LivreursResource(LivreursService livreursService, LivreursRepository livreursRepository) {
        this.livreursService = livreursService;
        this.livreursRepository = livreursRepository;
    }

    /**
     * {@code POST  /livreurs} : Create a new livreurs.
     *
     * @param livreursDTO the livreursDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livreursDTO, or with status {@code 400 (Bad Request)} if the livreurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livreurs")
    public ResponseEntity<LivreursDTO> createLivreurs(@Valid @RequestBody LivreursDTO livreursDTO) throws URISyntaxException {
        log.debug("REST request to save Livreurs : {}", livreursDTO);
        if (livreursDTO.getId() != null) {
            throw new BadRequestAlertException("A new livreurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LivreursDTO result = livreursService.save(livreursDTO);
        return ResponseEntity
            .created(new URI("/api/livreurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livreurs/:id} : Updates an existing livreurs.
     *
     * @param id the id of the livreursDTO to save.
     * @param livreursDTO the livreursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreursDTO,
     * or with status {@code 400 (Bad Request)} if the livreursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livreursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livreurs/{id}")
    public ResponseEntity<LivreursDTO> updateLivreurs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivreursDTO livreursDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livreurs : {}, {}", id, livreursDTO);
        if (livreursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livreursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LivreursDTO result = livreursService.update(livreursDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livreursDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /livreurs/:id} : Partial updates given fields of an existing livreurs, field will ignore if it is null
     *
     * @param id the id of the livreursDTO to save.
     * @param livreursDTO the livreursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreursDTO,
     * or with status {@code 400 (Bad Request)} if the livreursDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livreursDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livreursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livreurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivreursDTO> partialUpdateLivreurs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivreursDTO livreursDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livreurs partially : {}, {}", id, livreursDTO);
        if (livreursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livreursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivreursDTO> result = livreursService.partialUpdate(livreursDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livreursDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livreurs} : get all the livreurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livreurs in body.
     */
    @GetMapping("/livreurs")
    public List<LivreursDTO> getAllLivreurs() {
        log.debug("REST request to get all Livreurs");
        return livreursService.findAll();
    }

    /**
     * {@code GET  /livreurs/:id} : get the "id" livreurs.
     *
     * @param id the id of the livreursDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livreursDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livreurs/{id}")
    public ResponseEntity<LivreursDTO> getLivreurs(@PathVariable Long id) {
        log.debug("REST request to get Livreurs : {}", id);
        Optional<LivreursDTO> livreursDTO = livreursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livreursDTO);
    }

    /**
     * {@code DELETE  /livreurs/:id} : delete the "id" livreurs.
     *
     * @param id the id of the livreursDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livreurs/{id}")
    public ResponseEntity<Void> deleteLivreurs(@PathVariable Long id) {
        log.debug("REST request to delete Livreurs : {}", id);
        livreursService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
