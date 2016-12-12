/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package org.opensaml.messaging.decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.binding.SAMLBindingSupport;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import net.shibboleth.utilities.java.support.codec.Base64Support;
import net.shibboleth.utilities.java.support.xml.ParserPool;
import net.shibboleth.utilities.java.support.xml.XMLParserException;

/**
*
* A class that implements AbstractMessageDecoder and just to remove the dependency for httpservlet
*/
public class SimpleSAMLMessageDecoder extends AbstractMessageDecoder {

	private ParserPool parserPool;

	public SimpleSAMLMessageDecoder() {
		parserPool = XMLObjectProviderRegistrySupport.getParserPool();
	}

	private static Logger log = LoggerFactory.getLogger(SimpleSAMLMessageDecoder.class);
	private String samlresponse;

	public void setSAMLResponse(String response) {
		this.samlresponse = response;
	}

	@Override
	protected void doDecode() throws MessageDecodingException {

		MessageContext<SAMLObject> messageContext = new MessageContext<>();

		// TODO: shall we handle relay here?

		InputStream base64DecodedMessage = getBase64DecodedMessage(samlresponse);
		SAMLObject inboundMessage = (SAMLObject) unmarshallMessage(base64DecodedMessage);
		messageContext.setMessage(inboundMessage);
		log.info("Decoded SAML message");

		populateBindingContext(messageContext);

		setMessageContext(messageContext);

	}

	/**
	 * Gets the Base64 encoded message from the request and decodes it.
	 *
	 * @param encodedMessage
	 *            the inbound saml response/request from the http
	 *            request/response which is encoded
	 *
	 * @return decoded message
	 *
	 * @throws MessageDecodingException
	 *             thrown if the message does not contain a base64 encoded SAML
	 *             message
	 */
	private InputStream getBase64DecodedMessage(String encodedMessage) throws MessageDecodingException {
		log.info("Getting Base64 encoded message from ");

		if (Strings.isNullOrEmpty(encodedMessage)) {
			log.error(" Invalid request/response for SAML 2 HTTP POST binding, null message.");
			throw new MessageDecodingException("No SAML message present in request/response");
		}

		log.info("Base64 decoding SAML message:\n{}", encodedMessage);
		byte[] decodedBytes = Base64Support.decode(encodedMessage);
		if (decodedBytes == null) {
			log.error("Unable to Base64 decode SAML message");
			throw new MessageDecodingException("Unable to Base64 decode SAML message");
		}

		log.info("Decoded SAML message:\n{}", new String(decodedBytes));
		return new ByteArrayInputStream(decodedBytes);
	}

	private XMLObject unmarshallMessage(InputStream messageStream) throws MessageDecodingException {
		try {
			XMLObject message = XMLObjectSupport.unmarshallFromInputStream(this.parserPool, messageStream);
			return message;
		} catch (XMLParserException e) {
			log.error("Error unmarshalling message from input stream", e);
			throw new MessageDecodingException("Error unmarshalling message from input stream", e);
		} catch (UnmarshallingException e) {
			log.error("Error unmarshalling message from input stream", e);
			throw new MessageDecodingException("Error unmarshalling message from input stream", e);
		}
	}

	/**
	 * Populate the context which carries information specific to this binding.
	 *
	 * @param messageContext
	 *            the current message context
	 */
	private void populateBindingContext(MessageContext<SAMLObject> messageContext) {
		SAMLBindingContext bindingContext = messageContext.getSubcontext(SAMLBindingContext.class, true);
		// TODO: support SAML2 POST only for now, may need support others
		bindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);
		bindingContext.setHasBindingSignature(false);
		bindingContext.setIntendedDestinationEndpointURIRequired(SAMLBindingSupport.isMessageSigned(messageContext));
	}
}
