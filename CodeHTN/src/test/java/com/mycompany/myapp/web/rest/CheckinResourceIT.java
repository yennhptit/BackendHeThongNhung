package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Checkin;
import com.mycompany.myapp.repository.CheckinRepository;
import com.mycompany.myapp.service.dto.CheckinDTO;
import com.mycompany.myapp.service.mapper.CheckinMapper;
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
 * Integration tests for the {@link CheckinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CheckinResourceIT {

    private static final Instant DEFAULT_CHECKIN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECKIN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECKOUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECKOUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_FACE_VERIFIED = false;
    private static final Boolean UPDATED_FACE_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/checkins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckinMockMvc;

    private Checkin checkin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkin createEntity(EntityManager em) {
        Checkin checkin = new Checkin()
            .checkinTime(DEFAULT_CHECKIN_TIME)
            .checkoutTime(DEFAULT_CHECKOUT_TIME)
            .faceVerified(DEFAULT_FACE_VERIFIED);
        return checkin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkin createUpdatedEntity(EntityManager em) {
        Checkin checkin = new Checkin()
            .checkinTime(UPDATED_CHECKIN_TIME)
            .checkoutTime(UPDATED_CHECKOUT_TIME)
            .faceVerified(UPDATED_FACE_VERIFIED);
        return checkin;
    }

    @BeforeEach
    public void initTest() {
        checkin = createEntity(em);
    }

    @Test
    @Transactional
    void createCheckin() throws Exception {
        int databaseSizeBeforeCreate = checkinRepository.findAll().size();
        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);
        restCheckinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(checkinDTO)))
            .andExpect(status().isCreated());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeCreate + 1);
        Checkin testCheckin = checkinList.get(checkinList.size() - 1);
        assertThat(testCheckin.getCheckinTime()).isEqualTo(DEFAULT_CHECKIN_TIME);
        assertThat(testCheckin.getCheckoutTime()).isEqualTo(DEFAULT_CHECKOUT_TIME);
        assertThat(testCheckin.getFaceVerified()).isEqualTo(DEFAULT_FACE_VERIFIED);
    }

    @Test
    @Transactional
    void createCheckinWithExistingId() throws Exception {
        // Create the Checkin with an existing ID
        checkin.setId(1L);
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        int databaseSizeBeforeCreate = checkinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(checkinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCheckins() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        // Get all the checkinList
        restCheckinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkin.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkinTime").value(hasItem(DEFAULT_CHECKIN_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkoutTime").value(hasItem(DEFAULT_CHECKOUT_TIME.toString())))
            .andExpect(jsonPath("$.[*].faceVerified").value(hasItem(DEFAULT_FACE_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getCheckin() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        // Get the checkin
        restCheckinMockMvc
            .perform(get(ENTITY_API_URL_ID, checkin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkin.getId().intValue()))
            .andExpect(jsonPath("$.checkinTime").value(DEFAULT_CHECKIN_TIME.toString()))
            .andExpect(jsonPath("$.checkoutTime").value(DEFAULT_CHECKOUT_TIME.toString()))
            .andExpect(jsonPath("$.faceVerified").value(DEFAULT_FACE_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCheckin() throws Exception {
        // Get the checkin
        restCheckinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCheckin() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();

        // Update the checkin
        Checkin updatedCheckin = checkinRepository.findById(checkin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCheckin are not directly saved in db
        em.detach(updatedCheckin);
        updatedCheckin.checkinTime(UPDATED_CHECKIN_TIME).checkoutTime(UPDATED_CHECKOUT_TIME).faceVerified(UPDATED_FACE_VERIFIED);
        CheckinDTO checkinDTO = checkinMapper.toDto(updatedCheckin);

        restCheckinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isOk());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
        Checkin testCheckin = checkinList.get(checkinList.size() - 1);
        assertThat(testCheckin.getCheckinTime()).isEqualTo(UPDATED_CHECKIN_TIME);
        assertThat(testCheckin.getCheckoutTime()).isEqualTo(UPDATED_CHECKOUT_TIME);
        assertThat(testCheckin.getFaceVerified()).isEqualTo(UPDATED_FACE_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(checkinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCheckinWithPatch() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();

        // Update the checkin using partial update
        Checkin partialUpdatedCheckin = new Checkin();
        partialUpdatedCheckin.setId(checkin.getId());

        partialUpdatedCheckin.checkoutTime(UPDATED_CHECKOUT_TIME);

        restCheckinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCheckin))
            )
            .andExpect(status().isOk());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
        Checkin testCheckin = checkinList.get(checkinList.size() - 1);
        assertThat(testCheckin.getCheckinTime()).isEqualTo(DEFAULT_CHECKIN_TIME);
        assertThat(testCheckin.getCheckoutTime()).isEqualTo(UPDATED_CHECKOUT_TIME);
        assertThat(testCheckin.getFaceVerified()).isEqualTo(DEFAULT_FACE_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCheckinWithPatch() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();

        // Update the checkin using partial update
        Checkin partialUpdatedCheckin = new Checkin();
        partialUpdatedCheckin.setId(checkin.getId());

        partialUpdatedCheckin.checkinTime(UPDATED_CHECKIN_TIME).checkoutTime(UPDATED_CHECKOUT_TIME).faceVerified(UPDATED_FACE_VERIFIED);

        restCheckinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCheckin))
            )
            .andExpect(status().isOk());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
        Checkin testCheckin = checkinList.get(checkinList.size() - 1);
        assertThat(testCheckin.getCheckinTime()).isEqualTo(UPDATED_CHECKIN_TIME);
        assertThat(testCheckin.getCheckoutTime()).isEqualTo(UPDATED_CHECKOUT_TIME);
        assertThat(testCheckin.getFaceVerified()).isEqualTo(UPDATED_FACE_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, checkinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCheckin() throws Exception {
        int databaseSizeBeforeUpdate = checkinRepository.findAll().size();
        checkin.setId(count.incrementAndGet());

        // Create the Checkin
        CheckinDTO checkinDTO = checkinMapper.toDto(checkin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(checkinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Checkin in the database
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCheckin() throws Exception {
        // Initialize the database
        checkinRepository.saveAndFlush(checkin);

        int databaseSizeBeforeDelete = checkinRepository.findAll().size();

        // Delete the checkin
        restCheckinMockMvc
            .perform(delete(ENTITY_API_URL_ID, checkin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Checkin> checkinList = checkinRepository.findAll();
        assertThat(checkinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
