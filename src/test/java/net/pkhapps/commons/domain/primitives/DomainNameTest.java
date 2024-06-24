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
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DomainNameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "example.com",
            "subdomain.example.com",
            "example.org",
            "my-website.net",
            "hello-world.co",
            "valid-domain.io",
            "example123.com",
            "test.example.edu",
            "example-company.biz",
            "example.travel",
            "xn--kbenhavn-54a.eu"
    })
    void can_create_valid_domain_names(String input) {
        var domainName = DomainName.valueOf(input);
        assertThat(domainName.toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "example..com",
            "-example.com",
            "example.com-",
            "example@com",
            "example,com",
            "example..org",
            "example_123.com",
            "test@example.com",
            ".example.com",
            "example .com",
            "example.com."
    })
    void refuses_to_create_invalid_domain_names(String input) {
        assertThatThrownBy(() -> DomainName.valueOf(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void domain_names_with_same_value_are_equal() {
        var domainName1 = DomainName.valueOf("example.com");
        var domainName2 = DomainName.valueOf("example.com");
        assertThat(domainName1).isEqualTo(domainName2);
        assertThat(domainName1.hashCode()).isEqualTo(domainName2.hashCode());
    }

    @Test
    void domain_names_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var domainName = DomainName.valueOf("example.com");
        var json = objectMapper.writeValueAsString(domainName);
        assertThat(json).isEqualTo("\"example.com\"");
    }

    @Test
    void domain_names_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var domainName = objectMapper.readValue("\"example.com\"", DomainName.class);
        assertThat(domainName.toString()).isEqualTo("example.com");
    }

    @Test
    void domain_names_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"example.com@\"", DomainName.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}
