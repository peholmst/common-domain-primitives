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
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing an IP address (either IPv4 or IPv6).
 */
public sealed abstract class IpAddress implements Serializable {

    private final String value;

    protected IpAddress(@NotNull String value) {
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
        var that = (IpAddress) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Domain primitive representing an IPv4 address.
     */
    public static final class Ipv4 extends IpAddress {

        public static final int MIN_LENGTH = 7; // 0.0.0.0
        public static final int MAX_LENGTH = 15; // 255.255.255.255

        private Ipv4(@NotNull String value) {
            super(value);
        }

        /**
         * Checks if the given string is a valid IPv4 address.
         *
         * @param value the string to check.
         * @return {@code true} if the string is a valid IPv4 address, {@code false} otherwise.
         */
        public static boolean isValidIpv4(@NotNull String value) {
            // Check length
            if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
                return false;
            }
            // Check number of octets
            var octets = value.split("\\.");
            if (octets.length != 4) {
                return false;
            }
            // Check that each octet is a number between 0 and 255
            for (var octet : octets) {
                try {
                    var number = Integer.parseInt(octet);
                    if (number < 0 || number > 255) {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Domain primitive representing an IPv6 address.
     */
    public static final class Ipv6 extends IpAddress {

        public static final int MIN_LENGTH = 2; // ::
        public static final int MAX_LENGTH = 39; // 2001:0db8:85a3:0000:0000:8a2e:0370:7334

        private Ipv6(@NotNull String value) {
            super(value);
        }

        /**
         * Checks if the given string is a valid IPv6 address.
         *
         * @param value the string to check.
         * @return {@code true} if the string is a valid IPv6 address, {@code false} otherwise.
         */
        public static boolean isValidIpv6(@NotNull String value) {
            // Check length
            if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
                return false;
            }
            // Check number of quibbles
            var quibbles = value.split(":", -1);
            if (quibbles.length < 3 || quibbles.length > 8) {
                return false;
            }
            // Check that each quibble is either empty or a valid hexadecimal number
            for (var quibble : quibbles) {
                if (quibble.isEmpty()) {
                    continue;
                }
                if (quibble.length() > 4) {
                    return false;
                }
                for (var c : quibble.toCharArray()) {
                    if (!Character.isDigit(c) && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                        return false;
                    }
                }
            }
            var doubleColon = value.indexOf("::");
            // Check that if there is no double colon, there are exactly 8 quibbles
            if (doubleColon == -1 && quibbles.length != 8) {
                return false;
            }
            // Check that if there is a double colon, it occurs at most once
            if (doubleColon > -1 && value.indexOf("::", doubleColon + 1) != -1) {
                return false;
            }
            // Check that if there is a double colon, and it is not at the beginning of the string, the first quibble must not be empty
            if (doubleColon > 0 && quibbles[0].isEmpty()) {
                return false;
            }
            // Check that if there is a double colon, and it is not at the end of the string, the last quibble must not be empty
            if (doubleColon < value.length() - 2 && quibbles[quibbles.length - 1].isEmpty()) {
                return false;
            }

            return true;
        }
    }

    /**
     * Creates a new {@code IpAddress} from the given string.
     *
     * @param value the IP address to create.
     * @return the new {@code IpAddress}.
     * @throws IllegalArgumentException if the string is not a valid IP address.
     */
    @JsonCreator
    public static @NotNull IpAddress valueOf(@NotNull String value) {
        if (Ipv4.isValidIpv4(value)) {
            return new Ipv4(value);
        } else if (Ipv6.isValidIpv6(value)) {
            return new Ipv6(value);
        } else {
            throw new IllegalArgumentException("Invalid IP address");
        }
    }
}
