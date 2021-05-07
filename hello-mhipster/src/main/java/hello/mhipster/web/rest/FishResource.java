package hello.mhipster.web.rest;

import hello.mhipster.domain.Fish;
import hello.mhipster.service.FishService;
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
 * REST controller for managing {@link hello.mhipster.domain.Fish}.
 */
@Controller("/api")
public class FishResource {

    private final Logger log = LoggerFactory.getLogger(FishResource.class);

    private static final String ENTITY_NAME = "fish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FishService fishService;

    public FishResource(FishService fishService) {
        this.fishService = fishService;
    }

    /**
     * {@code POST  /fish} : Create a new fish.
     *
     * @param fish the fish to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new fish, or with status {@code 400 (Bad Request)} if the fish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/fish")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Fish> createFish(@Body Fish fish) throws URISyntaxException {
        log.debug("REST request to save Fish : {}", fish);
        if (fish.getId() != null) {
            throw new BadRequestAlertException("A new fish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fish result = fishService.save(fish);
        URI location = new URI("/api/fish/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /fish} : Updates an existing fish.
     *
     * @param fish the fish to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated fish,
     * or with status {@code 400 (Bad Request)} if the fish is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/fish")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Fish> updateFish(@Body Fish fish) throws URISyntaxException {
        log.debug("REST request to update Fish : {}", fish);
        if (fish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fish result = fishService.update(fish);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, fish.getId().toString()));
    }

    /**
     * {@code GET  /fish} : get all the fish.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of fish in body.
     */
    @Get("/fish")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Fish> getAllFish(HttpRequest request) {
        log.debug("REST request to get all Fish");
        return fishService.findAll();
    }

    /**
     * {@code GET  /fish/:id} : get the "id" fish.
     *
     * @param id the id of the fish to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the fish, or with status {@code 404 (Not Found)}.
     */
    @Get("/fish/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Fish> getFish(@PathVariable Long id) {
        log.debug("REST request to get Fish : {}", id);
        
        return fishService.findOne(id);
    }

    /**
     * {@code DELETE  /fish/:id} : delete the "id" fish.
     *
     * @param id the id of the fish to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/fish/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteFish(@PathVariable Long id) {
        log.debug("REST request to delete Fish : {}", id);
        fishService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
