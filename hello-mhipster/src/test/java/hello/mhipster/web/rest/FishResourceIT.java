package hello.mhipster.web.rest;


import hello.mhipster.domain.Fish;
import hello.mhipster.repository.FishRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.sql.Connection;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


import hello.mhipster.domain.enumeration.WaterType;
/**
 * Integration tests for the {@Link FishResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;

    private static final WaterType DEFAULT_WATER_TYPE = WaterType.FRESH;
    private static final WaterType UPDATED_WATER_TYPE = WaterType.SALT;

    @Inject
    private FishRepository fishRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Fish fish;

    @BeforeEach
    public void initTest() {
        fish = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fish createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Fish fish = new Fish()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .waterType(DEFAULT_WATER_TYPE);
        return fish;
    }

    /**
     * Delete all fish entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Fish.class);
    }


    @Test
    public void createFish() throws Exception {
        int databaseSizeBeforeCreate = fishRepository.findAll().size();


        // Create the Fish
        HttpResponse<Fish> response = client.exchange(HttpRequest.POST("/api/fish", fish), Fish.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Fish in the database
        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeCreate + 1);
        Fish testFish = fishList.get(fishList.size() - 1);

        assertThat(testFish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFish.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testFish.getWaterType()).isEqualTo(DEFAULT_WATER_TYPE);
    }

    @Test
    public void createFishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fishRepository.findAll().size();

        // Create the Fish with an existing ID
        fish.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.POST("/api/fish", fish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Fish in the database
        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fishRepository.findAll().size();
        // set the field null
        fish.setName(null);

        // Create the Fish, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.POST("/api/fish", fish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fishRepository.findAll().size();
        // set the field null
        fish.setAge(null);

        // Create the Fish, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.POST("/api/fish", fish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkWaterTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fishRepository.findAll().size();
        // set the field null
        fish.setWaterType(null);

        // Create the Fish, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.POST("/api/fish", fish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFish() throws Exception {
        // Initialize the database
        fishRepository.saveAndFlush(fish);

        // Get the fishList w/ all the fish
        List<Fish> fish = client.retrieve(HttpRequest.GET("/api/fish?eagerload=true"), Argument.listOf(Fish.class)).blockingFirst();
        Fish testFish = fish.get(0);


        assertThat(testFish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFish.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testFish.getWaterType()).isEqualTo(DEFAULT_WATER_TYPE);
    }

    @Test
    public void getFish() throws Exception {
        // Initialize the database
        fishRepository.saveAndFlush(fish);

        // Get the fish
        Fish testFish = client.retrieve(HttpRequest.GET("/api/fish/" + fish.getId()), Fish.class).blockingFirst();


        assertThat(testFish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFish.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testFish.getWaterType()).isEqualTo(DEFAULT_WATER_TYPE);
    }

    @Test
    public void getNonExistingFish() throws Exception {
        // Get the fish
        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.GET("/api/fish/"+ Long.MAX_VALUE), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateFish() throws Exception {
        // Initialize the database
        fishRepository.saveAndFlush(fish);

        int databaseSizeBeforeUpdate = fishRepository.findAll().size();

        // Update the fish
        Fish updatedFish = fishRepository.findById(fish.getId()).get();

        updatedFish
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .waterType(UPDATED_WATER_TYPE);

        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.PUT("/api/fish", updatedFish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Fish in the database
        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeUpdate);
        Fish testFish = fishList.get(fishList.size() - 1);

        assertThat(testFish.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFish.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testFish.getWaterType()).isEqualTo(UPDATED_WATER_TYPE);
    }

    @Test
    public void updateNonExistingFish() throws Exception {
        int databaseSizeBeforeUpdate = fishRepository.findAll().size();

        // Create the Fish

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.PUT("/api/fish", fish), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Fish in the database
        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFish() throws Exception {
        // Initialize the database with one entity
        fishRepository.saveAndFlush(fish);

        int databaseSizeBeforeDelete = fishRepository.findAll().size();

        // Delete the fish
        @SuppressWarnings("unchecked")
        HttpResponse<Fish> response = client.exchange(HttpRequest.DELETE("/api/fish/"+ fish.getId()), Fish.class)
            .onErrorReturn(t -> (HttpResponse<Fish>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Fish> fishList = fishRepository.findAll();
        assertThat(fishList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fish.class);
        Fish fish1 = new Fish();
        fish1.setId(1L);
        Fish fish2 = new Fish();
        fish2.setId(fish1.getId());
        assertThat(fish1).isEqualTo(fish2);
        fish2.setId(2L);
        assertThat(fish1).isNotEqualTo(fish2);
        fish1.setId(null);
        assertThat(fish1).isNotEqualTo(fish2);
    }
}
