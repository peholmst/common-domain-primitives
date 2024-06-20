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
package net.pkhapps.commons.domain.primitives;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NanoIdTest {

    @Test
    void can_generate_random_nano_ids() {
        var nanoId1 = NanoId.randomNanoId();
        var nanoId2 = NanoId.randomNanoId();
        assertThat(nanoId1).isNotEqualTo(nanoId2);
    }

    @Test
    void can_create_nano_id_from_string() {
        var nanoId = NanoId.fromString("ku_qLNv1wDmIS5_EcT3j7");
        assertThat(nanoId.toString()).isEqualTo("ku_qLNv1wDmIS5_EcT3j7");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "ku_qLNv1wDmIS5_EcT3j7x",
            "ku_qLNv1wDmIS5_EcT3j",
            "ku-qLNv1wDmIS5_EcT3j7",
            "ku+qLNv1wDmIS5_EcT3j7",
            "ku/qLNv1wDmIS5_EcT3j7",
            "ku^qLNv1wDmIS5_EcT3j7",
            "ku.qLNv1wDmIS5_EcT3j7",
            "ku,qLNv1wDmIS5_EcT3j7",
            "ku:qLNv1wDmIS5_EcT3j7",
            "ku;qLNv1wDmIS5_EcT3j7",
            "kuåqLNv1wDmIS5_EcT3j7",
            "kuäqLNv1wDmIS5_EcT3j7",
            "ku*qLNv1wDmIS5_EcT3j7",
            "ku(qLNv1wDmIS5_EcT3j7",
            "ku)qLNv1wDmIS5_EcT3j7",
            "ku=qLNv1wDmIS5_EcT3j7",
            "ku[qLNv1wDmIS5_EcT3j7",
            "ku]qLNv1wDmIS5_EcT3j7",
    })
    void refuses_to_create_invalid_nano_ids(String input) {
        assertThatThrownBy(() -> NanoId.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nano_ids_with_same_value_are_equal() {
        var nanoId1 = NanoId.randomNanoId();
        var nanoId2 = NanoId.fromString(nanoId1.toString());
        assertThat(nanoId1).isEqualTo(nanoId2);
        assertThat(nanoId1.hashCode()).isEqualTo(nanoId2.hashCode());
    }

    @Test
    void nano_ids_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var nanoId = NanoId.fromString("ku_qLNv1wDmIS5_EcT3j7");
        var json = objectMapper.writeValueAsString(nanoId);
        assertThat(json).isEqualTo("\"ku_qLNv1wDmIS5_EcT3j7\"");
    }

    @Test
    void nano_ids_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var nanoId = objectMapper.readValue("\"ku_qLNv1wDmIS5_EcT3j7\"", NanoId.class);
        assertThat(nanoId.toString()).isEqualTo("ku_qLNv1wDmIS5_EcT3j7");
    }
}
