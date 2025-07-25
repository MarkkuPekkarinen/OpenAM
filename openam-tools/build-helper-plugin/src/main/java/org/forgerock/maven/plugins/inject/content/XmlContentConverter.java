/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2014 ForgeRock AS.
 * Portions Copyrighted 2023-2025 3A Systems, LLC.
 */

package org.forgerock.maven.plugins.inject.content;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * An implementation of a {@code ContentConverter} which will escape xml special characters from the line.
 *
 * @since 12.0.0
 */
public class XmlContentConverter implements ContentConverter {

    /**
     * Escapes xml special characters.
     *
     * @param line {@inheritDoc}
     * @return {@inheritDoc}
     */
    public String convert(String line) {
        return StringEscapeUtils.escapeXml(line);
    }
}
