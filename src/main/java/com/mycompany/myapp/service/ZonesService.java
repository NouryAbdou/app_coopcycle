package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Zones;
import com.mycompany.myapp.repository.ZonesRepository;
import com.mycompany.myapp.service.dto.ZonesDTO;
import com.mycompany.myapp.service.mapper.ZonesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Zones}.
 */
@Service
@Transactional
public class ZonesService {

    private final Logger log = LoggerFactory.getLogger(ZonesService.class);

    private final ZonesRepository zonesRepository;

    private final ZonesMapper zonesMapper;

    public ZonesService(ZonesRepository zonesRepository, ZonesMapper zonesMapper) {
        this.zonesRepository = zonesRepository;
        this.zonesMapper = zonesMapper;
    }

    /**
     * Save a zones.
     *
     * @param zonesDTO the entity to save.
     * @return the persisted entity.
     */
    public ZonesDTO save(ZonesDTO zonesDTO) {
        log.debug("Request to save Zones : {}", zonesDTO);
        Zones zones = zonesMapper.toEntity(zonesDTO);
        zones = zonesRepository.save(zones);
        return zonesMapper.toDto(zones);
    }

    /**
     * Update a zones.
     *
     * @param zonesDTO the entity to save.
     * @return the persisted entity.
     */
    public ZonesDTO update(ZonesDTO zonesDTO) {
        log.debug("Request to save Zones : {}", zonesDTO);
        Zones zones = zonesMapper.toEntity(zonesDTO);
        zones = zonesRepository.save(zones);
        return zonesMapper.toDto(zones);
    }

    /**
     * Partially update a zones.
     *
     * @param zonesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ZonesDTO> partialUpdate(ZonesDTO zonesDTO) {
        log.debug("Request to partially update Zones : {}", zonesDTO);

        return zonesRepository
            .findById(zonesDTO.getId())
            .map(existingZones -> {
                zonesMapper.partialUpdate(existingZones, zonesDTO);

                return existingZones;
            })
            .map(zonesRepository::save)
            .map(zonesMapper::toDto);
    }

    /**
     * Get all the zones.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ZonesDTO> findAll() {
        log.debug("Request to get all Zones");
        return zonesRepository.findAll().stream().map(zonesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one zones by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ZonesDTO> findOne(Long id) {
        log.debug("Request to get Zones : {}", id);
        return zonesRepository.findById(id).map(zonesMapper::toDto);
    }

    /**
     * Delete the zones by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Zones : {}", id);
        zonesRepository.deleteById(id);
    }
}
