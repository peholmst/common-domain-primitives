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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive representing a US street address, consisting of a number (optional) and name (required).
 *
 * @param number the street number (optional).
 * @param name   the street name (required).
 */
public record StreetAddress(
        @Nullable StreetNumber number,
        @NotNull StreetName name) {

    public StreetAddress {
        requireNonNull(name, "name must not be null");
    }
}
