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
 * Copyright 2015-2016 ForgeRock AS.
 */

package org.forgerock.oauth2.core;

import java.util.Set;

/**
 * An OAuth 2.0 token abstraction for introspection.
 *
 * 
 */
public interface IntrospectableToken extends Token {

    /**
     * Gets the token's expiry time.
     *
     * @return The token's expiry time.
     */
    long getExpiryTime();

    /**
     * Gets whether the token has expired.
     *
     * @return Whether the token has expired.
     */
    boolean isExpired();

    /**
     * Gets the token's realm.
     *
     * @return The token's realm.
     */
    String getRealm();

    /**
     * Gets the token's client id.
     *
     * @return The token's client id.
     */
    String getClientId();

    /**
     * Gets the token's resource owner id.
     *
     * @return The token's resource owner id.
     */
    String getResourceOwnerId();

    /**
     * Gets the token's scopes.
     *
     * @return The token's scopes.
     */
    Set<String> getScope();

    /**
     * Gets the end user's authentication time in seconds.
     *
     * @return The authentication time.
     */
    long getAuthTimeSeconds();
}
