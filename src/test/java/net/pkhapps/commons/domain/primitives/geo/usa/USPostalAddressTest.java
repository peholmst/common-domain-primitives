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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class USPostalAddressTest {

    @Test
    void postal_addresses_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var address = USPostalAddress.builder()
                .streetNumber("123")
                .streetName("Main St.")
                .secondaryAddressDesignator("Apt 4B")
                .city("New York")
                .state(USStateAndTerritory.NY)
                .zipCode("10001-1234")
                .build();
        var json = objectMapper.writeValueAsString(address);
        var deserialized = objectMapper.readValue(json, USPostalAddress.class);
        assertThat(address).isEqualTo(deserialized);
    }

    @Test
    void country_is_included_when_serialized_to_json() throws Exception {
        var objectMapper = new ObjectMapper();
        var address = USPostalAddress.builder()
                .streetNumber("123")
                .streetName("Main St.")
                .secondaryAddressDesignator("Apt 4B")
                .city("New York")
                .state(USStateAndTerritory.NY)
                .zipCode("10001-1234")
                .build();
        var json = objectMapper.writeValueAsString(address);
        assertThat(json).contains("\"country\":\"US\"");
    }

    @Test
    void country_is_ignored_when_deserialized_from_json() throws Exception {
        var objectMapper = new ObjectMapper();
        var address = objectMapper.readValue("{\"streetAddress\":{\"number\":\"123\",\"name\":\"Main St.\"},\"secondaryAddressDesignator\":\"Apt 4B\",\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10001-1234\",\"country\":\"FI\"}", USPostalAddress.class);
        assertThat(address.country()).isEqualTo(USPostalAddress.UNITED_STATES);
    }

    @Test
    void can_be_converted_to_a_generic_address() {
        var address = USPostalAddress.builder()
                .streetNumber("123")
                .streetName("Main St.")
                .secondaryAddressDesignator("Apt 4B")
                .city("New York")
                .state(USStateAndTerritory.NY)
                .zipCode("10001-1234")
                .build();
        var generic = address.toGenericAddress();
        assertThat(generic.line1()).isEqualTo("123 Main St.");
        assertThat(generic.line2()).isEqualTo("Apt 4B");
        assertThat(generic.line3()).isEqualTo("New York, NY 10001-1234");
        assertThat(generic.country()).isEqualTo(USPostalAddress.UNITED_STATES);
    }

    @Test
    void builder_can_be_used_to_edit_existing_addresses() {
        var address = USPostalAddress.builder()
                .streetNumber("123")
                .streetName("Main St.")
                .secondaryAddressDesignator("Apt 4B")
                .city("New York")
                .state(USStateAndTerritory.NY)
                .zipCode("10001-1234")
                .build();
        var editedAddress = USPostalAddress.builder(address)
                .streetNumber("124")
                .build();
        assertThat(editedAddress.streetAddress().number()).isEqualTo(StreetNumber.valueOf("124"));
        assertThat(editedAddress.streetAddress().name()).isEqualTo(address.streetAddress().name());
        assertThat(editedAddress.secondaryAddressDesignator()).isEqualTo(address.secondaryAddressDesignator());
        assertThat(editedAddress.city()).isEqualTo(address.city());
        assertThat(editedAddress.state()).isEqualTo(address.state());
        assertThat(editedAddress.zipCode()).isEqualTo(address.zipCode());
    }
}
