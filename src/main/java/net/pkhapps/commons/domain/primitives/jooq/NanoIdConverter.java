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
package net.pkhapps.commons.domain.primitives.jooq;

import net.pkhapps.commons.domain.primitives.NanoId;
import org.jooq.impl.AbstractConverter;

/**
 * JOOQ converter for converting between string and {@link NanoId}.
 */
public class NanoIdConverter extends AbstractConverter<String, NanoId> {

    public NanoIdConverter() {
        super(String.class, NanoId.class);
    }

    @Override
    public NanoId from(String databaseObject) {
        return databaseObject == null ? null : NanoId.valueOf(databaseObject);
    }

    @Override
    public String to(NanoId userObject) {
        return userObject == null ? null : userObject.toString();
    }
}
