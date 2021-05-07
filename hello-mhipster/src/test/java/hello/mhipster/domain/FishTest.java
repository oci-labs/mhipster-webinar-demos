package hello.mhipster.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hello.mhipster.web.rest.TestUtil;

public class FishTest {

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
