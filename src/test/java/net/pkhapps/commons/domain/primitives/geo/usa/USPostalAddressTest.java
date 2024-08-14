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
        var address = USPostalAddress.of(
                StreetNumber.valueOf("123"),
                StreetName.valueOf("Main St."),
                SecondaryAddressDesignator.valueOf("Apt 4B"),
                CityName.valueOf("New York"),
                USStateAndTerritory.NY,
                ZipCode.valueOf("10001-1234")
        );
        var json = objectMapper.writeValueAsString(address);
        var deserialized = objectMapper.readValue(json, USPostalAddress.class);
        assertThat(address).isEqualTo(deserialized);
    }
}
