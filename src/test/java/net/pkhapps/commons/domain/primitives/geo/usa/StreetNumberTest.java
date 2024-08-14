package net.pkhapps.commons.domain.primitives.geo.usa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StreetNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "123",
            "4567",
            "89",
            "101A",
            "202B",
            "303-1",
            "404-2",
            "55C",
            "678-90",
            "9001",
            "100-10A"
    })
    void can_create_valid_street_numbers(String input) {
        assertThat(StreetNumber.valueOf(input).toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "12#3",        // Contains disallowed character '#'
            "45@67",       // Contains disallowed character '@'
            "-789",        // Starts with a hyphen
            "123 456",     // Contains a space
            "78*90",       // Contains disallowed character '*'
            "/123",        // Starts with a slash
            "456/",        // Ends with a slash
            "1.23",        // Contains a period
            "12345678901", // Too long (more than 10 characters)
            "",            // Empty string
            "100A-200B"    // Double suffixes
    })
    void refuses_to_create_invalid_street_numbers(String input) {
        assertThatThrownBy(() -> StreetNumber.valueOf(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void street_numbers_with_same_value_are_equal() {
        var number1 = StreetNumber.valueOf("101-2B");
        var number2 = StreetNumber.valueOf("101-2B");
        assertThat(number1).isEqualTo(number2);
        assertThat(number1.hashCode()).isEqualTo(number2.hashCode());
    }

    @Test
    void street_numbers_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var streetNumber = StreetNumber.valueOf("101-2B");
        var json = objectMapper.writeValueAsString(streetNumber);
        var deserialized = objectMapper.readValue(json, StreetNumber.class);
        assertThat(streetNumber).isEqualTo(deserialized);
    }

    @Test
    void street_numbers_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"ABC\"", StreetNumber.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void street_numbers_are_stripped() {
        assertThat(StreetNumber.valueOf(" 101\t").toString()).isEqualTo("101");
    }
}
