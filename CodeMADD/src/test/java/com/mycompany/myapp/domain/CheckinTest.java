package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CheckinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checkin.class);
        Checkin checkin1 = new Checkin();
        checkin1.setId(1L);
        Checkin checkin2 = new Checkin();
        checkin2.setId(checkin1.getId());
        assertThat(checkin1).isEqualTo(checkin2);
        checkin2.setId(2L);
        assertThat(checkin1).isNotEqualTo(checkin2);
        checkin1.setId(null);
        assertThat(checkin1).isNotEqualTo(checkin2);
    }
}
