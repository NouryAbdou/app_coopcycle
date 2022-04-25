package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ClientsRepository;
import com.mycompany.myapp.service.ClientsService;
import com.mycompany.myapp.service.dto.ClientsDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Clients}.
 */
@RestController
@RequestMapping("/api")
public class ClientsResource {

    private final Logger log = LoggerFactory.getLogger(ClientsResource.class);

    private static final String ENTITY_NAME = "clients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientsService clientsService;

    private final ClientsRepository clientsRepository;

    public ClientsResource(ClientsService clientsService, ClientsRepository clientsRepository) {
        this.clientsService = clientsService;
        this.clientsRepository = clientsRepository;
    }

    /**
     * {@code POST  /clients} : Create a new clients.
     *
     * @param clientsDTO the clientsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientsDTO, or with status {@code 400 (Bad Request)} if the clients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public ResponseEntity<ClientsDTO> createClients(@Valid @RequestBody ClientsDTO clientsDTO) throws URISyntaxException {
        log.debug("REST request to save Clients : {}", clientsDTO);
        if (clientsDTO.getId() != null) {
            throw new BadRequestAlertException("A new clients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientsDTO result = clientsService.save(clientsDTO);
        return ResponseEntity
            .created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing clients.
     *
     * @param id the id of the clientsDTO to save.
     * @param clientsDTO the clientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientsDTO,
     * or with status {@code 400 (Bad Request)} if the clientsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientsDTO> updateClients(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientsDTO clientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Clients : {}, {}", id, clientsDTO);
        if (clientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClientsDTO result = clientsService.update(clientsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clients/:id} : Partial updates given fields of an existing clients, field will ignore if it is null
     *
     * @param id the id of the clientsDTO to save.
     * @param clientsDTO the clientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientsDTO,
     * or with status {@code 400 (Bad Request)} if the clientsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientsDTO> partialUpdateClients(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientsDTO clientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clients partially : {}, {}", id, clientsDTO);
        if (clientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientsDTO> result = clientsService.partialUpdate(clientsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public List<ClientsDTO> getAllClients() {
        log.debug("REST request to get all Clients");
        return clientsService.findAll();
    }

    /**
     * {@code GET  /clients/:id} : get the "id" clients.
     *
     * @param id the id of the clientsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientsDTO> getClients(@PathVariable Long id) {
        log.debug("REST request to get Clients : {}", id);
        Optional<ClientsDTO> clientsDTO = clientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientsDTO);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" clients.
     *
     * @param id the id of the clientsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClients(@PathVariable Long id) {
        log.debug("REST request to delete Clients : {}", id);
        clientsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
