package hello.mhipster.service;

import hello.mhipster.domain.School;
import hello.mhipster.repository.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link School}.
 */
@Singleton
@Transactional
public class SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolService.class);

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    /**
     * Save a school.
     *
     * @param school the entity to save.
     * @return the persisted entity.
     */
    public School save(School school) {
        log.debug("Request to save School : {}", school);
        return schoolRepository.save(school);
    }

    /**
     * Update a school.
     *
     * @param school the entity to update.
     * @return the persisted entity.
     */
    public School update(School school) {
        log.debug("Request to update School : {}", school);
        return schoolRepository.update(school);
    }

    /**
     * Get all the schools.
     *
     * @return the list of entities.
     */
    @ReadOnly
    @Transactional
    public List<School> findAll() {
        log.debug("Request to get all Schools");
        return schoolRepository.findAll();
    }


    /**
     * Get one school by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @ReadOnly
    @Transactional
    public Optional<School> findOne(Long id) {
        log.debug("Request to get School : {}", id);
        return schoolRepository.findById(id);
    }

    /**
     * Delete the school by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.deleteById(id);
    }
}
