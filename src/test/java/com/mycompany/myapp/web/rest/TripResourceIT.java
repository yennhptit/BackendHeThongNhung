package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.enumeration.TripStatus;
import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.service.dto.TripDTO;
import com.mycompany.myapp.service.mapper.TripMapper;
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
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TripStatus DEFAULT_STATUS = TripStatus.ONGOING;
    private static final TripStatus UPDATED_STATUS = TripStatus.COMPLETED;

    private static final Boolean DEFAULT_IS_DELETE = false;
    private static final Boolean UPDATED_IS_DELETE = true;

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip().startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME).status(DEFAULT_STATUS).isDelete(DEFAULT_IS_DELETE);
        return trip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip().startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).status(UPDATED_STATUS).isDelete(UPDATED_IS_DELETE);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();
        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTrip.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTrip.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTrip.getIsDelete()).isEqualTo(DEFAULT_IS_DELETE);
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);
        TripDTO tripDTO = tripMapper.toDto(trip);

        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDelete").value(hasItem(DEFAULT_IS_DELETE.booleanValue())));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isDelete").value(DEFAULT_IS_DELETE.booleanValue()));
    }

    @Test
    @Transactional
    void getTripsByIdFiltering() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        Long id = trip.getId();

        defaultTripShouldBeFound("id.equals=" + id);
        defaultTripShouldNotBeFound("id.notEquals=" + id);

        defaultTripShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.greaterThan=" + id);

        defaultTripShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime equals to DEFAULT_START_TIME
        defaultTripShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime equals to UPDATED_START_TIME
        defaultTripShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultTripShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the tripList where startTime equals to UPDATED_START_TIME
        defaultTripShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is not null
        defaultTripShouldBeFound("startTime.specified=true");

        // Get all the tripList where startTime is null
        defaultTripShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where endTime equals to DEFAULT_END_TIME
        defaultTripShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the tripList where endTime equals to UPDATED_END_TIME
        defaultTripShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultTripShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the tripList where endTime equals to UPDATED_END_TIME
        defaultTripShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where endTime is not null
        defaultTripShouldBeFound("endTime.specified=true");

        // Get all the tripList where endTime is null
        defaultTripShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where status equals to DEFAULT_STATUS
        defaultTripShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tripList where status equals to UPDATED_STATUS
        defaultTripShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTripShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tripList where status equals to UPDATED_STATUS
        defaultTripShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTripsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where status is not null
        defaultTripShouldBeFound("status.specified=true");

        // Get all the tripList where status is null
        defaultTripShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByIsDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where isDelete equals to DEFAULT_IS_DELETE
        defaultTripShouldBeFound("isDelete.equals=" + DEFAULT_IS_DELETE);

        // Get all the tripList where isDelete equals to UPDATED_IS_DELETE
        defaultTripShouldNotBeFound("isDelete.equals=" + UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void getAllTripsByIsDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where isDelete in DEFAULT_IS_DELETE or UPDATED_IS_DELETE
        defaultTripShouldBeFound("isDelete.in=" + DEFAULT_IS_DELETE + "," + UPDATED_IS_DELETE);

        // Get all the tripList where isDelete equals to UPDATED_IS_DELETE
        defaultTripShouldNotBeFound("isDelete.in=" + UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void getAllTripsByIsDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where isDelete is not null
        defaultTripShouldBeFound("isDelete.specified=true");

        // Get all the tripList where isDelete is null
        defaultTripShouldNotBeFound("isDelete.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByDriverIsEqualToSomething() throws Exception {
        Driver driver;
        if (TestUtil.findAll(em, Driver.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            driver = DriverResourceIT.createEntity(em);
        } else {
            driver = TestUtil.findAll(em, Driver.class).get(0);
        }
        em.persist(driver);
        em.flush();
        trip.setDriver(driver);
        tripRepository.saveAndFlush(trip);
        Long driverId = driver.getId();
        // Get all the tripList where driver equals to driverId
        defaultTripShouldBeFound("driverId.equals=" + driverId);

        // Get all the tripList where driver equals to (driverId + 1)
        defaultTripShouldNotBeFound("driverId.equals=" + (driverId + 1));
    }

    @Test
    @Transactional
    void getAllTripsByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            tripRepository.saveAndFlush(trip);
            vehicle = VehicleResourceIT.createEntity(em);
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        trip.setVehicle(vehicle);
        tripRepository.saveAndFlush(trip);
        Long vehicleId = vehicle.getId();
        // Get all the tripList where vehicle equals to vehicleId
        defaultTripShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the tripList where vehicle equals to (vehicleId + 1)
        defaultTripShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripShouldBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isDelete").value(hasItem(DEFAULT_IS_DELETE.booleanValue())));

        // Check, that the count call also returns 1
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripShouldNotBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).status(UPDATED_STATUS).isDelete(UPDATED_IS_DELETE);
        TripDTO tripDTO = tripMapper.toDto(updatedTrip);

        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTrip.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTrip.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).isDelete(UPDATED_IS_DELETE);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTrip.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTrip.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).status(UPDATED_STATUS).isDelete(UPDATED_IS_DELETE);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTrip.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTrip.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
