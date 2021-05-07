package hello.mhipster.service;

import hello.mhipster.domain.Fish;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Fish}.
 */
public interface FishService {

    /**
     * Save a fish.
     *
     * @param fish the entity to save.
     * @return the persisted entity.
     */
    Fish save(Fish fish);

    /**
     * Update a fish.
     *
     * @param fish the entity to save.
     * @return the persisted entity.
     */
    Fish update(Fish fish);

    /**
     * Get all the fish.
     *
     * @return the list of entities.
     */
    List<Fish> findAll();


    /**
     * Get the "id" fish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fish> findOne(Long id);

    /**
     * Delete the "id" fish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
