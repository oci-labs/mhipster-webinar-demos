package com.mhipster.demo.crm.repository;

import com.mhipster.demo.crm.domain.User;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Micronaut Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    

    public Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneById(String id);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneByEmail(String email);

    public Page<User> findAllByLoginNot(String login, Pageable pageable);

    public void update(@Id String id, Instant createdDate);
}
