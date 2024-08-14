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
package net.pkhapps.commons.domain.primitives.geo.jooq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.commons.domain.primitives.geo.Address;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSONB;
import org.jooq.exception.DataTypeException;

/**
 * JOOQ converter for converting between {@link JSONB} and {@link Address} using Jackson.
 */
public class AddressJsonConverter implements Converter<JSONB, Address> {

    private final ObjectMapper objectMapper;

    public AddressJsonConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Override
    public Address from(JSONB databaseObject) {
        try {
            return databaseObject == null ? null : objectMapper.readValue(databaseObject.data(), Address.class);
        } catch (JsonProcessingException ex) {
            throw new DataTypeException("Could not deserialize from JSON", ex);
        }
    }

    @Override
    public JSONB to(Address userObject) {
        try {
            return userObject == null ? null : JSONB.jsonb(objectMapper.writeValueAsString(userObject));
        } catch (JsonProcessingException ex) {
            throw new DataTypeException("Could not serialize to JSON", ex);
        }
    }

    @Override
    public @NotNull Class<JSONB> fromType() {
        return JSONB.class;
    }

    @Override
    public @NotNull Class<Address> toType() {
        return Address.class;
    }
}
