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

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractLongIdTest {

    public static class TestLongId extends AbstractLongId {

        private TestLongId(long value) {
            super(value);
        }

        public static TestLongId valueOf(long value) {
            return new TestLongId(value);
        }
    }

    @Test
    void ids_can_be_accessed_as_long() {
        assertThat(TestLongId.valueOf(123L).toLong()).isEqualTo(123L);
    }

    @Test
    void ids_can_be_converted_to_string() {
        assertThat(TestLongId.valueOf(123L).toString()).isEqualTo("123");
    }

    @Test
    void ids_with_same_value_are_equal() {
        var id1 = TestLongId.valueOf(123L);
        var id2 = TestLongId.valueOf(123L);
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    void ids_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var id = TestLongId.valueOf(123L);
        var json = objectMapper.writeValueAsString(id);
        assertThat(json).isEqualTo("123");
    }

    @Test
    void ids_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var domainName = objectMapper.readValue("123", TestLongId.class);
        assertThat(domainName.toLong()).isEqualTo(123L);
    }
}
