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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import net.pkhapps.commons.domain.primitives.geo.Address;
import net.pkhapps.commons.domain.primitives.geo.Country;
import net.pkhapps.commons.domain.primitives.geo.GenericAddress;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a postal address in the United States.
 *
 * @param streetAddress              the street address (required).
 * @param secondaryAddressDesignator the secondary address designator, such as an apartment or P.O. Box number (optional).
 * @param city                       the city (required).
 * @param state                      the state or territory (required).
 * @param zipCode                    the zip code (required).
 */
@JsonTypeName("US")
public record USPostalAddress(@NotNull StreetAddress streetAddress,
                              @Nullable SecondaryAddressDesignator secondaryAddressDesignator,
                              @NotNull CityName city,
                              @NotNull USStateAndTerritory state,
                              @NotNull ZipCode zipCode) implements Address {

    public static final Country UNITED_STATES = Country.ofIsoCode("US");

    public USPostalAddress {
        requireNonNull(streetAddress, "streetAddress must not be null");
        requireNonNull(city, "city must not be null");
        requireNonNull(state, "state must not be null");
        requireNonNull(zipCode, "zipCode must not be null");
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public @NotNull Country country() {
        return UNITED_STATES;
    }

    @Override
    public @NotNull GenericAddress toGenericAddress() {
        return new GenericAddress(line1(), line2(), line3(), country());
    }

    private @NotNull String line1() {
        if (streetAddress.number() == null) {
            return streetAddress.name().toString();
        } else {
            return "%s %s".formatted(streetAddress.number(), streetAddress.name());
        }
    }

    private @Nullable String line2() {
        return secondaryAddressDesignator == null ? null : secondaryAddressDesignator.toString();
    }

    private @NotNull String line3() {
        return "%s, %s %s".formatted(city, state, zipCode);
    }

    /**
     * Creates a new builder for creating new {@code USPostalAddress} instances.
     *
     * @return the new builder.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new builder for creating new {@code USPostalAddress} instances based on the given instance.
     *
     * @param from the address to initialize the builder from.
     * @return the new builder.
     */
    public static @NotNull Builder builder(@NotNull USPostalAddress from) {
        return new Builder(from);
    }

    /**
     * Builder for creating new {@link USPostalAddress} instances.
     */
    public static final class Builder {

        private StreetNumber streetNumber;
        private StreetName streetName;
        private SecondaryAddressDesignator secondaryAddressDesignator;
        private CityName city;
        private USStateAndTerritory state;
        private ZipCode zipCode;

        private Builder() {
        }

        private Builder(@NotNull USPostalAddress from) {
            streetNumber = from.streetAddress.number();
            streetName = from.streetAddress.name();
            secondaryAddressDesignator = from.secondaryAddressDesignator;
            city = from.city;
            state = from.state;
            zipCode = from.zipCode;
        }

        public @NotNull Builder streetNumber(@Nullable StreetNumber streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public @NotNull Builder streetNumber(@NotNull String streetNumber) {
            return streetNumber(StreetNumber.valueOf(streetNumber));
        }

        public @NotNull Builder streetName(@Nullable StreetName streetName) {
            this.streetName = streetName;
            return this;
        }

        public @NotNull Builder streetName(@NotNull String streetName) {
            return streetName(StreetName.valueOf(streetName));
        }

        public @NotNull Builder secondaryAddressDesignator(@Nullable SecondaryAddressDesignator secondaryAddressDesignator) {
            this.secondaryAddressDesignator = secondaryAddressDesignator;
            return this;
        }

        public @NotNull Builder secondaryAddressDesignator(@NotNull String secondaryAddressDesignator) {
            return secondaryAddressDesignator(SecondaryAddressDesignator.valueOf(secondaryAddressDesignator));
        }

        public @NotNull Builder city(@Nullable CityName city) {
            this.city = city;
            return this;
        }

        public @NotNull Builder city(@NotNull String city) {
            return city(CityName.valueOf(city));
        }

        public @NotNull Builder state(@Nullable USStateAndTerritory state) {
            this.state = state;
            return this;
        }

        public @NotNull Builder zipCode(@Nullable ZipCode zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public @NotNull Builder zipCode(@NotNull String zipCode) {
            return zipCode(ZipCode.valueOf(zipCode));
        }

        /**
         * Creates a new {@link USPostalAddress} instance. This method can be called multiple times and the builder
         * can be modified in between calls if necessary.
         *
         * @return a new {@link USPostalAddress} instance.
         * @throws IllegalStateException if required field values are missing from the builder.
         */
        public @NotNull USPostalAddress build() {
            require(streetName, "streetName");
            require(city, "city");
            require(state, "state");
            require(zipCode, "zipCode");
            return new USPostalAddress(new StreetAddress(streetNumber, streetName), secondaryAddressDesignator, city, state, zipCode);
        }

        @Contract("null,_ -> fail")
        private static void require(@Nullable Object field, @NotNull String fieldName) {
            if (field == null) {
                throw new IllegalStateException(fieldName + " is missing");
            }
        }
    }
}
