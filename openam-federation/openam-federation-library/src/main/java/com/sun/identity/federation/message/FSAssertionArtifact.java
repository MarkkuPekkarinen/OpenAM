/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006 Sun Microsystems Inc. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://opensso.dev.java.net/public/CDDLv1.0.html or
 * opensso/legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at opensso/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * $Id: FSAssertionArtifact.java,v 1.3 2008/06/25 05:46:43 qcheng Exp $
 * Portions Copyrighted 2014 ForgeRock AS
 */

package com.sun.identity.federation.message;

import com.sun.identity.federation.common.IFSConstants;
import com.sun.identity.federation.message.common.FSMsgException;
import com.sun.identity.federation.common.FSUtils;

import com.sun.identity.saml.protocol.AssertionArtifact;
import com.sun.identity.saml.common.SAMLUtils;

import com.sun.identity.shared.encode.Base64;

/**
 * This class represents the <code>AssertionArtifact</code> element in the
 * <code>SAML</code> protocol schema. Current implementation supports
 * TYPE 1 artifact only. Other type of artifact can be supported by
 * extending this class.
 *
 * 
 * @deprecated since 12.0.0
 */
@Deprecated
public class FSAssertionArtifact extends AssertionArtifact {
    
    /**
     * Default Artifact length
     */
    public final static int ARTIFACT_1_LENGTH = 42;
    
    /**
     * Default Artifact Type Code 0 Constant
     */
    public final static byte ARTIFACT_1_TYPE_CODE_0 = 0;
    
    /**
     * Default Artifact Type Code 1 Constant
     */
    public final static byte ARTIFACT_1_TYPE_CODE_1 = 3;
    
    /**
     * Default Artifact Type Code Byte Array
     */
    public final static byte[] ARTIFACT_1_TYPE_CODE = {0, 3};
    
    /**
     * Default Constructor.
     */
    protected FSAssertionArtifact() {
    }
    
    /**
     * Constructor to create <code>AssertionArtifact</code> object.
     *
     * @param theArtifact is the string that is generated by a provider.
     * @throws SAMLException if there is an error decoding
     *         the artifact string , the length of the artifact string
     *         is incorrect , the <code>TYPE CODE</code> in the artifact
     *         or other errors which prevent creation of
     *         this object.
     */
    public FSAssertionArtifact(String theArtifact) throws FSMsgException {
        // check if the input is empty
        if ((theArtifact == null) || (theArtifact.length() == 0)) {
            FSUtils.debug.message("FSAssertionArtifact: empty input.");
            throw new FSMsgException("nullInput",null);
        }
        
        // decode the artifact
        byte raw[] = Base64.decode(theArtifact);
        if(raw == null) {
            if (FSUtils.debug.messageEnabled()) {
                FSUtils.debug.message("FSAssertionArtifact: decode error");
            }
            throw new FSMsgException("wrongInput",null);
        }
        
        // check if the length is 42bytes
        if (raw.length != ARTIFACT_1_LENGTH) {
            if (FSUtils.debug.messageEnabled()) {
                FSUtils.debug.message("FSAssertionArtifact: the length is"
                        + " not 42:" + raw.length);
            }
            throw new FSMsgException("wrongInput",null);
        }
        
        // check if the typecode is correct
        if ((raw[0] != ARTIFACT_1_TYPE_CODE_0) ||
                (raw[1] != ARTIFACT_1_TYPE_CODE_1)) {
            FSUtils.debug.message("FSAssertionArtifact: wrong typecode.");
            throw new FSMsgException("wrongInput", null);
        }
        typeCode = ARTIFACT_1_TYPE_CODE;
        
        artifact = theArtifact;
        
        // get the sourceID and assertionHandle
        byte sBytes[] = new byte[IFSConstants.ART_ID_LENGTH];
        byte aBytes[] = new byte[IFSConstants.ART_ID_LENGTH];
        System.arraycopy(raw, 2, sBytes, 0, IFSConstants.ART_ID_LENGTH);
        System.arraycopy(raw, 22, aBytes, 0, IFSConstants.ART_ID_LENGTH);
        
        sourceID = SAMLUtils.byteArrayToString(sBytes);
        assertionHandle = SAMLUtils.byteArrayToString(aBytes);
    }
    
    /**
     * Constructor to create <code>FSAssertionArtifact</code> object.
     *
     * @param idBytes the source identifier in the <code>Assertion</code>
     * @param handleBytes the assertion identifier
     * @throws SAMLException if wrong input or couldn't encode the artifact.
     */
    public FSAssertionArtifact(byte[] idBytes, byte[] handleBytes)
    throws FSMsgException {
        if ((idBytes == null) || (handleBytes == null)) {
            FSUtils.debug.message("FSAssertionArtifact: null input.");
            throw new FSMsgException("nullInput",null);
        }
        
        if ((idBytes.length != IFSConstants.ART_ID_LENGTH) ||
                (handleBytes.length != IFSConstants.ART_ID_LENGTH)) {
            FSUtils.debug.message("FSAssertionArtifact: wrong input length.");
            throw new FSMsgException("wrongInput",null);
        }
        sourceID = SAMLUtils.byteArrayToString(idBytes);
        assertionHandle = SAMLUtils.byteArrayToString(handleBytes);
        byte raw[] = new byte[42];
        raw[0] = ARTIFACT_1_TYPE_CODE_0;
        raw[1] = ARTIFACT_1_TYPE_CODE_1;
        for (int i = 0; i < IFSConstants.ART_ID_LENGTH; i++) {
            raw[2+i] = idBytes[i];
            raw[22+i] = handleBytes[i];
        }
        try {
            artifact = Base64.encode(raw).trim();
        } catch (Exception e) {
            if (FSUtils.debug.messageEnabled()) {
                FSUtils.debug.message("FSAssertionArtifact: exception encode"
                        + " input:", e);
            }
            throw new FSMsgException("errorCreateArtifact",null);
        }
        typeCode = ARTIFACT_1_TYPE_CODE;
    }
}
