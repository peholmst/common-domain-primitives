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

public class CityNameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "New York",               // Space
            "St. Louis",              // Period
            "El Paso",                // Space
            "O'Fallon",               // Apostrophe
            "San José",               // Accent
            "Grand Forks",            // Space
            "La Cañada Flintridge",   // Space, Tilde
            "Coeur d'Alene",          // Space, Apostrophe
            "Boca Raton",             // Space
            "D'Iberville"             // Apostrophe
    })
    void can_create_valid_city_names(String input) {
        assertThat(CityName.valueOf(input).toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1234 City",             // Starts with digits
            "City@Place",            // Contains special character '@'
            "City!Name",             // Contains special character '!'
            "City#1",                // Contains special character '#'
            "City$",                 // Contains special character '$'
            "City%Town",             // Contains special character '%'
            "City^Town",             // Contains special character '^'
            "City&Village",          // Contains special character '&'
            "City*",                 // Contains special character '*'
            "City/Place",            // Contains special character '/'
            "City\tPlace"            // Contains tab character
    })
    void refuses_to_create_invalid_city_names(String input) {
        assertThatThrownBy(() -> CityName.valueOf(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void city_name_length_has_a_max_limit() {
        assertThatThrownBy(() -> CityName.valueOf("X".repeat(CityName.MAX_LENGTH + 1))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void city_names_with_same_value_are_equal() {
        var city1 = CityName.valueOf("New York");
        var city2 = CityName.valueOf("New York");
        assertThat(city1).isEqualTo(city2);
        assertThat(city1.hashCode()).isEqualTo(city2.hashCode());
    }

    @Test
    void city_names_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var cityName = CityName.valueOf("La Cañada Flintridge");
        var json = objectMapper.writeValueAsString(cityName);
        var deserialized = objectMapper.readValue(json, CityName.class);
        assertThat(cityName).isEqualTo(deserialized);
    }

    @Test
    void city_names_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"123 City\"", CityName.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}
