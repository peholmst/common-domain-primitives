package net.pkhapps.commons.domain.primitives.finland;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PersonalIdentityCodeTest {
    private static final String BORN_2000S = "010106A973C";
    private static final String BORN_1900S = "010190-935U";
    private static final String BORN_1800s = "010190+925H";

    @Test
    void people_born_in_the_1900s_are_supported() {
        var pic = PersonalIdentityCode.valueOf(BORN_1900S);
        assertThat(pic.isSameBirthdateAs(LocalDate.of(1990, 1, 1))).isTrue();
    }

    @Test
    void people_born_in_the_2000s_are_supported() {
        var pic = PersonalIdentityCode.valueOf(BORN_2000S);
        assertThat(pic.isSameBirthdateAs(LocalDate.of(2006, 1, 1))).isTrue();
    }

    @Test
    void people_born_in_the_1800s_are_supported() {
        var pic = PersonalIdentityCode.valueOf(BORN_1800s);
        assertThat(pic.isSameBirthdateAs(LocalDate.of(1890, 1, 1))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0101901-935U",
            "010190<935Z",
            "010190BA-5+",
            "0101901-936U"
    })
    void refuses_to_create_invalid_pics(String input) {
        assertThatThrownBy(() -> PersonalIdentityCode.valueOf(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pics_with_same_value_are_equal() {
        var pic1 = PersonalIdentityCode.valueOf(BORN_2000S);
        var pic2 = PersonalIdentityCode.valueOf(BORN_2000S);
        assertThat(pic1).isEqualTo(pic2);
        assertThat(pic1.hashCode()).isEqualTo(pic2.hashCode());
    }

    @Test
    void pics_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var pic = PersonalIdentityCode.valueOf(BORN_2000S);
        var json = objectMapper.writeValueAsString(pic);
        assertThat(json).isEqualTo("\"" + BORN_2000S + "\"");
    }

    @Test
    void pics_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var pic = objectMapper.readValue("\"" + BORN_2000S + "\"", PersonalIdentityCode.class);
        assertThat(pic.toString()).isEqualTo(BORN_2000S);
    }
}
