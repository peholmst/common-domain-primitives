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
package net.pkhapps.commons.domain.primitives.geo.usa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a US zip code.
 */
public final class ZipCode implements Serializable {

    public static final int MAX_LENGTH = 10;
    public static final int MIN_LENGTH = 5;
    private static final Pattern REGEX = Pattern.compile("^[0-9]{5}(-[0-9]{4})?$");

    private final String value;

    private ZipCode(@NotNull String value) {
        this.value = requireNonNull(value);
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
        var that = (ZipCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Checks if the given string is a syntactically valid zip code (without checking that the zip code actually exists).
     *
     * @param value the zip code to validate.
     * @return {@code true} if the zip code is syntactically valid, {@code false} otherwise.
     */
    public static boolean isValid(@NotNull String value) {
        // Check length
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            return false;
        }
        // All characters should be ASCII digits or a '-'
        if (!value.chars().allMatch(ch -> (ch >= '0' && ch <= '9') || (ch == '-'))) {
            return false;
        }
        return REGEX.matcher(value).matches();
    }

    /**
     * Creates a new {@code ZipCode} from the given string.
     *
     * @param value the zip code to create.
     * @return the new {@code ZipCode}.
     * @throws IllegalArgumentException if the value is not a syntactically valid zip code.
     */
    @JsonCreator
    public static @NotNull ZipCode valueOf(@NotNull String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid zip code");
        }
        return new ZipCode(value);
    }
}
