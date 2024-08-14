package net.pkhapps.commons.domain.primitives.geo.usa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SecondaryAddressDesignatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "Apt 101",           // Apartment number
            "Unit B",            // Unit number with letter
            "Suite 200",         // Suite number
            "#3A",               // Number with hash symbol
            "P.O. Box 456",      // P.O. Box number
            "Room 12",           // Room number
            "Floor 3",           // Floor number
            "Building 7",        // Building number
            "Dept 8",            // Department number
            "Lot 24"             // Lot number
    })
    void can_create_valid_address_designators(String input) {
        assertThat(SecondaryAddressDesignator.valueOf(input).toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Apt @101",       // Contains disallowed character '@'
            "Unit #$",        // Contains disallowed character '$'
            "Suite !200",     // Contains disallowed character '!'
            "Room 12%",       // Contains disallowed character '%'
            "Floor 3*",       // Contains disallowed character '*'
            "Building &7",    // Contains disallowed character '&'
            "Dept 8(",        // Contains disallowed character '('
            "Lot 24)",         // Contains disallowed character ')',
            "P.O. Box 123456789101" // Too long
    })
    void refuses_to_create_invalid_address_designators(String input) {
        assertThatThrownBy(() -> SecondaryAddressDesignator.valueOf(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void address_designators_with_same_value_are_equal() {
        var room1 = SecondaryAddressDesignator.valueOf("Room 1");
        var room2 = SecondaryAddressDesignator.valueOf("Room 1");
        assertThat(room1).isEqualTo(room2);
        assertThat(room1.hashCode()).isEqualTo(room2.hashCode());
    }

    @Test
    void address_designators_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var designator = SecondaryAddressDesignator.valueOf("Suite 200");
        var json = objectMapper.writeValueAsString(designator);
        var deserialized = objectMapper.readValue(json, SecondaryAddressDesignator.class);
        assertThat(designator).isEqualTo(deserialized);
    }

    @Test
    void address_designators_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"Suite*200\"", SecondaryAddressDesignator.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void address_designators_are_stripped() {
        assertThat(SecondaryAddressDesignator.valueOf(" Suite 200\t").toString()).isEqualTo("Suite 200");
    }
}
