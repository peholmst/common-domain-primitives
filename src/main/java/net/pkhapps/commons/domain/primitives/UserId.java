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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a user ID. This is not the login name, but the internal ID used by the identity
 * provider to identify a user. In OIDC, this is typically the value of the {@code sub}, {@code uid} or {@code oid}
 * field of the ID token.
 */
public final class UserId implements Serializable, Principal {

    public static final int MAX_LENGTH = 100;

    private final String value;

    private UserId(@NotNull String value) {
        this.value = requireNonNull(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (UserId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Checks if the given string is a valid user ID (without actually checking that the user ID exists). Valid IDs
     * can contain digits 0 to 9, ASCII letters in upper and lower case, dashes (-), periods (.), pipes (|),
     * colons (:), and underscores (_). This should cover most identity management systems.
     * <p>
     * <b>Please note that usernames and e-mail addresses should not be used as user IDs, as these may change.</b>
     *
     * @param value the string to check.
     * @return {@code true} if the string is a valid user ID, {@code false} otherwise.
     */
    public static boolean isValid(@NotNull String value) {
        if (value.isEmpty() || value.length() > MAX_LENGTH) {
            return false;
        }
        for (var c : value.toCharArray()) {
            if (!isLegalCharacter(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLegalCharacter(char chr) {
        return (chr >= '0' && chr <= '9')
                || (chr >= 'a' && chr <= 'z')
                || (chr >= 'A' && chr <= 'Z')
                || (chr == '-')
                || (chr == '_')
                || (chr == '.')
                || (chr == ':')
                || (chr == '|');
    }

    /**
     * Creates a new {@code UserId} from the given string.
     *
     * @param value the user ID to create.
     * @return the new {@code UserId}.
     * @throws IllegalArgumentException if the string is not a valid user ID.
     * @see #isValid(String)
     */
    @JsonCreator
    public static @NotNull UserId valueOf(@NotNull String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return new UserId(value);
    }
}
