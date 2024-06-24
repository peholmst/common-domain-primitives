package net.pkhapps.commons.domain.primitives.finland;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a Finnish personal identity code (henkilötunnus / personbeteckning). If you are paranoid,
 * use {@link ReadOncePersonalIdentityCode} instead to prevent the code from leaking accidentally.
 */
public final class PersonalIdentityCode implements Serializable {

    private static final char[] CONTROL_CHARS = {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'H',
            'J',
            'K',
            'L',
            'M',
            'N',
            'P',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y'
    };
    public static final int LENGTH = 11;

    private final String value;

    private PersonalIdentityCode(@NotNull String value) {
        this.value = requireNonNull(value);
    }

    /**
     * Checks if the person holding this identity code has the same birthdate as the given date.
     *
     * @param birthDate the birthdate to compare to.
     * @return {@code true} if the person has the same birthdate as the given date, {@code false} otherwise.
     */
    public boolean isSameBirthdateAs(@NotNull LocalDate birthDate) {
        var day = Integer.parseInt(value.substring(0, 2));
        var month = Integer.parseInt(value.substring(2, 4));
        var year = Integer.parseInt(value.substring(4, 6));
        var separator = value.charAt(6);
        if (separator == '+') {
            year = year + 1800;
        } else if (separator == '-') {
            year = year + 1900;
        } else if (separator == 'A') {
            year = year + 2000;
        }
        return birthDate.getDayOfMonth() == day
                && birthDate.getMonthValue() == month
                && birthDate.getYear() == year;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalIdentityCode that = (PersonalIdentityCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Checks if the given value is a valid personal identity code.
     *
     * @param value the value to check.
     * @return {@code true} if the value is a valid personal identity code, {@code false} otherwise.
     */
    public static boolean isValid(char[] value) {
        // Check length
        if (value.length != LENGTH) {
            return false;
        }
        // Check syntax
        for (var i = 0; i < 6; ++i) {
            if (value[i] < '0' || value[i] > '9') {
                return false;
            }
        }
        if (value[6] != '+' && value[6] != '-' && value[6] != 'A') {
            return false;
        }
        for (var i = 7; i < 10; ++i) {
            if (value[i] < '0' || value[i] > '9') {
                return false;
            }
        }
        if ((value[10] < '0' || value[10] > '9') && (value[10] < 'A' || value[10] > 'Y')) {
            return false;
        }
        // Check control char
        var actualControlChar = value[10];
        var expectedControlChar = calculateControlChar(value);
        return actualControlChar == expectedControlChar;
    }

    private static char calculateControlChar(char[] bic) {
        long[] number = {(bic[0] - '0') * 100000000L
                + (bic[1] - '0') * 10000000L
                + (bic[2] - '0') * 1000000L
                + (bic[3] - '0') * 100000L
                + (bic[4] - '0') * 10000L
                + (bic[5] - '0') * 1000L
                + (bic[7] - '0') * 100L
                + (bic[8] - '0') * 10L
                + (bic[9] - '0')};
        var reminder = number[0] % 31L;
        number[0] = 0; // Clear sensitive data
        return CONTROL_CHARS[(int) reminder];
    }

    /**
     * Creates a new {@code PersonalIdentityCode} from the given string.
     *
     * @param value the personal identity code to create.
     * @return the new {@code PersonalIdentityCode}
     * @throws IllegalArgumentException if the string is not a valid personal identity code.
     */
    @JsonCreator
    public static @NotNull PersonalIdentityCode valueOf(@NotNull String value) {
        if (!isValid(value.toCharArray())) {
            throw new IllegalArgumentException("Invalid personal identity code");
        }
        return new PersonalIdentityCode(value);
    }
}
