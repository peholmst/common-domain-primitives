package net.pkhapps.commons.domain.primitives.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.commons.domain.primitives.geo.usa.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest {

    @Test
    void can_serialize_and_deserialize_us_addresses_using_interface_as_type() throws Exception {
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
        System.out.println(json);
        var deserialized = objectMapper.readValue(json, Address.class);
        assertThat(address).isEqualTo(deserialized);
    }

    @Test
    void can_serialize_and_deserialize_generic_addresses_using_interface_as_type() throws Exception {
        var objectMapper = new ObjectMapper();
        var address = new GenericAddress("Acme Street 1", "Apartment 1", "12345 Toontown", Country.ofName("Nonexistent Country"));
        var json = objectMapper.writeValueAsString(address);
        System.out.println(json);
        var deserialized = objectMapper.readValue(json, Address.class);
        assertThat(address).isEqualTo(deserialized);
    }
}