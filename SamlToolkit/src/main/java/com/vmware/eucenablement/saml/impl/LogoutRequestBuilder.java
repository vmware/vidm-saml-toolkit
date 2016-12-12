/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.eucenablement.saml.impl;

import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.SessionIndex;

/**
 * Build SLO request to communicate with VIDM
 *
 *
 */
public class LogoutRequestBuilder {

	 /**
     * Generate SLO request
     *
     * @param	nameIdValue		user name
     * @param	sessionIndex	Session index of SSO
     * @param	logoutURL		the location of SLO
     * @param	issuerName		request sender
     * @return 	SAML object; This function will return null if cannot build SAML object
     */
	public static LogoutRequest genererateLogoutRequest(String nameIdValue, String sessionIndex, String logoutURL,
			String issuerName) {
		LogoutRequest logoutRequest = (LogoutRequest) SAMLUtil.buildSAMLObject(LogoutRequest.DEFAULT_ELEMENT_NAME);

		logoutRequest.setID(SAMLUtil.generateSamlId());
		logoutRequest.setDestination(logoutURL);

		logoutRequest.setIssueInstant(new DateTime());
		logoutRequest.setIssuer(SAMLUtil.generateIssuer(issuerName));
		SessionIndex sessionIndexElement = (SessionIndex) SAMLUtil.buildSAMLObject(SessionIndex.DEFAULT_ELEMENT_NAME);
		sessionIndexElement.setSessionIndex(sessionIndex);
		logoutRequest.getSessionIndexes().add(sessionIndexElement);

		NameID nameId = (NameID) SAMLUtil.buildSAMLObject(NameID.DEFAULT_ELEMENT_NAME);
		nameId.setValue(nameIdValue);
		logoutRequest.setNameID(nameId);

		return logoutRequest;
	}
}
