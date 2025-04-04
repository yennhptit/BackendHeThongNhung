package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViolationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Violation.class);
        Violation violation1 = new Violation();
        violation1.setId(1L);
        Violation violation2 = new Violation();
        violation2.setId(violation1.getId());
        assertThat(violation1).isEqualTo(violation2);
        violation2.setId(2L);
        assertThat(violation1).isNotEqualTo(violation2);
        violation1.setId(null);
        assertThat(violation1).isNotEqualTo(violation2);
    }
}
