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
package net.pkhapps.commons.domain.primitives.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.pkhapps.commons.domain.primitives.DomainName;

/**
 * JPA attribute converter for converting between string and {@link DomainName}.
 */
@Converter
public class DomainNameAttributeConverter implements AttributeConverter<DomainName, String> {

    @Override
    public String convertToDatabaseColumn(DomainName attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public DomainName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DomainName.valueOf(dbData);
    }
}