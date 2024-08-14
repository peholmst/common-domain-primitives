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

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a street name in a US address.
 */
public final class StreetName implements Serializable {

    public static final int MAX_LENGTH = 100;

    private final String value;

    private StreetName(@NotNull String value) {
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
        var that = (StreetName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Checks if the given string is a valid street name (without checking that the street actually exists).
     *
     * @param value the street name to validate.
     * @return {@code true} if the street name is syntactically valid, {@code false} otherwise.
     */
    public static boolean isValid(@NotNull String value) {
        if (value.isEmpty() || value.length() > MAX_LENGTH) {
            return false;
        }
        if (value.chars().allMatch(Character::isDigit)) {
            return false;
        }
        return value.chars().allMatch(ch -> Character.isAlphabetic(ch) || Character.isDigit((ch)) || ch == ' ' || ch == '.' || ch == '\'');
    }

    /**
     * Creates a new {@code StreetName} from the given string.
     *
     * @param value the street name to create.
     * @return the new {@code StreetName}.
     * @throws IllegalArgumentException if the value is not a valid street name.
     */
    @JsonCreator
    public static @NotNull StreetName valueOf(@NotNull String value) {
        var stripped = value.strip();
        if (!isValid(stripped)) {
            throw new IllegalArgumentException("Invalid street name");
        }
        return new StreetName(stripped);
    }
}
