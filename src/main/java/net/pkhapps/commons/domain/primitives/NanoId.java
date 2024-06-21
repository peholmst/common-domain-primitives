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
import org.jetbrains.annotations.NotNull;

/**
 * Domain primitive representing a NanoID.
 *
 * @see <a href="https://github.com/aventrix/jnanoid">JNanoID</a>.
 */
public final class NanoId extends AbstractNanoId {

    private NanoId(@NotNull String value) {
        super(value);
    }

    /**
     * Creates a new {@code NanoId} from the given string.
     *
     * @param value the NanoID to create.
     * @return the new {@code NanoId}.
     * @throws IllegalArgumentException if the given string is not a valid NanoID.
     */
    @JsonCreator
    public static @NotNull NanoId fromString(@NotNull String value) {
        return new NanoId(validate(value));
    }

    /**
     * Generates a new random {@code NanoId}.
     *
     * @return the new {@code NanoId}.
     */
    public static @NotNull NanoId randomNanoId() {
        return new NanoId(randomNanoIdString());
    }
}
