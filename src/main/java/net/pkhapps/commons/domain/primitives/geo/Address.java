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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;
import net.pkhapps.commons.domain.primitives.geo.usa.USPostalAddress;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to be implemented by all addresses (postal or physical).
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "format",
        requireTypeIdForSubtypes = OptBoolean.FALSE
)
@JsonSubTypes({
        @JsonSubTypes.Type(GenericAddress.class),
        @JsonSubTypes.Type(USPostalAddress.class)
}) // TODO Make it possible to add custom sub types without having to mention them here
public interface Address {

    /**
     * Gets the country of the address.
     *
     * @return the country.
     */
    @NotNull Country country();

    /**
     * Converts this address into a "generic" address that should work in most (if not all) countries.
     *
     * @return a generic version of this address.
     */
    @NotNull GenericAddress toGenericAddress();
}
