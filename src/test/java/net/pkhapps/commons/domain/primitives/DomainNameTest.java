package net.pkhapps.commons.domain.primitives;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        var domainName = DomainName.fromString(input);
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
        assertThatThrownBy(() -> DomainName.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void domain_names_with_same_value_are_equal() {
        var domainName1 = DomainName.fromString("example.com");
        var domainName2 = DomainName.fromString("example.com");
        assertThat(domainName1).isEqualTo(domainName2);
        assertThat(domainName1.hashCode()).isEqualTo(domainName2.hashCode());
    }

    @Test
    void domain_names_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var domainName = DomainName.fromString("example.com");
        var json = objectMapper.writeValueAsString(domainName);
        assertThat(json).isEqualTo("\"example.com\"");
    }

    @Test
    void domain_names_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var domainName = objectMapper.readValue("\"example.com\"", DomainName.class);
        assertThat(domainName.toString()).isEqualTo("example.com");
    }
}
