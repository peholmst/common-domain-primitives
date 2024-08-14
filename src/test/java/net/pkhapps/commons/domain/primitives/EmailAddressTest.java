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

public class EmailAddressTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "firstname.lastname@example.com",
            "user123@example.org",
            "user.name+tag+sorting@example.com",
            "user_name@example.co.uk",
            "username@example.travel",
            "user-name@subdomain.example.com",
            "user.name@example.museum",
            "user@[123.123.123.123]",
            "user@[IPv6:2001:db8::1]"
    })
    void can_create_valid_email_addresses(String input) {
        var emailAddress = EmailAddress.valueOf(input);
        assertThat(emailAddress.toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missingusername.com",
            "user..name@example.com",
            "username@.com",
            "username@.com.",
            "missingdomain@",
            "username@.com..com",
            "username@domain..com",
            "username@domain.com.",
            "username@-domain.com",
            "user@domain@domain.com",
            "user@domain,com",
            "user name@domain.com",
            "user@domain..com",
            "user@domain-.com",
            "user@domain.com-",
            ".user@domain.com",
            "user.@domain.com",
            "user@domain.com space",
            "user@domain#.com",
            "user@domain$.com",
            "user@domain%.com",
            "user@domain^.com",
            "user@domain&.com",
            "user@domain*.com",
            "user@domain(.com",
            "user@domain).com",
            "user@domain!.com",
            "user@domain~.com",
            "user@domain`.com",
            "user@domain+.com",
            "user@domain=.com",
            "user@domain{.com",
            "user@domain}.com",
            "user@domain[.com",
            "user@domain].com",
            "user@domain|.com",
            "user@domain\\.com",
            "user@domain;.com",
            "user@domain'.com",
            "user@domain<.com",
            "user@domain>.com",
            "user@domain?.com",
            "user@domain/.com",
            "invalid_ipv4@[192.168.1.256]",
            "missing_ipv6_prefix@[2001:db8::1]"
    })
    void refuses_to_create_invalid_email_addresses(String input) {
        assertThatThrownBy(() -> EmailAddress.valueOf(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void email_addresses_with_same_value_are_equal() {
        var emailAddress1 = EmailAddress.valueOf("user@example.com");
        var emailAddress2 = EmailAddress.valueOf("user@example.com");
        assertThat(emailAddress1).isEqualTo(emailAddress2);
        assertThat(emailAddress1.hashCode()).isEqualTo(emailAddress2.hashCode());
    }

    @Test
    void email_addresses_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var emailAddress = EmailAddress.valueOf("user@example.com");
        var json = objectMapper.writeValueAsString(emailAddress);
        assertThat(json).isEqualTo("\"user@example.com\"");
    }

    @Test
    void email_addresses_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var emailAddress = objectMapper.readValue("\"user@example.com\"", EmailAddress.class);
        assertThat(emailAddress.toString()).isEqualTo("user@example.com");
    }

    @Test
    void email_addresses_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"user@domain<.com\"", EmailAddress.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void email_addresses_have_a_verified_subtype() {
        var email = EmailAddress.valueOf("user@example.com");
        var verified = email.toVerified();
        assertThat(email.toString()).isEqualTo(verified.toString());
    }

    @Test
    void verified_email_addresses_are_not_equal_to_unverified_ones() {
        var email = EmailAddress.valueOf("user@example.com");
        assertThat(email).isNotEqualTo(email.toVerified());
    }

    @Test
    void verified_email_addresses_also_work_with_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var verified = EmailAddress.Verified.valueOf("user@example.com");
        var json = objectMapper.writeValueAsString(verified);
        var deserialized = objectMapper.readValue(json, EmailAddress.Verified.class);
        assertThat(verified).isEqualTo(deserialized);
        assertThatThrownBy(() -> objectMapper.readValue("\"user@domain<.com\"", EmailAddress.Verified.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}