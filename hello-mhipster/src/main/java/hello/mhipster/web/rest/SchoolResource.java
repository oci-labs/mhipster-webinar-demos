package hello.mhipster.web.rest;

import hello.mhipster.domain.School;
import hello.mhipster.service.SchoolService;
import hello.mhipster.web.rest.errors.BadRequestAlertException;

import hello.mhipster.util.HeaderUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link hello.mhipster.domain.School}.
 */
@Controller("/api")
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

    private static final String ENTITY_NAME = "school";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolService schoolService;

    public SchoolResource(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    /**
     * {@code POST  /schools} : Create a new school.
     *
     * @param school the school to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new school, or with status {@code 400 (Bad Request)} if the school has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/schools")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<School> createSchool(@Body School school) throws URISyntaxException {
        log.debug("REST request to save School : {}", school);
        if (school.getId() != null) {
            throw new BadRequestAlertException("A new school cannot already have an ID", ENTITY_NAME, "idexists");
        }
        School result = schoolService.save(school);
        URI location = new URI("/api/schools/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /schools} : Updates an existing school.
     *
     * @param school the school to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated school,
     * or with status {@code 400 (Bad Request)} if the school is not valid,
     * or with status {@code 500 (Internal Server Error)} if the school couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/schools")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<School> updateSchool(@Body School school) throws URISyntaxException {
        log.debug("REST request to update School : {}", school);
        if (school.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        School result = schoolService.update(school);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, school.getId().toString()));
    }

    /**
     * {@code GET  /schools} : get all the schools.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of schools in body.
     */
    @Get("/schools")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<School> getAllSchools(HttpRequest request) {
        log.debug("REST request to get all Schools");
        return schoolService.findAll();
    }

    /**
     * {@code GET  /schools/:id} : get the "id" school.
     *
     * @param id the id of the school to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the school, or with status {@code 404 (Not Found)}.
     */
    @Get("/schools/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<School> getSchool(@PathVariable Long id) {
        log.debug("REST request to get School : {}", id);
        
        return schoolService.findOne(id);
    }

    /**
     * {@code DELETE  /schools/:id} : delete the "id" school.
     *
     * @param id the id of the school to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/schools/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteSchool(@PathVariable Long id) {
        log.debug("REST request to delete School : {}", id);
        schoolService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
