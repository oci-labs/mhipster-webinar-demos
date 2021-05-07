package hello.mhipster.repository;

import hello.mhipster.domain.Fish;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the Fish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {

}
