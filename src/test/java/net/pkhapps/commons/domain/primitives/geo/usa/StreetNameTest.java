/*
 * Copyright 2024 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pkhapps.commons.domain.primitives.geo.usa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StreetNameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "Main St.",              // Simple street name with abbreviation
            "Elm Avenue",            // Full street name with suffix
            "Downing Street",        // Street name without abbreviation
            "Pennsylvania Ave NW",   // Street name with directional suffix
            "5th Avenue",            // Ordinal street name
            "Sunset Blvd",           // Street name with abbreviation
            "South Park Ave",        // Street name with directional prefix
            "E. Maple Dr.",          // Street name with directional prefix and abbreviation
            "O'Connell Street",      // Street name with apostrophe
            "St. John's Blvd",       // Street name with period and apostrophe
            "Martin Luther King Jr. Blvd", // Street name with period and spaces
            "Cañon Blvd",            // Street name with a tilde (ñ)
            "Rue de l'Église",       // Street name with accent (É) and spaces
            "Château Ave.",          // Street name with accent (â)
            "Dr. Martin Luther King Jr. Blvd", // Street name with titles and periods
            "Queen's Walk",          // Street name with apostrophe
            "St. George's Rd.",      // Street name with period and apostrophe
            "L'Étang Dr.",           // Street name with accent (É) and abbreviation
            "Cooper's Alley"         // Street name with apostrophe
    })
    void can_create_valid_street_names(String input) {
        assertThat(StreetName.valueOf(input).toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Main St.@",             // Contains disallowed character '@'
            "Elm Ave#",              // Contains disallowed character '#'
            "Oak Street$",           // Contains disallowed character '$'
            "1234",                  // Contains only numbers
            "Sunset & Vine",         // Contains disallowed character '&'
            "Broadway!",             // Contains disallowed character '!'
            "1st Avenue%",           // Contains disallowed character '%'
            "/Main Street",          // Contains disallowed character '/'
            "Elm\\Avenue",           // Contains disallowed character '\\'
            "Ave * Maple",           // Contains disallowed character '*'
            "Maple Street;",         // Contains disallowed character ';'
            "<Main Avenue>",         // Contains disallowed characters '<' and '>'
            "Elm Street^",           // Contains disallowed character '^'
            "Cherry|Lane",           // Contains disallowed character '|'
            "Oak+Pine",              // Contains disallowed character '+'
            "Elm (Street)",          // Contains disallowed characters '(' and ')'
            "Main St.?",             // Contains disallowed character '?'
            "Elm Street`",           // Contains disallowed character '`'
            "Tab\tStreet"            // Contains a tab character (represented as '\t')
    })
    void refuses_to_create_invalid_street_names(String input) {
        assertThatThrownBy(() -> StreetName.valueOf(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void street_name_length_has_a_max_limit() {
        assertThatThrownBy(() -> StreetName.valueOf("X".repeat(StreetName.MAX_LENGTH + 1))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void street_names_with_same_value_are_equal() {
        var street1 = StreetName.valueOf("Downing Street");
        var street2 = StreetName.valueOf("Downing Street");
        assertThat(street1).isEqualTo(street2);
        assertThat(street1.hashCode()).isEqualTo(street2.hashCode());
    }

    @Test
    void street_names_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var streetName = StreetName.valueOf("Rue de l'Église");
        var json = objectMapper.writeValueAsString(streetName);
        var deserialized = objectMapper.readValue(json, StreetName.class);
        assertThat(streetName).isEqualTo(deserialized);
    }

    @Test
    void street_names_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"<Main Avenue>\"", StreetName.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void street_names_are_stripped() {
        assertThat(StreetName.valueOf(" Main Street\t").toString()).isEqualTo("Main Street");
    }
}
