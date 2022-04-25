package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CommandesRepository;
import com.mycompany.myapp.service.CommandesService;
import com.mycompany.myapp.service.dto.CommandesDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Commandes}.
 */
@RestController
@RequestMapping("/api")
public class CommandesResource {

    private final Logger log = LoggerFactory.getLogger(CommandesResource.class);

    private static final String ENTITY_NAME = "commandes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandesService commandesService;

    private final CommandesRepository commandesRepository;

    public CommandesResource(CommandesService commandesService, CommandesRepository commandesRepository) {
        this.commandesService = commandesService;
        this.commandesRepository = commandesRepository;
    }

    /**
     * {@code POST  /commandes} : Create a new commandes.
     *
     * @param commandesDTO the commandesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandesDTO, or with status {@code 400 (Bad Request)} if the commandes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commandes")
    public ResponseEntity<CommandesDTO> createCommandes(@RequestBody CommandesDTO commandesDTO) throws URISyntaxException {
        log.debug("REST request to save Commandes : {}", commandesDTO);
        if (commandesDTO.getId() != null) {
            throw new BadRequestAlertException("A new commandes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommandesDTO result = commandesService.save(commandesDTO);
        return ResponseEntity
            .created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /commandes/:id} : Updates an existing commandes.
     *
     * @param id the id of the commandesDTO to save.
     * @param commandesDTO the commandesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandesDTO,
     * or with status {@code 400 (Bad Request)} if the commandesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commandes/{id}")
    public ResponseEntity<CommandesDTO> updateCommandes(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CommandesDTO commandesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Commandes : {}, {}", id, commandesDTO);
        if (commandesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommandesDTO result = commandesService.update(commandesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandesDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /commandes/:id} : Partial updates given fields of an existing commandes, field will ignore if it is null
     *
     * @param id the id of the commandesDTO to save.
     * @param commandesDTO the commandesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandesDTO,
     * or with status {@code 400 (Bad Request)} if the commandesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commandesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommandesDTO> partialUpdateCommandes(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CommandesDTO commandesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commandes partially : {}, {}", id, commandesDTO);
        if (commandesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommandesDTO> result = commandesService.partialUpdate(commandesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandesDTO.getId())
        );
    }

    /**
     * {@code GET  /commandes} : get all the commandes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandes in body.
     */
    @GetMapping("/commandes")
    public List<CommandesDTO> getAllCommandes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Commandes");
        return commandesService.findAll();
    }

    /**
     * {@code GET  /commandes/:id} : get the "id" commandes.
     *
     * @param id the id of the commandesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commandes/{id}")
    public ResponseEntity<CommandesDTO> getCommandes(@PathVariable String id) {
        log.debug("REST request to get Commandes : {}", id);
        Optional<CommandesDTO> commandesDTO = commandesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commandesDTO);
    }

    /**
     * {@code DELETE  /commandes/:id} : delete the "id" commandes.
     *
     * @param id the id of the commandesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commandes/{id}")
    public ResponseEntity<Void> deleteCommandes(@PathVariable String id) {
        log.debug("REST request to delete Commandes : {}", id);
        commandesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
