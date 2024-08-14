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

/**
 * Enumeration of US states and territories.
 */
public enum USStateAndTerritory {
    AL("Alabama", true),
    AK("Alaska", true),
    AZ("Arizona", true),
    AR("Arkansas", true),
    CA("California", true),
    CO("Colorado", true),
    CT("Connecticut", true),
    DE("Delaware", true),
    FL("Florida", true),
    GA("Georgia", true),
    HI("Hawaii", true),
    ID("Idaho", true),
    IL("Illinois", true),
    IN("Indiana", true),
    IA("Iowa", true),
    KS("Kansas", true),
    KY("Kentucky", true),
    LA("Louisiana", true),
    ME("Maine", true),
    MD("Maryland", true),
    MA("Massachusetts", true),
    MI("Michigan", true),
    MN("Minnesota", true),
    MS("Mississippi", true),
    MO("Missouri", true),
    MT("Montana", true),
    NE("Nebraska", true),
    NV("Nevada", true),
    NH("New Hampshire", true),
    NJ("New Jersey", true),
    NM("New Mexico", true),
    NY("New York", true),
    NC("North Carolina", true),
    ND("North Dakota", true),
    OH("Ohio", true),
    OK("Oklahoma", true),
    OR("Oregon", true),
    PA("Pennsylvania", true),
    RI("Rhode Island", true),
    SC("South Carolina", true),
    SD("South Dakota", true),
    TN("Tennessee", true),
    TX("Texas", true),
    UT("Utah", true),
    VT("Vermont", true),
    VA("Virginia", true),
    WA("Washington", true),
    WV("West Virginia", true),
    WI("Wisconsin", true),
    WY("Wyoming", true),
    DC("District of Columbia", false),
    PR("Puerto Rico", false),
    GU("Guam", false),
    VI("U.S. Virgin Islands", false),
    AS("American Samoa", false),
    MP("Northern Mariana Islands", false),
    FM("Federated States of Micronesia", false),
    MH("Marshall Islands", false),
    PW("Palau", false);

    private final String displayName;
    private final boolean isState;

    USStateAndTerritory(@NotNull String displayName, boolean isState) {
        this.displayName = displayName;
        this.isState = isState;
    }

    /**
     * Returns whether this is a state or a territory
     *
     * @return {@code true} if this enum constant represents a US state, {@code false} if it is a territory or Washington D.C.
     */
    public boolean isState() {
        return isState;
    }

    /**
     * Gets the display name of the state or territory.
     *
     * @return the display name of the state or territory.
     */
    public @NotNull String displayName() {
        return displayName;
    }
}
