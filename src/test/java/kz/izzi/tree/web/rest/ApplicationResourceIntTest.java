package kz.izzi.tree.web.rest;

import kz.izzi.tree.PlantATreeApp;

import kz.izzi.tree.domain.Application;
import kz.izzi.tree.domain.User;
import kz.izzi.tree.repository.ApplicationRepository;
import kz.izzi.tree.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import kz.izzi.tree.domain.enumeration.TreeType;
import kz.izzi.tree.domain.enumeration.EventType;
/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlantATreeApp.class)
public class ApplicationResourceIntTest {

    private static final TreeType DEFAULT_TYPE = TreeType.SPRUCE;
    private static final TreeType UPDATED_TYPE = TreeType.CEDAR;

    private static final Double DEFAULT_PLACE_LATITUDE = 1D;
    private static final Double UPDATED_PLACE_LATITUDE = 2D;

    private static final Double DEFAULT_PLACE_LONGITUDE = 1D;
    private static final Double UPDATED_PLACE_LONGITUDE = 2D;

    private static final Double DEFAULT_SEED_DATE = 1D;
    private static final Double UPDATED_SEED_DATE = 2D;

    private static final LocalDate DEFAULT_SEEDING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SEEDING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final EventType DEFAULT_EVENT = EventType.OTHER;
    private static final EventType UPDATED_EVENT = EventType.WEDDING;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource(applicationRepository);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .type(DEFAULT_TYPE)
            .placeLatitude(DEFAULT_PLACE_LATITUDE)
            .placeLongitude(DEFAULT_PLACE_LONGITUDE)
            .seedDate(DEFAULT_SEED_DATE)
            .seedingDate(DEFAULT_SEEDING_DATE)
            .event(DEFAULT_EVENT);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        application.setUser(user);
        return application;
    }

    @Before
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application
        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApplication.getPlaceLatitude()).isEqualTo(DEFAULT_PLACE_LATITUDE);
        assertThat(testApplication.getPlaceLongitude()).isEqualTo(DEFAULT_PLACE_LONGITUDE);
        assertThat(testApplication.getSeedDate()).isEqualTo(DEFAULT_SEED_DATE);
        assertThat(testApplication.getSeedingDate()).isEqualTo(DEFAULT_SEEDING_DATE);
        assertThat(testApplication.getEvent()).isEqualTo(DEFAULT_EVENT);
    }

    @Test
    @Transactional
    public void createApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application with an existing ID
        application.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setType(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlaceLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setPlaceLatitude(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlaceLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setPlaceLongitude(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isBadRequest());

        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].placeLatitude").value(hasItem(DEFAULT_PLACE_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].placeLongitude").value(hasItem(DEFAULT_PLACE_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].seedDate").value(hasItem(DEFAULT_SEED_DATE.doubleValue())))
            .andExpect(jsonPath("$.[*].seedingDate").value(hasItem(DEFAULT_SEEDING_DATE.toString())))
            .andExpect(jsonPath("$.[*].event").value(hasItem(DEFAULT_EVENT.toString())));
    }

    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.placeLatitude").value(DEFAULT_PLACE_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.placeLongitude").value(DEFAULT_PLACE_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.seedDate").value(DEFAULT_SEED_DATE.doubleValue()))
            .andExpect(jsonPath("$.seedingDate").value(DEFAULT_SEEDING_DATE.toString()))
            .andExpect(jsonPath("$.event").value(DEFAULT_EVENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findOne(application.getId());
        updatedApplication
            .type(UPDATED_TYPE)
            .placeLatitude(UPDATED_PLACE_LATITUDE)
            .placeLongitude(UPDATED_PLACE_LONGITUDE)
            .seedDate(UPDATED_SEED_DATE)
            .seedingDate(UPDATED_SEEDING_DATE)
            .event(UPDATED_EVENT);

        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApplication.getPlaceLatitude()).isEqualTo(UPDATED_PLACE_LATITUDE);
        assertThat(testApplication.getPlaceLongitude()).isEqualTo(UPDATED_PLACE_LONGITUDE);
        assertThat(testApplication.getSeedDate()).isEqualTo(UPDATED_SEED_DATE);
        assertThat(testApplication.getSeedingDate()).isEqualTo(UPDATED_SEEDING_DATE);
        assertThat(testApplication.getEvent()).isEqualTo(UPDATED_EVENT);
    }

    @Test
    @Transactional
    public void updateNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Create the Application

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application)))
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application.class);
        Application application1 = new Application();
        application1.setId(1L);
        Application application2 = new Application();
        application2.setId(application1.getId());
        assertThat(application1).isEqualTo(application2);
        application2.setId(2L);
        assertThat(application1).isNotEqualTo(application2);
        application1.setId(null);
        assertThat(application1).isNotEqualTo(application2);
    }
}
