/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.eucenablement.saml.impl;

import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.NameIDPolicy;
import org.opensaml.saml.saml2.core.NameIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAuthnRequestBuilder {
	private static Logger log = LoggerFactory.getLogger(SimpleAuthnRequestBuilder.class);


	/**
	 * generate simple saml sso request
	 *
	 * @param assertionConsumerServiceURL   consumer URL to handle the SSO response
	 * @param ssoTargetUrl					SSO service URL on IDP
	 * @param issuerName					issuer of this application
	 * @param binding						binding type to communicate with IDP for SSO feature
	 */
	public static AuthnRequest create(String assertionConsumerServiceURL, String ssoTargetUrl, String issuerName,
			String binding) {

		AuthnRequest request = (AuthnRequest) SAMLUtil.buildSAMLObject(AuthnRequest.DEFAULT_ELEMENT_NAME);

		request.setAssertionConsumerServiceURL(assertionConsumerServiceURL);
		request.setID(generateID());
		request.setIssueInstant(getCurrentTime());
		request.setDestination(ssoTargetUrl);
		request.setIssuer(SAMLUtil.generateIssuer(issuerName));

		// protocol binding
		request.setProtocolBinding(binding);
		request.setNameIDPolicy(buildNameIdPolicy());
		return request;
	}

	private static String generateID() {
		return SAMLUtil.generateSamlId();
	}

	/**
	 * get current date time
	 */
	private static DateTime getCurrentTime() {
		return new DateTime();
	}


	/**
	 * build name id policy
	 */
	private static NameIDPolicy buildNameIdPolicy() {
		NameIDPolicy nameIDPolicy = SAMLUtil.buildSAMLObject(NameIDPolicy.DEFAULT_ELEMENT_NAME);
		nameIDPolicy.setAllowCreate(true);
		nameIDPolicy.setFormat(NameIDType.TRANSIENT);

		return nameIDPolicy;
	}
}
