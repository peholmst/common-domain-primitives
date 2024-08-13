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
package net.pkhapps.commons.domain.primitives.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CountryTest {

    @Test
    void can_be_created_from_iso_code() {
        var finland = Country.ofIsoCode("FI");
        assertThat(finland.displayName(Locale.ENGLISH)).isEqualTo("Finland");
        assertThat(finland.displayName(Locale.GERMAN)).isEqualTo("Finnland");
    }

    @Test
    void can_be_created_from_locale() {
        var germany = Country.ofLocale(Locale.GERMANY);
        assertThat(germany.displayName(Locale.ENGLISH)).isEqualTo("Germany");
        assertThat(germany.displayName(Locale.GERMAN)).isEqualTo("Deutschland");
    }

    @Test
    void validates_iso_code() {
        assertThatThrownBy(() -> Country.ofIsoCode("åäö")).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Îles Caïmans",
            "Centrafricaine, République",
            "Cocos (Keeling) Islands",
            "Finland",
            "Al Jaza'ir",
            "Kūki 'Āirani",
            "Côte D'ivoire",
            "al-ʿIrāq",
            "Aolepān Aorōkin Ṃajeḷ",
            "Åland"
    })
    void can_be_created_from_name(String name) {
        assertThat(Country.ofName(name).displayName()).isEqualTo(name);
    }

    @Test
    void user_named_countries_have_max_length() {
        assertThatThrownBy(() -> Country.ofName("A".repeat(Country.UserNamedCountry.MAX_LENGTH + 1))).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "\t",
            "\"",
            ";",
            "<",
            ">",
            ":",
            "|",
            "_"
    })
    void validates_user_named_countries_to_some_extent(String input) {
        assertThatThrownBy(() -> Country.ofName(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void iso_countries_are_available_as_list() {
        var isoCountryCodes = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
        assertThat(Country.isoCountries()).hasSameSizeAs(isoCountryCodes);
        assertThat(Country.isoCountries()).allMatch(isoCountry -> isoCountryCodes.contains(isoCountry.locale().getCountry()));
    }

    @Test
    void iso_countries_with_same_locale_are_equal() {
        var country1 = Country.ofIsoCode("fi");
        var country2 = Country.ofIsoCode("FI");
        assertThat(country1).isEqualTo(country2);
        assertThat(country1.hashCode()).isEqualTo(country2.hashCode());
    }

    @Test
    void user_named_countries_with_same_name_are_equal() {
        var country1 = Country.ofName("Suomi");
        var country2 = Country.ofName("Suomi");
        assertThat(country1).isEqualTo(country2);
        assertThat(country1.hashCode()).isEqualTo(country2.hashCode());
    }

    @Test
    void iso_countries_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var country = Country.ofIsoCode("fi");
        var json = objectMapper.writeValueAsString(country);
        assertThat(json).isEqualTo("\"FI\"");
    }

    @Test
    void user_named_countries_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var country = Country.ofName("Suomi");
        var json = objectMapper.writeValueAsString(country);
        assertThat(json).isEqualTo("\"Suomi\"");
    }

    @Test
    void iso_countries_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var country = objectMapper.readValue("\"SE\"", Country.class);
        assertThat(country).isInstanceOf(Country.IsoCountry.class);
        assertThat(country.displayName(Locale.ENGLISH)).isEqualTo("Sweden");
    }

    @Test
    void user_named_countries_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var country = objectMapper.readValue("\"Suomi\"", Country.class);
        assertThat(country).isInstanceOf(Country.UserNamedCountry.class);
        assertThat(country.displayName()).isEqualTo("Suomi");
    }

    @Test
    void countries_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"<Finland>\"", Country.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}
