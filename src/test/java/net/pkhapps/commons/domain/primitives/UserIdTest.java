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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserIdTest {

    @Test
    void supports_google_identity_platform() {
        assertValidUserId("115012345678901234567");
    }

    @Test
    void supports_microsoft_azure_ad() {
        assertValidUserId("d3b8ec9f-3213-4bde-9fb3-1026b66efedb");
    }

    @Test
    void supports_auth0() {
        assertValidUserId("auth0|5f8c7a5e8e1d3e001c123456");
    }

    @Test
    void supports_okta() {
        assertValidUserId("00u123abcDEFGHIJ4567");
    }

    @Test
    void supports_amazon_cognito() {
        assertValidUserId("us-east-1:12345678-1234-1234-1234-123456789012");
    }

    @Test
    void supports_ping_identity() {
        assertValidUserId("b0123456-7890-1234-5678-9abcdef01234");
    }

    @Test
    void supports_keycloak() {
        assertValidUserId("f29ab8f6-6e2b-4e17-b908-1204a7c6e4b0");
    }

    @Test
    void supports_identity_server() {
        assertValidUserId("a1234567-bc89-1234-d567-890123456789");
    }

    @Test
    void supports_apple_sign_in() {
        assertValidUserId("001234.a12b3c456d7e890f8g9h12345");
    }

    @Test
    void supports_salesforce_identity() {
        assertValidUserId("005123456789012345");
    }

    @Test
    void user_ids_cannot_be_email_addresses() {
        assertInvalidUserId("joe.cool@nonexitent.com");
    }

    @Test
    void user_ids_cannot_be_empty() {
        assertInvalidUserId("");
    }

    @Test
    void user_ids_cannot_be_blank() {
        assertInvalidUserId(" ");
    }

    @Test
    void user_ids_have_a_max_length() {
        assertInvalidUserId("1".repeat(UserId.MAX_LENGTH + 1));
    }

    @Test
    void user_ids_cannot_contain_javascript() {
        assertInvalidUserId("alert('hello world');");
    }

    @Test
    void user_ids_cannot_contain_html() {
        assertInvalidUserId("<div>user</div>");
    }

    @Test
    void user_ids_cannot_contain_urls() {
        assertInvalidUserId("https://foo.bar/user");
    }

    @Test
    void user_ids_with_same_value_are_equal() {
        var userId1 = UserId.valueOf("115012345678901234567");
        var userId2 = UserId.valueOf("115012345678901234567");
        assertThat(userId1).isEqualTo(userId2);
        assertThat(userId1.hashCode()).isEqualTo(userId2.hashCode());
    }

    @Test
    void user_ids_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var userId = UserId.valueOf("auth0|5f8c7a5e8e1d3e001c123456");
        var json = objectMapper.writeValueAsString(userId);
        assertThat(json).isEqualTo("\"auth0|5f8c7a5e8e1d3e001c123456\"");
    }

    @Test
    void user_ids_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var userId = objectMapper.readValue("\"auth0|5f8c7a5e8e1d3e001c123456\"", UserId.class);
        assertThat(userId.toString()).isEqualTo("auth0|5f8c7a5e8e1d3e001c123456");
    }

    @Test
    void user_ids_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"id;with,invalid/characters\"", UserId.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }

    private static void assertValidUserId(String userId) {
        assertThat(UserId.valueOf(userId).toString()).isEqualTo(userId);
    }

    private static void assertInvalidUserId(String userId) {
        assertThatThrownBy(() -> UserId.valueOf(userId)).isInstanceOf(IllegalArgumentException.class);
    }
}
