package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cooperatives;
import com.mycompany.myapp.repository.CooperativesRepository;
import com.mycompany.myapp.service.dto.CooperativesDTO;
import com.mycompany.myapp.service.mapper.CooperativesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cooperatives}.
 */
@Service
@Transactional
public class CooperativesService {

    private final Logger log = LoggerFactory.getLogger(CooperativesService.class);

    private final CooperativesRepository cooperativesRepository;

    private final CooperativesMapper cooperativesMapper;

    public CooperativesService(CooperativesRepository cooperativesRepository, CooperativesMapper cooperativesMapper) {
        this.cooperativesRepository = cooperativesRepository;
        this.cooperativesMapper = cooperativesMapper;
    }

    /**
     * Save a cooperatives.
     *
     * @param cooperativesDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativesDTO save(CooperativesDTO cooperativesDTO) {
        log.debug("Request to save Cooperatives : {}", cooperativesDTO);
        Cooperatives cooperatives = cooperativesMapper.toEntity(cooperativesDTO);
        cooperatives = cooperativesRepository.save(cooperatives);
        return cooperativesMapper.toDto(cooperatives);
    }

    /**
     * Update a cooperatives.
     *
     * @param cooperativesDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativesDTO update(CooperativesDTO cooperativesDTO) {
        log.debug("Request to save Cooperatives : {}", cooperativesDTO);
        Cooperatives cooperatives = cooperativesMapper.toEntity(cooperativesDTO);
        cooperatives = cooperativesRepository.save(cooperatives);
        return cooperativesMapper.toDto(cooperatives);
    }

    /**
     * Partially update a cooperatives.
     *
     * @param cooperativesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativesDTO> partialUpdate(CooperativesDTO cooperativesDTO) {
        log.debug("Request to partially update Cooperatives : {}", cooperativesDTO);

        return cooperativesRepository
            .findById(cooperativesDTO.getId())
            .map(existingCooperatives -> {
                cooperativesMapper.partialUpdate(existingCooperatives, cooperativesDTO);

                return existingCooperatives;
            })
            .map(cooperativesRepository::save)
            .map(cooperativesMapper::toDto);
    }

    /**
     * Get all the cooperatives.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativesDTO> findAll() {
        log.debug("Request to get all Cooperatives");
        return cooperativesRepository.findAll().stream().map(cooperativesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cooperatives by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativesDTO> findOne(Long id) {
        log.debug("Request to get Cooperatives : {}", id);
        return cooperativesRepository.findById(id).map(cooperativesMapper::toDto);
    }

    /**
     * Delete the cooperatives by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cooperatives : {}", id);
        cooperativesRepository.deleteById(id);
    }
}
