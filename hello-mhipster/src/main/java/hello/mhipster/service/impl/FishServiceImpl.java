package hello.mhipster.service.impl;

import hello.mhipster.service.FishService;
import hello.mhipster.domain.Fish;
import hello.mhipster.repository.FishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Fish}.
 */
@Singleton
@Transactional
public class FishServiceImpl implements FishService {

    private final Logger log = LoggerFactory.getLogger(FishServiceImpl.class);

    private final FishRepository fishRepository;

    public FishServiceImpl(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    /**
     * Save a fish.
     *
     * @param fish the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Fish save(Fish fish) {
        log.debug("Request to save Fish : {}", fish);
        return fishRepository.save(fish);
    }

    /**
     * Update a fish.
     *
     * @param fish the entity to update.
     * @return the persisted entity.
     */
    @Override
    public Fish update(Fish fish) {
        log.debug("Request to update Fish : {}", fish);
        return fishRepository.update(fish);
    }

    /**
     * Get all the fish.
     *
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public List<Fish> findAll() {
        log.debug("Request to get all Fish");
        return fishRepository.findAll();
    }


    /**
     * Get one fish by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<Fish> findOne(Long id) {
        log.debug("Request to get Fish : {}", id);
        return fishRepository.findById(id);
    }

    /**
     * Delete the fish by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fish : {}", id);
        fishRepository.deleteById(id);
    }
}
