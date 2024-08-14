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

public class ZipCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "02108-1234",
            "98101-9876",
            "30301",
            "90210"
    })
    void can_create_valid_zip_codes(String value) {
        assertThat(ZipCode.valueOf(value).toString()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "02108-",
            " 02108-1234",
            "ABC",
            "02108 1234",
            "021081234",
            "0210",
            "02108-123",
            "02108-12345"
    })
        // TODO Consider some sanitation in the same way as PhoneNumber sanitizes its input
    void refuses_to_create_invalid_zip_codes(String value) {
        assertThatThrownBy(() -> ZipCode.valueOf(value)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void zip_codes_with_same_value_are_equal() {
        var zipCode1 = ZipCode.valueOf("90210");
        var zipCode2 = ZipCode.valueOf("90210");
        assertThat(zipCode1).isEqualTo(zipCode2);
        assertThat(zipCode1.hashCode()).isEqualTo(zipCode2.hashCode());
    }

    @Test
    void zip_codes_can_be_serialized_and_deserialized_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var zipCode = ZipCode.valueOf("90210-5678");
        var json = objectMapper.writeValueAsString(zipCode);
        var deserialized = objectMapper.readValue(json, ZipCode.class);
        assertThat(zipCode).isEqualTo(deserialized);
    }

    @Test
    void zip_codes_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"90210-ABCD\"", ZipCode.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}
