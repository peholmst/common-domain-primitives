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

import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a country. Countries can be either ISO-codes or user-provided.
 */
public abstract sealed class Country implements Serializable {

    private static final List<IsoCountry> ISO_COUNTRIES;

    static {
        ISO_COUNTRIES = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2).stream().map(Country::ofIsoCode).toList();
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
    public abstract @NotNull String displayName(@Nullable Locale displayIn);

    /**
     * Subclass of {@link Country} for countries that are identified by a Java {@link Locale}.
     */
    public static final class IsoCountry extends Country {

        private final Locale locale;

        private IsoCountry(@NotNull Locale locale) {
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

        @Override
        @JsonValue
        public String toString() {
            return locale.getCountry();
        }

        @Override
        public @NotNull String displayName(@Nullable Locale displayIn) {
            return locale.getDisplayCountry(displayIn == null ? Locale.getDefault() : displayIn);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IsoCountry that = (IsoCountry) o;
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
    }

    /**
     * Subclass of {@link Country} for countries whose names have been specified by the user.
     */
    public static final class UserNamedCountry extends Country {

        public static final int MAX_LENGTH = 100;

        private final String countryName;

        private UserNamedCountry(@NotNull String countryName) {
            this.countryName = requireNonNull(countryName);
        }

        @Override
        public @NotNull String displayName(@Nullable Locale displayIn) {
            // Ignore displayIn
            return countryName;
        }

        @Override
        @JsonValue
        public String toString() {
            return countryName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserNamedCountry that = (UserNamedCountry) o;
            return Objects.equals(countryName, that.countryName);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(countryName);
        }

        /**
         * Checks if the given string is a syntactically valid country name (without checking whether the country actually exists).
         *
         * @param countryName the string to check.
         * @return {@code true} if the string is a valid country name, {@code false} otherwise.
         */
        public static boolean isValid(@NotNull String countryName) {
            if (countryName.isBlank() || countryName.length() > MAX_LENGTH) {
                return false;
            }
            return countryName.codePoints().allMatch(codePoint ->
                    Character.isAlphabetic(codePoint)
                            || Character.isWhitespace(codePoint)
                            || codePoint == ','
                            || codePoint == '\''
                            || codePoint == '('
                            || codePoint == ')'
                            || codePoint == '-'
            );
        }
    }

    /**
     * Creates a new {@link IsoCountry} from the given locale.
     *
     * @param locale the locale of the country to create.
     * @return the new {@link IsoCountry}.
     * @throws IllegalArgumentException if the given locale does not represent a valid country.
     */
    public static @NotNull IsoCountry ofLocale(@NotNull Locale locale) {
        if (!IsoCountry.isValid(locale)) {
            throw new IllegalArgumentException("Locale does not represent a country");
        }
        return new IsoCountry(locale);
    }

    /**
     * Creates a new {@link IsoCountry} from the given ISO 3166 code.
     *
     * @param isoCode the ISO code of the country to create.
     * @return the new {@link IsoCountry}.
     * @throws IllegalArgumentException if the given ISO code is not valid.
     */
    public static @NotNull IsoCountry ofIsoCode(@NotNull String isoCode) {
        return ofLocale(Locale.of("", isoCode));
    }

    /**
     * Creates a new {@link UserNamedCountry} with the given name.
     *
     * @param name the name of the country to create.
     * @return the new {@link UserNamedCountry}.
     * @throws IllegalArgumentException if the given country name is blank, too long or contains illegal characters.
     */
    public static @NotNull UserNamedCountry ofName(@NotNull String name) {
        var strippedName = name.strip();
        if (!UserNamedCountry.isValid(strippedName)) {
            throw new IllegalArgumentException("Invalid country name");
        }
        return new UserNamedCountry(strippedName);
    }

    /**
     * Creates a new {@code Country} from the given string. If the value represents a valid
     * ISO 3166 code, a new {@link IsoCountry} is created. Otherwise, a new {@link UserNamedCountry} is created.
     *
     * @param value the ISO code or name of the country to create.
     * @return the new {@code Country}.
     * @throws IllegalArgumentException if the given value is blank, too long or contains illegal characters.
     */
    public static @NotNull Country valueOf(@NotNull String value) {
        var stripped = value.strip();
        var locale = Locale.of("", stripped);
        if (IsoCountry.isValid(locale)) {
            return new IsoCountry(locale);
        } else {
            return ofName(stripped);
        }
    }

    /**
     * A list of all ISO countries provided by the current Java VM.
     */
    public static @NotNull List<IsoCountry> isoCountries() {
        return ISO_COUNTRIES;
    }
}
