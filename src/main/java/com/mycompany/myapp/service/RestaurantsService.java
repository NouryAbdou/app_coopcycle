package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Restaurants;
import com.mycompany.myapp.repository.RestaurantsRepository;
import com.mycompany.myapp.service.dto.RestaurantsDTO;
import com.mycompany.myapp.service.mapper.RestaurantsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurants}.
 */
@Service
@Transactional
public class RestaurantsService {

    private final Logger log = LoggerFactory.getLogger(RestaurantsService.class);

    private final RestaurantsRepository restaurantsRepository;

    private final RestaurantsMapper restaurantsMapper;

    public RestaurantsService(RestaurantsRepository restaurantsRepository, RestaurantsMapper restaurantsMapper) {
        this.restaurantsRepository = restaurantsRepository;
        this.restaurantsMapper = restaurantsMapper;
    }

    /**
     * Save a restaurants.
     *
     * @param restaurantsDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantsDTO save(RestaurantsDTO restaurantsDTO) {
        log.debug("Request to save Restaurants : {}", restaurantsDTO);
        Restaurants restaurants = restaurantsMapper.toEntity(restaurantsDTO);
        restaurants = restaurantsRepository.save(restaurants);
        return restaurantsMapper.toDto(restaurants);
    }

    /**
     * Update a restaurants.
     *
     * @param restaurantsDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantsDTO update(RestaurantsDTO restaurantsDTO) {
        log.debug("Request to save Restaurants : {}", restaurantsDTO);
        Restaurants restaurants = restaurantsMapper.toEntity(restaurantsDTO);
        restaurants = restaurantsRepository.save(restaurants);
        return restaurantsMapper.toDto(restaurants);
    }

    /**
     * Partially update a restaurants.
     *
     * @param restaurantsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurantsDTO> partialUpdate(RestaurantsDTO restaurantsDTO) {
        log.debug("Request to partially update Restaurants : {}", restaurantsDTO);

        return restaurantsRepository
            .findById(restaurantsDTO.getId())
            .map(existingRestaurants -> {
                restaurantsMapper.partialUpdate(existingRestaurants, restaurantsDTO);

                return existingRestaurants;
            })
            .map(restaurantsRepository::save)
            .map(restaurantsMapper::toDto);
    }

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantsDTO> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantsRepository.findAll().stream().map(restaurantsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one restaurants by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestaurantsDTO> findOne(Long id) {
        log.debug("Request to get Restaurants : {}", id);
        return restaurantsRepository.findById(id).map(restaurantsMapper::toDto);
    }

    /**
     * Delete the restaurants by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restaurants : {}", id);
        restaurantsRepository.deleteById(id);
    }
}
