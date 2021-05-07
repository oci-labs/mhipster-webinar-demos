package hello.mhipster.web.rest;


import hello.mhipster.domain.School;
import hello.mhipster.repository.SchoolRepository;

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


/**
 * Integration tests for the {@Link SchoolResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SchoolResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private SchoolRepository schoolRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private School school;

    @BeforeEach
    public void initTest() {
        school = createEntity(transactionManager, em);
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
    public static School createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        School school = new School()
            .name(DEFAULT_NAME);
        return school;
    }

    /**
     * Delete all school entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, School.class);
    }


    @Test
    public void createSchool() throws Exception {
        int databaseSizeBeforeCreate = schoolRepository.findAll().size();


        // Create the School
        HttpResponse<School> response = client.exchange(HttpRequest.POST("/api/schools", school), School.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate + 1);
        School testSchool = schoolList.get(schoolList.size() - 1);

        assertThat(testSchool.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createSchoolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolRepository.findAll().size();

        // Create the School with an existing ID
        school.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.POST("/api/schools", school), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setName(null);

        // Create the School, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.POST("/api/schools", school), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSchools() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get the schoolList w/ all the schools
        List<School> schools = client.retrieve(HttpRequest.GET("/api/schools?eagerload=true"), Argument.listOf(School.class)).blockingFirst();
        School testSchool = schools.get(0);


        assertThat(testSchool.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get the school
        School testSchool = client.retrieve(HttpRequest.GET("/api/schools/" + school.getId()), School.class).blockingFirst();


        assertThat(testSchool.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingSchool() throws Exception {
        // Get the school
        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.GET("/api/schools/"+ Long.MAX_VALUE), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Update the school
        School updatedSchool = schoolRepository.findById(school.getId()).get();

        updatedSchool
            .name(UPDATED_NAME);

        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.PUT("/api/schools", updatedSchool), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
        School testSchool = schoolList.get(schoolList.size() - 1);

        assertThat(testSchool.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Create the School

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.PUT("/api/schools", school), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSchool() throws Exception {
        // Initialize the database with one entity
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeDelete = schoolRepository.findAll().size();

        // Delete the school
        @SuppressWarnings("unchecked")
        HttpResponse<School> response = client.exchange(HttpRequest.DELETE("/api/schools/"+ school.getId()), School.class)
            .onErrorReturn(t -> (HttpResponse<School>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(School.class);
        School school1 = new School();
        school1.setId(1L);
        School school2 = new School();
        school2.setId(school1.getId());
        assertThat(school1).isEqualTo(school2);
        school2.setId(2L);
        assertThat(school1).isNotEqualTo(school2);
        school1.setId(null);
        assertThat(school1).isNotEqualTo(school2);
    }
}
