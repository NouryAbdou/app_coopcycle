package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Restaurateurs;
import com.mycompany.myapp.repository.RestaurateursRepository;
import com.mycompany.myapp.service.dto.RestaurateursDTO;
import com.mycompany.myapp.service.mapper.RestaurateursMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurateurs}.
 */
@Service
@Transactional
public class RestaurateursService {

    private final Logger log = LoggerFactory.getLogger(RestaurateursService.class);

    private final RestaurateursRepository restaurateursRepository;

    private final RestaurateursMapper restaurateursMapper;

    public RestaurateursService(RestaurateursRepository restaurateursRepository, RestaurateursMapper restaurateursMapper) {
        this.restaurateursRepository = restaurateursRepository;
        this.restaurateursMapper = restaurateursMapper;
    }

    /**
     * Save a restaurateurs.
     *
     * @param restaurateursDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurateursDTO save(RestaurateursDTO restaurateursDTO) {
        log.debug("Request to save Restaurateurs : {}", restaurateursDTO);
        Restaurateurs restaurateurs = restaurateursMapper.toEntity(restaurateursDTO);
        restaurateurs = restaurateursRepository.save(restaurateurs);
        return restaurateursMapper.toDto(restaurateurs);
    }

    /**
     * Update a restaurateurs.
     *
     * @param restaurateursDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurateursDTO update(RestaurateursDTO restaurateursDTO) {
        log.debug("Request to save Restaurateurs : {}", restaurateursDTO);
        Restaurateurs restaurateurs = restaurateursMapper.toEntity(restaurateursDTO);
        restaurateurs = restaurateursRepository.save(restaurateurs);
        return restaurateursMapper.toDto(restaurateurs);
    }

    /**
     * Partially update a restaurateurs.
     *
     * @param restaurateursDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurateursDTO> partialUpdate(RestaurateursDTO restaurateursDTO) {
        log.debug("Request to partially update Restaurateurs : {}", restaurateursDTO);

        return restaurateursRepository
            .findById(restaurateursDTO.getId())
            .map(existingRestaurateurs -> {
                restaurateursMapper.partialUpdate(existingRestaurateurs, restaurateursDTO);

                return existingRestaurateurs;
            })
            .map(restaurateursRepository::save)
            .map(restaurateursMapper::toDto);
    }

    /**
     * Get all the restaurateurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurateursDTO> findAll() {
        log.debug("Request to get all Restaurateurs");
        return restaurateursRepository.findAll().stream().map(restaurateursMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the restaurateurs where Client is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurateursDTO> findAllWhereClientIsNull() {
        log.debug("Request to get all restaurateurs where Client is null");
        return StreamSupport
            .stream(restaurateursRepository.findAll().spliterator(), false)
            .filter(restaurateurs -> restaurateurs.getClient() == null)
            .map(restaurateursMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the restaurateurs where Livreur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurateursDTO> findAllWhereLivreurIsNull() {
        log.debug("Request to get all restaurateurs where Livreur is null");
        return StreamSupport
            .stream(restaurateursRepository.findAll().spliterator(), false)
            .filter(restaurateurs -> restaurateurs.getLivreur() == null)
            .map(restaurateursMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one restaurateurs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestaurateursDTO> findOne(Long id) {
        log.debug("Request to get Restaurateurs : {}", id);
        return restaurateursRepository.findById(id).map(restaurateursMapper::toDto);
    }

    /**
     * Delete the restaurateurs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restaurateurs : {}", id);
        restaurateursRepository.deleteById(id);
    }
}
