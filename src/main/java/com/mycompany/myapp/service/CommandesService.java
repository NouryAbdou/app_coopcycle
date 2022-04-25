package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Commandes;
import com.mycompany.myapp.repository.CommandesRepository;
import com.mycompany.myapp.service.dto.CommandesDTO;
import com.mycompany.myapp.service.mapper.CommandesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commandes}.
 */
@Service
@Transactional
public class CommandesService {

    private final Logger log = LoggerFactory.getLogger(CommandesService.class);

    private final CommandesRepository commandesRepository;

    private final CommandesMapper commandesMapper;

    public CommandesService(CommandesRepository commandesRepository, CommandesMapper commandesMapper) {
        this.commandesRepository = commandesRepository;
        this.commandesMapper = commandesMapper;
    }

    /**
     * Save a commandes.
     *
     * @param commandesDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandesDTO save(CommandesDTO commandesDTO) {
        log.debug("Request to save Commandes : {}", commandesDTO);
        Commandes commandes = commandesMapper.toEntity(commandesDTO);
        commandes = commandesRepository.save(commandes);
        return commandesMapper.toDto(commandes);
    }

    /**
     * Update a commandes.
     *
     * @param commandesDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandesDTO update(CommandesDTO commandesDTO) {
        log.debug("Request to save Commandes : {}", commandesDTO);
        Commandes commandes = commandesMapper.toEntity(commandesDTO);
        commandes.setIsPersisted();
        commandes = commandesRepository.save(commandes);
        return commandesMapper.toDto(commandes);
    }

    /**
     * Partially update a commandes.
     *
     * @param commandesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandesDTO> partialUpdate(CommandesDTO commandesDTO) {
        log.debug("Request to partially update Commandes : {}", commandesDTO);

        return commandesRepository
            .findById(commandesDTO.getId())
            .map(existingCommandes -> {
                commandesMapper.partialUpdate(existingCommandes, commandesDTO);

                return existingCommandes;
            })
            .map(commandesRepository::save)
            .map(commandesMapper::toDto);
    }

    /**
     * Get all the commandes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandesDTO> findAll() {
        log.debug("Request to get all Commandes");
        return commandesRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(commandesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the commandes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CommandesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return commandesRepository.findAllWithEagerRelationships(pageable).map(commandesMapper::toDto);
    }

    /**
     * Get one commandes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandesDTO> findOne(String id) {
        log.debug("Request to get Commandes : {}", id);
        return commandesRepository.findOneWithEagerRelationships(id).map(commandesMapper::toDto);
    }

    /**
     * Delete the commandes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Commandes : {}", id);
        commandesRepository.deleteById(id);
    }
}
