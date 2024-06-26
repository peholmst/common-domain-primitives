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

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Base class for domain primitives that are used as wrappers of NanoIDs.
 *
 * @see <a href="https://github.com/aventrix/jnanoid">JNanoID</a>.
 */
public abstract class AbstractNanoId implements Serializable {

    public static final int LENGTH = NanoIdUtils.DEFAULT_SIZE;
    // Use a custom alphabet without the "-" because that makes it easier to copy-paste NanoIDs by double-clicking
    private static final char[] ALPHABET = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private final String value;

    protected AbstractNanoId(@NotNull String value) {
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
        var that = (AbstractNanoId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Checks if the given string is a valid NanoID.
     *
     * @param value the string to check.
     * @return {@code true} if the string is a valid NanoID, {@code false} otherwise.
     */
    public static boolean isValid(@NotNull String value) {
        // Check length
        if (value.length() != LENGTH) {
            return false;
        }
        // Check characters
        for (var c : value.toCharArray()) {
            if (!Character.isDigit(c) && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given string is a valid NanoID and returns it if so.
     *
     * @param value the string to check.
     * @return the passed-in string.
     * @throws IllegalArgumentException if the string was not a valid NanoID.
     */
    public static @NotNull String validate(@NotNull String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid NanoID");
        }
        return value;
    }

    /**
     * Generates a new random NanoID string.
     *
     * @return the new NanoID string.
     */
    protected static @NotNull String randomNanoIdString() {
        try {
            return NanoIdUtils.randomNanoId(SecureRandom.getInstanceStrong(), ALPHABET, LENGTH);
        } catch (NoSuchAlgorithmException ex) {
            return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, ALPHABET, LENGTH);
        }
    }
}
