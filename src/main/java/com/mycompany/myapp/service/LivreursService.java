package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Livreurs;
import com.mycompany.myapp.repository.LivreursRepository;
import com.mycompany.myapp.service.dto.LivreursDTO;
import com.mycompany.myapp.service.mapper.LivreursMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Livreurs}.
 */
@Service
@Transactional
public class LivreursService {

    private final Logger log = LoggerFactory.getLogger(LivreursService.class);

    private final LivreursRepository livreursRepository;

    private final LivreursMapper livreursMapper;

    public LivreursService(LivreursRepository livreursRepository, LivreursMapper livreursMapper) {
        this.livreursRepository = livreursRepository;
        this.livreursMapper = livreursMapper;
    }

    /**
     * Save a livreurs.
     *
     * @param livreursDTO the entity to save.
     * @return the persisted entity.
     */
    public LivreursDTO save(LivreursDTO livreursDTO) {
        log.debug("Request to save Livreurs : {}", livreursDTO);
        Livreurs livreurs = livreursMapper.toEntity(livreursDTO);
        livreurs = livreursRepository.save(livreurs);
        return livreursMapper.toDto(livreurs);
    }

    /**
     * Update a livreurs.
     *
     * @param livreursDTO the entity to save.
     * @return the persisted entity.
     */
    public LivreursDTO update(LivreursDTO livreursDTO) {
        log.debug("Request to save Livreurs : {}", livreursDTO);
        Livreurs livreurs = livreursMapper.toEntity(livreursDTO);
        livreurs = livreursRepository.save(livreurs);
        return livreursMapper.toDto(livreurs);
    }

    /**
     * Partially update a livreurs.
     *
     * @param livreursDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LivreursDTO> partialUpdate(LivreursDTO livreursDTO) {
        log.debug("Request to partially update Livreurs : {}", livreursDTO);

        return livreursRepository
            .findById(livreursDTO.getId())
            .map(existingLivreurs -> {
                livreursMapper.partialUpdate(existingLivreurs, livreursDTO);

                return existingLivreurs;
            })
            .map(livreursRepository::save)
            .map(livreursMapper::toDto);
    }

    /**
     * Get all the livreurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LivreursDTO> findAll() {
        log.debug("Request to get all Livreurs");
        return livreursRepository.findAll().stream().map(livreursMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one livreurs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivreursDTO> findOne(Long id) {
        log.debug("Request to get Livreurs : {}", id);
        return livreursRepository.findById(id).map(livreursMapper::toDto);
    }

    /**
     * Delete the livreurs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Livreurs : {}", id);
        livreursRepository.deleteById(id);
    }
}
