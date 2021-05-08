package com.mhipster.demo.invoice.service;

import com.mhipster.demo.invoice.config.Constants;
import com.mhipster.demo.invoice.domain.Authority;
import com.mhipster.demo.invoice.domain.User;
import com.mhipster.demo.invoice.repository.AuthorityRepository;
import com.mhipster.demo.invoice.repository.UserRepository;
import com.mhipster.demo.invoice.security.AuthoritiesConstants;
import com.mhipster.demo.invoice.security.SecurityUtils;
import com.mhipster.demo.invoice.service.dto.UserDTO;
import com.mhipster.demo.invoice.service.util.RandomUtil;
import com.mhipster.demo.invoice.web.rest.errors.*;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Singleton
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;


    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public User syncUserWithIdP(User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities =
            user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        for (String authority : userAuthorities) {
            if (!dbAuthorities.contains(authority)) {
                log.debug("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {

            log.debug("Updating user '{}' in local database", user.getLogin());
            updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getLangKey(), user.getImageUrl());

        } else {
            log.debug("Saving user '{}' in local database", user.getLogin());
            userRepository.save(user);
        }
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @ReadOnly
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAllByLoginNot(Constants.ANONYMOUS_USER, pageable);
        return Page.of(userPage.getContent().stream().map(UserDTO::new).collect(Collectors.toList()), pageable, userPage.getTotalSize());
    }

    @ReadOnly
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    @ReadOnly
    public Optional<User> getUserWithAuthorities(String id) {
        return userRepository.findOneById(id);
    }

    @ReadOnly
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }


    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream()
            .map(Authority::getName).collect(Collectors.toList());
    }

    public Optional<String> getCurrentUserLogin() {
        return SecurityUtils.getCurrentUserLogin();
    }
}
