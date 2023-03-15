package academy.spring.sample.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SpringAcademySampleThingTest {
    @Test
    void springAcademySampleThings_shouldBeReal() {
        assertThat(new SpringAcademySampleThing(99, "Sample")).isNotNull();
    }
}