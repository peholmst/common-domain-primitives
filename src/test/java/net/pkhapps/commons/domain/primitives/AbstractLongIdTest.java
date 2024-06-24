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
