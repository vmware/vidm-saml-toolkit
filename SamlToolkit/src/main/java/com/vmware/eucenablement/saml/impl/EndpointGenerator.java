/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.eucenablement.saml.impl;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointGenerator {
	private final static Logger logger = LoggerFactory.getLogger(EndpointGenerator.class);

    /**
     * Generate endpoint based on the input parameters.
     *
     * @param service 				Element name of service
     * @param location 				URI, usually a URL, for the location of the Endpoint
     * @param responseLocation		The URI responses should be sent to this for this Endpoint
     * @param binding				The URI identifier for the binding supported by the Endpoint
     */
	public static Endpoint generateEndpoint(QName service, String location, String responseLocation, String binding) {
		Endpoint samlEndpoint = SAMLUtil.buildSAMLObject(service);

		samlEndpoint.setLocation(location);
		if (binding != null) {
			samlEndpoint.setBinding(binding);
		}

		if (StringUtils.isNotEmpty(responseLocation))
			samlEndpoint.setResponseLocation(responseLocation);

		return samlEndpoint;
	}
}
