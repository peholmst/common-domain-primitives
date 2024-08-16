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
package net.pkhapps.commons.domain.primitives.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a country.
 */
public final class Country implements Serializable {

    private static final List<Country> ISO_COUNTRIES;

    static {
        ISO_COUNTRIES = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2).stream().map(Country::ofIsoCode).toList();
    }

    private final Locale locale;

    private Country(@NotNull Locale locale) {
        this.locale = requireNonNull(locale);
    }

    /**
     * Gets the locale of the country.
     *
     * @return the locale.
     */
    public @NotNull Locale locale() {
        return locale;
    }

    /**
     * Gets the ISO 3166 code of the country,
     *
     * @return the ISO code.
     */
    @JsonValue
    public @NotNull String isoCode() {
        return locale.getCountry();
    }

    /**
     * Gets the display name of the country.
     *
     * @return the display name of the country.
     */
    public @NotNull String displayName() {
        return displayName(null);
    }

    /**
     * The display name of the country, in the given locale if applicable.
     *
     * @param displayIn the locale to display the country's name in, if relevant.
     * @return the display name of the country.
     */
    public @NotNull String displayName(@Nullable Locale displayIn) {
        return locale.getDisplayCountry(displayIn == null ? Locale.getDefault() : displayIn);
    }

    @Override
    public String toString() {
        return locale.getCountry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Country) o;
        return Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(locale);
    }

    /**
     * Checks if the given locale is a valid country,
     *
     * @param locale the locale to check.
     * @return {@code true} if the locale is a valid country, {@code false} otherwise.
     */
    public static boolean isValid(@NotNull Locale locale) {
        var countryName = locale.getDisplayCountry();
        return !countryName.isBlank() && !countryName.equals(locale.getCountry());
    }

    /**
     * Creates a new {@code Country} from the given locale.
     *
     * @param locale the locale of the country to create.
     * @return the new {@code Country}.
     * @throws IllegalArgumentException if the given locale does not represent a valid country.
     */
    public static @NotNull Country ofLocale(@NotNull Locale locale) {
        if (!isValid(locale)) {
            throw new IllegalArgumentException("Locale does not represent a country");
        }
        return new Country(locale);
    }

    /**
     * Creates a new {@code Country} from the given ISO 3166 code.
     *
     * @param isoCode the ISO code of the country to create.
     * @return the new {@code Country}.
     * @throws IllegalArgumentException if the given ISO code is not valid.
     */
    @JsonCreator
    public static @NotNull Country ofIsoCode(@NotNull String isoCode) {
        return ofLocale(Locale.of("", isoCode));
    }

    /**
     * A list of all ISO countries provided by the current Java VM.
     */
    public static @NotNull List<Country> isoCountries() {
        return ISO_COUNTRIES;
    }
}
