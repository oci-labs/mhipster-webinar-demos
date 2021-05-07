package hello.mhipster.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hello.mhipster.web.rest.TestUtil;

public class SchoolTest {

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
