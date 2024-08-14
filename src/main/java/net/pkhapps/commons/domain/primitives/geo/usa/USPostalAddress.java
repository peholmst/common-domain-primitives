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

import net.pkhapps.commons.domain.primitives.geo.Address;
import net.pkhapps.commons.domain.primitives.geo.Country;
import net.pkhapps.commons.domain.primitives.geo.GenericAddress;
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
     * Creates a new {@code USPostalAddress}.
     *
     * @param streetAddress the street address.
     * @param city          the city.
     * @param state         the state or territory.
     * @param zipCode       the zip code.
     * @return the new {@code USPostalAddress}.
     */
    public static @NotNull USPostalAddress of(@NotNull StreetAddress streetAddress, @NotNull CityName city, @NotNull USStateAndTerritory state, @NotNull ZipCode zipCode) {
        return new USPostalAddress(streetAddress, null, city, state, zipCode);
    }

    /**
     * Creates a new {@code USPostalAddress}.
     *
     * @param streetName the street name.
     * @param city       the city.
     * @param state      the state or territory.
     * @param zipCode    the zip code.
     * @return the new {@code USPostalAddress}.
     */
    public static @NotNull USPostalAddress of(@NotNull StreetName streetName, @NotNull CityName city, @NotNull USStateAndTerritory state, @NotNull ZipCode zipCode) {
        return of(null, streetName, null, city, state, zipCode);
    }

    /**
     * Creates a new {@code USPostalAddress}.
     *
     * @param streetNumber the street number, or {@code null}.
     * @param streetName   the street name.
     * @param city         the city.
     * @param state        the state or territory.
     * @param zipCode      the zip code.
     * @return the new {@code USPostalAddress}.
     */
    public static @NotNull USPostalAddress of(@Nullable StreetNumber streetNumber, @NotNull StreetName streetName, @NotNull CityName city, @NotNull USStateAndTerritory state, @NotNull ZipCode zipCode) {
        return of(null, streetName, null, city, state, zipCode);
    }

    /**
     * Creates a new {@code USPostalAddress}.
     *
     * @param streetNumber               the street number, or {@code null}.
     * @param streetName                 the street name.
     * @param secondaryAddressDesignator the secondary address designator (such as an apartment or P.O. Box number), or {@code null}.
     * @param city                       the city.
     * @param state                      the state or territory.
     * @param zipCode                    the zip code.
     * @return the new {@code USPostalAddress}.
     */
    public static @NotNull USPostalAddress of(@Nullable StreetNumber streetNumber, @NotNull StreetName streetName, @Nullable SecondaryAddressDesignator secondaryAddressDesignator, @NotNull CityName city, @NotNull USStateAndTerritory state, @NotNull ZipCode zipCode) {
        return new USPostalAddress(new StreetAddress(streetNumber, streetName), secondaryAddressDesignator, city, state, zipCode);
    }
}
