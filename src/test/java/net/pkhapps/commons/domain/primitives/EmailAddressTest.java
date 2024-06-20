package net.pkhapps.commons.domain.primitives;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        var emailAddress = EmailAddress.fromString(input);
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
        assertThatThrownBy(() -> EmailAddress.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void email_addresses_with_same_value_are_equal() {
        var emailAddress1 = EmailAddress.fromString("user@example.com");
        var emailAddress2 = EmailAddress.fromString("user@example.com");
        assertThat(emailAddress1).isEqualTo(emailAddress2);
        assertThat(emailAddress1.hashCode()).isEqualTo(emailAddress2.hashCode());
    }

    @Test
    void email_addresses_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var emailAddress = EmailAddress.fromString("user@example.com");
        var json = objectMapper.writeValueAsString(emailAddress);
        assertThat(json).isEqualTo("\"user@example.com\"");
    }

    @Test
    void email_addresses_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var emailAddress = objectMapper.readValue("\"user@example.com\"", EmailAddress.class);
        assertThat(emailAddress.toString()).isEqualTo("user@example.com");
    }
}