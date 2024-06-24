package net.pkhapps.commons.domain.primitives;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PhoneNumberTest {

    @Test
    void whitespace_is_removed() {
        assertThat(PhoneNumber.valueOf("+358 40 123 456").toString()).isEqualTo("+35840123456");
    }

    @Test
    void common_formatting_characters_are_removed() {
        assertThat(PhoneNumber.valueOf("+358-40-123-456").toString()).isEqualTo("+35840123456");
        assertThat(PhoneNumber.valueOf("+358.40.123.456").toString()).isEqualTo("+35840123456");
        assertThat(PhoneNumber.valueOf("(040) 123-4567").toString()).isEqualTo("0401234567");
    }

    @Test
    void phone_numbers_have_a_max_length() {
        assertThatThrownBy(() -> PhoneNumber.valueOf("+3581234567890987")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void phone_numbers_cannot_be_blank() {
        assertThatThrownBy(() -> PhoneNumber.valueOf(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void phone_numbers_can_contain_digits_only_and_a_plus() {
        assertThatThrownBy(() -> PhoneNumber.valueOf("A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> PhoneNumber.valueOf("1230<")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> PhoneNumber.valueOf("*df89#")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void plus_can_only_be_the_first_character() {
        assertThatThrownBy(() -> PhoneNumber.valueOf("123+456")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> PhoneNumber.valueOf("123456+")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void plus_cannot_be_the_only_character() {
        assertThatThrownBy(() -> PhoneNumber.valueOf("+")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void phone_numbers_with_same_value_are_equal() {
        var phoneNumber1 = PhoneNumber.valueOf("+35840123456");
        var phoneNumber2 = PhoneNumber.valueOf("+35840123456");
        assertThat(phoneNumber1).isEqualTo(phoneNumber2);
        assertThat(phoneNumber1.hashCode()).isEqualTo(phoneNumber2.hashCode());
    }

    @Test
    void phone_numbers_can_be_serialized_to_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var phoneNumber = PhoneNumber.valueOf("+35840123456");
        var json = objectMapper.writeValueAsString(phoneNumber);
        assertThat(json).isEqualTo("\"+35840123456\"");
    }

    @Test
    void phone_numbers_can_be_deserialized_from_json_using_jackson() throws Exception {
        var objectMapper = new ObjectMapper();
        var phoneNumber = objectMapper.readValue("\"+35840123456\"", PhoneNumber.class);
        assertThat(phoneNumber.toString()).isEqualTo("+35840123456");
    }

    @Test
    void phone_numbers_are_validated_when_deserialized_from_json_using_jackson() {
        var objectMapper = new ObjectMapper();
        assertThatThrownBy(() -> objectMapper.readValue("\"+35840123456ABCDEF\"", PhoneNumber.class))
                .isInstanceOf(ValueInstantiationException.class)
                .cause().isInstanceOf(IllegalArgumentException.class);
    }
}