package net.pkhapps.commons.domain.primitives;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IpAddressTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "192.168.1.1",
            "172.16.0.1",
            "10.0.0.1",
            "8.8.8.8",
            "203.0.113.1",
            "198.51.100.14",
            "192.0.2.235",
            "1.1.1.1",
            "93.184.216.34",
            "198.18.0.5",
            "0.0.0.0",
            "255.255.255.255"
    })
    void can_create_Ipv4_addresses(String input) {
        var ipAddress = IpAddress.fromString(input);
        assertThat(ipAddress).isInstanceOf(IpAddress.Ipv4.class);
        assertThat(ipAddress.toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "256.256.256.256",
            "192.168.1.999",
            "123.456.78.90",
            "192.168.1.-1",
            "300.0.0.1",
            "192.168.1.1.1",
            "192.168.1",
            "192.168.1.256",
            "192.168.1..1",
            "aaa.bbb.ccc.ddd"
    })
    void refuses_to_create_invalid_ipv4_addresses(String input) {
        assertThatThrownBy(() -> IpAddress.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
            "0000:0000:0000:0000:0000:0000:0000:0000",
            "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff",
            "2001:db8:0:1234:0:567:8:1",
            "fe80:0:0:0:202:b3ff:fe1e:8329",
            "fe80::202:b3ff:fe1e:8329",
            "2001:db8::1",
            "::1",
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
            "2001:db8:0:0:0:0:2:1",
            "2001:db8::2:1",
            "0:0:0:0:0:0:0:1",
            "::"
    })
    void can_create_Ipv6_addresses(String input) {
        var ipAddress = IpAddress.fromString(input);
        assertThat(ipAddress).isInstanceOf(IpAddress.Ipv6.class);
        assertThat(ipAddress.toString()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334:1234",
            "2001:db8:0:1234:0:567:8:1:12345",
            "fe80:::202:b3ff:fe1e:8329",
            "2001:db8::g",
            "2001::85a3::8a2e:0370:7334",
            "12345::5678:abcd:ef12:3456:7890:abcd",
            "2001:db8:85a3:0000:0000:8a2e:0370:7334g",
            "2001:db8:85a3:0000:0000:8a2e:0370",
            ":2001:db8::1",
            "2001:db8::1:",
            "2001:db8::85a3::7334"
    })
    void refuses_to_create_invalid_ipv6_addresses(String input) {
        assertThatThrownBy(() -> IpAddress.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ipv4_addresses_with_same_value_are_equal() {
        var ipAddress1 = IpAddress.fromString("192.168.1.2");
        var ipAddress2 = IpAddress.fromString("192.168.1.2");
        assertThat(ipAddress1).isEqualTo(ipAddress2);
        assertThat(ipAddress1.hashCode()).isEqualTo(ipAddress2.hashCode());
    }

    @Test
    void ipv6_addresses_with_same_value_are_equal() {
        var ipAddress1 = IpAddress.fromString("2001:db8::1");
        var ipAddress2 = IpAddress.fromString("2001:db8::1");
        assertThat(ipAddress1).isEqualTo(ipAddress2);
        assertThat(ipAddress1.hashCode()).isEqualTo(ipAddress2.hashCode());
    }

    @Test
    void ipv4_addresses_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var ipAddress = IpAddress.fromString("192.168.1.1");
        var json = objectMapper.writeValueAsString(ipAddress);
        assertThat(json).isEqualTo("\"192.168.1.1\"");
    }

    @Test
    void ipv4_addresses_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var ipAddress = objectMapper.readValue("\"192.168.1.1\"", IpAddress.class);
        assertThat(ipAddress.toString()).isEqualTo("192.168.1.1");
    }

    @Test
    void ipv6_addresses_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var ipAddress = IpAddress.fromString("2001:db8::1");
        var json = objectMapper.writeValueAsString(ipAddress);
        assertThat(json).isEqualTo("\"2001:db8::1\"");
    }

    @Test
    void ipv6_addresses_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var ipAddress = objectMapper.readValue("\"2001:db8::1\"", IpAddress.class);
        assertThat(ipAddress.toString()).isEqualTo("2001:db8::1");
    }
}
