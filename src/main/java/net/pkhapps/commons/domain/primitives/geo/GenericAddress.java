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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a generic postal address that should work with the formats of most (if not all)
 * countries.
 *
 * @param line1   the first address line, typically a street name and number (optional).
 * @param line2   the second address line, typically an apartment or post box number (optional).
 * @param line3   the third address line, typically city, postal code, state or province (optional).
 * @param country the country of the address (required).
 */
public record GenericAddress(@Nullable String line1, @Nullable String line2, @Nullable String line3,
                             @NotNull Country country) implements Address {

    public GenericAddress {
        requireNonNull(country, "country must not be null");
    }

    @Override
    public @NotNull GenericAddress toGenericAddress() {
        return this;
    }
}
