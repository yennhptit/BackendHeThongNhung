package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Violation;
import com.mycompany.myapp.domain.enumeration.DriverStatus;
import com.mycompany.myapp.repository.DriverRepository;
import com.mycompany.myapp.service.dto.DriverDTO;
import com.mycompany.myapp.service.mapper.DriverMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DriverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriverResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DRIVER_ID = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FACE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_FACE_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DriverStatus DEFAULT_STATUS = DriverStatus.ACTIVE;
    private static final DriverStatus UPDATED_STATUS = DriverStatus.INACTIVE;

    private static final Boolean DEFAULT_IS_DELETE = false;
    private static final Boolean UPDATED_IS_DELETE = true;

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverMockMvc;

    private Driver driver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
            .name(DEFAULT_NAME)
            .driverId(DEFAULT_DRIVER_ID)
            .licenseNumber(DEFAULT_LICENSE_NUMBER)
            .faceData(DEFAULT_FACE_DATA)
            .createdAt(DEFAULT_CREATED_AT)
            .status(DEFAULT_STATUS)
            .isDelete(DEFAULT_IS_DELETE);
        return driver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity(EntityManager em) {
        Driver driver = new Driver()
            .name(UPDATED_NAME)
            .driverId(UPDATED_DRIVER_ID)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .faceData(UPDATED_FACE_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .isDelete(UPDATED_IS_DELETE);
        return driver;
    }

    @BeforeEach
    public void initTest() {
        driver = createEntity(em);
    }

    @Test
    @Transactional
    void createDriver() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();
        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isCreated());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDriver.getDriverId()).isEqualTo(DEFAULT_DRIVER_ID);
        assertThat(testDriver.getLicenseNumber()).isEqualTo(DEFAULT_LICENSE_NUMBER);
        assertThat(testDriver.getFaceData()).isEqualTo(DEFAULT_FACE_DATA);
        assertThat(testDriver.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDriver.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDriver.getIsDelete()).isEqualTo(DEFAULT_IS_DELETE);
    }

    @Test
    @Transactional
    void createDriverWithExistingId() throws Exception {
        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().size();
        // set the field null
        driver.setName(null);

        // Create the Driver, which fails.
        DriverDTO driverDTO = driverMapper.toDto(driver);

        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrivers() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID)))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].faceData").value(hasItem(DEFAULT_FACE_DATA.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDelete").value(hasItem(DEFAULT_IS_DELETE.booleanValue())));
    }

    @Test
    @Transactional
    void getDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc
            .perform(get(ENTITY_API_URL_ID, driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.driverId").value(DEFAULT_DRIVER_ID))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER))
            .andExpect(jsonPath("$.faceData").value(DEFAULT_FACE_DATA.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isDelete").value(DEFAULT_IS_DELETE.booleanValue()));
    }

    @Test
    @Transactional
    void getDriversByIdFiltering() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        Long id = driver.getId();

        defaultDriverShouldBeFound("id.equals=" + id);
        defaultDriverShouldNotBeFound("id.notEquals=" + id);

        defaultDriverShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDriverShouldNotBeFound("id.greaterThan=" + id);

        defaultDriverShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDriverShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where name equals to DEFAULT_NAME
        defaultDriverShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the driverList where name equals to UPDATED_NAME
        defaultDriverShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDriverShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the driverList where name equals to UPDATED_NAME
        defaultDriverShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where name is not null
        defaultDriverShouldBeFound("name.specified=true");

        // Get all the driverList where name is null
        defaultDriverShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByNameContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where name contains DEFAULT_NAME
        defaultDriverShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the driverList where name contains UPDATED_NAME
        defaultDriverShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByNameNotContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where name does not contain DEFAULT_NAME
        defaultDriverShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the driverList where name does not contain UPDATED_NAME
        defaultDriverShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDriversByDriverIdIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where driverId equals to DEFAULT_DRIVER_ID
        defaultDriverShouldBeFound("driverId.equals=" + DEFAULT_DRIVER_ID);

        // Get all the driverList where driverId equals to UPDATED_DRIVER_ID
        defaultDriverShouldNotBeFound("driverId.equals=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByDriverIdIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where driverId in DEFAULT_DRIVER_ID or UPDATED_DRIVER_ID
        defaultDriverShouldBeFound("driverId.in=" + DEFAULT_DRIVER_ID + "," + UPDATED_DRIVER_ID);

        // Get all the driverList where driverId equals to UPDATED_DRIVER_ID
        defaultDriverShouldNotBeFound("driverId.in=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByDriverIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where driverId is not null
        defaultDriverShouldBeFound("driverId.specified=true");

        // Get all the driverList where driverId is null
        defaultDriverShouldNotBeFound("driverId.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByDriverIdContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where driverId contains DEFAULT_DRIVER_ID
        defaultDriverShouldBeFound("driverId.contains=" + DEFAULT_DRIVER_ID);

        // Get all the driverList where driverId contains UPDATED_DRIVER_ID
        defaultDriverShouldNotBeFound("driverId.contains=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByDriverIdNotContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where driverId does not contain DEFAULT_DRIVER_ID
        defaultDriverShouldNotBeFound("driverId.doesNotContain=" + DEFAULT_DRIVER_ID);

        // Get all the driverList where driverId does not contain UPDATED_DRIVER_ID
        defaultDriverShouldBeFound("driverId.doesNotContain=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllDriversByLicenseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where licenseNumber equals to DEFAULT_LICENSE_NUMBER
        defaultDriverShouldBeFound("licenseNumber.equals=" + DEFAULT_LICENSE_NUMBER);

        // Get all the driverList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultDriverShouldNotBeFound("licenseNumber.equals=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByLicenseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where licenseNumber in DEFAULT_LICENSE_NUMBER or UPDATED_LICENSE_NUMBER
        defaultDriverShouldBeFound("licenseNumber.in=" + DEFAULT_LICENSE_NUMBER + "," + UPDATED_LICENSE_NUMBER);

        // Get all the driverList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultDriverShouldNotBeFound("licenseNumber.in=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByLicenseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where licenseNumber is not null
        defaultDriverShouldBeFound("licenseNumber.specified=true");

        // Get all the driverList where licenseNumber is null
        defaultDriverShouldNotBeFound("licenseNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByLicenseNumberContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where licenseNumber contains DEFAULT_LICENSE_NUMBER
        defaultDriverShouldBeFound("licenseNumber.contains=" + DEFAULT_LICENSE_NUMBER);

        // Get all the driverList where licenseNumber contains UPDATED_LICENSE_NUMBER
        defaultDriverShouldNotBeFound("licenseNumber.contains=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByLicenseNumberNotContainsSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where licenseNumber does not contain DEFAULT_LICENSE_NUMBER
        defaultDriverShouldNotBeFound("licenseNumber.doesNotContain=" + DEFAULT_LICENSE_NUMBER);

        // Get all the driverList where licenseNumber does not contain UPDATED_LICENSE_NUMBER
        defaultDriverShouldBeFound("licenseNumber.doesNotContain=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDriversByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where createdAt equals to DEFAULT_CREATED_AT
        defaultDriverShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the driverList where createdAt equals to UPDATED_CREATED_AT
        defaultDriverShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDriversByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultDriverShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the driverList where createdAt equals to UPDATED_CREATED_AT
        defaultDriverShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDriversByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where createdAt is not null
        defaultDriverShouldBeFound("createdAt.specified=true");

        // Get all the driverList where createdAt is null
        defaultDriverShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where status equals to DEFAULT_STATUS
        defaultDriverShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the driverList where status equals to UPDATED_STATUS
        defaultDriverShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDriverShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the driverList where status equals to UPDATED_STATUS
        defaultDriverShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDriversByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where status is not null
        defaultDriverShouldBeFound("status.specified=true");

        // Get all the driverList where status is null
        defaultDriverShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByIsDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where isDelete equals to DEFAULT_IS_DELETE
        defaultDriverShouldBeFound("isDelete.equals=" + DEFAULT_IS_DELETE);

        // Get all the driverList where isDelete equals to UPDATED_IS_DELETE
        defaultDriverShouldNotBeFound("isDelete.equals=" + UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void getAllDriversByIsDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where isDelete in DEFAULT_IS_DELETE or UPDATED_IS_DELETE
        defaultDriverShouldBeFound("isDelete.in=" + DEFAULT_IS_DELETE + "," + UPDATED_IS_DELETE);

        // Get all the driverList where isDelete equals to UPDATED_IS_DELETE
        defaultDriverShouldNotBeFound("isDelete.in=" + UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void getAllDriversByIsDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList where isDelete is not null
        defaultDriverShouldBeFound("isDelete.specified=true");

        // Get all the driverList where isDelete is null
        defaultDriverShouldNotBeFound("isDelete.specified=false");
    }

    @Test
    @Transactional
    void getAllDriversByTripIsEqualToSomething() throws Exception {
        Trip trip;
        if (TestUtil.findAll(em, Trip.class).isEmpty()) {
            driverRepository.saveAndFlush(driver);
            trip = TripResourceIT.createEntity(em);
        } else {
            trip = TestUtil.findAll(em, Trip.class).get(0);
        }
        em.persist(trip);
        em.flush();
        driver.addTrip(trip);
        driverRepository.saveAndFlush(driver);
        Long tripId = trip.getId();
        // Get all the driverList where trip equals to tripId
        defaultDriverShouldBeFound("tripId.equals=" + tripId);

        // Get all the driverList where trip equals to (tripId + 1)
        defaultDriverShouldNotBeFound("tripId.equals=" + (tripId + 1));
    }

    @Test
    @Transactional
    void getAllDriversByViolationIsEqualToSomething() throws Exception {
        Violation violation;
        if (TestUtil.findAll(em, Violation.class).isEmpty()) {
            driverRepository.saveAndFlush(driver);
            violation = ViolationResourceIT.createEntity(em);
        } else {
            violation = TestUtil.findAll(em, Violation.class).get(0);
        }
        em.persist(violation);
        em.flush();
        driver.addViolation(violation);
        driverRepository.saveAndFlush(driver);
        Long violationId = violation.getId();
        // Get all the driverList where violation equals to violationId
        defaultDriverShouldBeFound("violationId.equals=" + violationId);

        // Get all the driverList where violation equals to (violationId + 1)
        defaultDriverShouldNotBeFound("violationId.equals=" + (violationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDriverShouldBeFound(String filter) throws Exception {
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID)))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].faceData").value(hasItem(DEFAULT_FACE_DATA.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDelete").value(hasItem(DEFAULT_IS_DELETE.booleanValue())));

        // Check, that the count call also returns 1
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDriverShouldNotBeFound(String filter) throws Exception {
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver
            .name(UPDATED_NAME)
            .driverId(UPDATED_DRIVER_ID)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .faceData(UPDATED_FACE_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .isDelete(UPDATED_IS_DELETE);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDriver.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testDriver.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testDriver.getFaceData()).isEqualTo(UPDATED_FACE_DATA);
        assertThat(testDriver.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDriver.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDriver.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void putNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver.name(UPDATED_NAME).driverId(UPDATED_DRIVER_ID).licenseNumber(UPDATED_LICENSE_NUMBER);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDriver.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testDriver.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testDriver.getFaceData()).isEqualTo(DEFAULT_FACE_DATA);
        assertThat(testDriver.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDriver.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDriver.getIsDelete()).isEqualTo(DEFAULT_IS_DELETE);
    }

    @Test
    @Transactional
    void fullUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver
            .name(UPDATED_NAME)
            .driverId(UPDATED_DRIVER_ID)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .faceData(UPDATED_FACE_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .isDelete(UPDATED_IS_DELETE);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDriver.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testDriver.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testDriver.getFaceData()).isEqualTo(UPDATED_FACE_DATA);
        assertThat(testDriver.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDriver.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDriver.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void patchNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(driverDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeDelete = driverRepository.findAll().size();

        // Delete the driver
        restDriverMockMvc
            .perform(delete(ENTITY_API_URL_ID, driver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
