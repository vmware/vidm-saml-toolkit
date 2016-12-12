/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.eucenablement.saml.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBuilder;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class SAMLUtil {
	private static Logger log = LoggerFactory.getLogger(SAMLUtil.class);

	 /**
     * Generate the ID for SAML message.
     *
     * @return ID for SAML message
     */
	public static String generateSamlId() {
		return UUID.randomUUID().toString();
	}

	 /**
     * Generate SAML object according to the element name.
     *
     * @param	name	element name
     * @return 	SAML object; This function will return null if cannot build SAML object
     */
	public static <T extends XMLObject> T buildSAMLObject(@Nonnull final QName name) {
		T object = null;
		if(name == null) {
			return null;
		}

		try {
			XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
			XMLObjectBuilder<T> builder = (XMLObjectBuilder<T>) builderFactory.getBuilder(name);
			if (builder != null) {
				object = builder.buildObject(name);
			}
		} catch (Exception e) {
			log.error("Build SAML Object failed", e);
			object = null;
		}

		return object;
	}

	 /**
     * Transfer XMLObject to string content.
     *
     * @param	object	XMLObject
     * @return 	The content of XMLObject
     */
	public static String transferSAMLObject2String(final XMLObject object) {
		Element element = null;
		String xmlString = null;

		if (object instanceof SignableSAMLObject && ((SignableSAMLObject) object).isSigned()
				&& object.getDOM() != null) {
			element = object.getDOM();
		} else {
			try {
				Marshaller out = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(object);
				out.marshall(object);
				element = object.getDOM();

			} catch (MarshallingException e) {
				log.error("Caught MarshallingException", e);
				// logger.error(e.getMessage(), e);
			}
		}

		if (element == null) {
			return null;
		}

		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(element);

			transformer.transform(source, result);
			xmlString = result.getWriter().toString();
		} catch (TransformerConfigurationException e) {
			log.error("Caught TransformerConfigurationException", e);
		} catch (TransformerException e) {
			log.error("Caught TransformerException", e);
		}
		return xmlString;
	}

	 /**
     * Generate SAML object according to the class name of SAML Object.
     *
     * @param	clazz	class name of SAML Object
     * @return 	SAML object; This function will return null if cannot build SAML object
     */
	public static <T> T buildSAMLObject(final Class<T> clazz) {
		T object = null;
		try {
			XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
			QName defaultElementName = (QName) clazz.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);
			object = (T) builderFactory.getBuilder(defaultElementName).buildObject(defaultElementName);
		} catch (IllegalAccessException e) {
			log.error("Cannot build SAMLObject because IllegalAccessException generated", e);
		} catch (NoSuchFieldException e) {
			log.error("Cannot build SAMLObject because NoSuchFieldException generated", e);
		}

		return object;
	}

	 /**
     * Decode base64 message.
     *
     * @param	base64Content base64 message content
     * @return 	SAML object; This function will return null if cannot build SAML object
     */
	public static String decodeFromBase64(String base64Content) throws Exception {
		Base64 base64 = new Base64();
		byte[] decodedB = base64.decode(base64Content);
		String decodedResponse = new String(decodedB);

		return decodedResponse;
	}


	 /**
     * convert key to pem format
     *
     */
	public static String convertKeyToPemFormat(String key) {
		return formatPEMString(VidmSamlConstants.BEGIN_PRIVATE_FULL, VidmSamlConstants.END_PRIVATE_FULL, key);
	}

	 /**
     * convert key to pem format
     *
     */
	public static String convertCertToPemFormat(String cert) {
		return formatPEMString(VidmSamlConstants.BEGIN_CERT_FULL, VidmSamlConstants.END_CERT_FULL, cert);
	}

	private static String formatPEMString(final String head, final String foot, final String indata) {
		StringBuilder pem = new StringBuilder(head);
		pem.append("\n");

		String data;
		if (indata != null) {
			data = indata.replaceAll("\\s+", "");
		} else {
			data = "";
		}
		int lineLength = 64;
		int dataLen = data.length();
		int si = 0;
		int ei = lineLength;

		while (si < dataLen) {
			if (ei > dataLen) {
				ei = dataLen;
			}

			pem.append(data.substring(si, ei));
			pem.append("\n");
			si = ei;
			ei += lineLength;
		}

		pem.append(foot);

		return pem.toString();
	}


	 /**
     * transfer certificate content to X509Certificate object
     *
     * @param	cert  content of certificate
     * @return 	X509Certificate object
     */
	public static X509Certificate transfer2X509Certificate(String cert) {

		// return null if no certificate content
		if (null == cert) {
			return null;
		}
		/*
		 * Wrong key for test cert = "-----BEGIN CERTIFICATE-----\n" +
		 * "MIID7DCCAtSgAwIBAgIFFHYYEzIwDQYJKoZIhvcNAQELBQAwgawxCzAJBgNVBAYT\n"
		 * +
		 * "AlVTMRMwEQYDVQQIEwpjYWxpZm9ybmlhMRIwEAYDVQQHEwlQYWxvIEFsdG8xDzAN\n"
		 * +
		 * "BgNVBAoTBlZNd2FyZTEaMBgGA1UECxMRSG9yaXpvbi1Xb3Jrc3BhY2UxJDAiBgNV\n"
		 * +
		 * "BAMTG0ludGVybmFsIFJvb3QgQ0EgMTQ3NjE4MTMzMTEhMB8GCSqGSIb3DQEJARYS\n"
		 * +
		 * "dW5rbm93bkB2bXdhcmUuY29tMB4XDTE1MTAxMjEwMjIxMloXDTQ0MDIyNjEwMjIx\n"
		 * +
		 * "MlowgaUxCzAJBgNVBAYTAlVTMRMwEQYDVQQIDApjYWxpZm9ybmlhMRIwEAYDVQQH\n"
		 * +
		 * "DAlQYWxvIEFsdG8xDzANBgNVBAoMBlZNd2FyZTEaMBgGA1UECwwRSG9yaXpvbi1X\n"
		 * +
		 * "b3Jrc3BhY2UxHTAbBgNVBAMMFHZpZG0uc3Rlbmdkb21haW4uZnZ0MSEwHwYJKoZI\n"
		 * +
		 * "hvcNAQkBFhJ1bmtub3duQHZtd2FyZS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IB\n"
		 * +
		 * "DwAwggEKAoIBAQDyj9UYrlYv/JlsbYlSRTQ/18HIgjWuNcE/V22xs2ZlQZpUXEPK\n"
		 * +
		 * "OIiaklqUh0yrIuq1TsszBgQxso774UyM2lAKI00+WM8A0n2JxBxJx68Md99vruG6\n"
		 * +
		 * "2mcjgrc0jEGVQoAQTzt0aXtNOmisjTPKxlYNegX/YuYMh78WVbv5zgDAuzzZX4P3\n"
		 * +
		 * "LWXwW/aqPQkVZMFvFaeiltEmG2iBdxM9s8ey9utq8dx/6L0mFhatBECPkkeqTZ5N\n"
		 * +
		 * "1gwqsXOtkvUSiA0a6olKak1e4dJ4YeqKDljkp5+WMIZbwJWxWLnvzgzkqgX9Aovv\n"
		 * +
		 * "v9eP1djB6dvpvEeUMqVVY2EF866HFDr2Vbi1AgMBAAGjGjAYMAkGA1UdEwQCMAAw\n"
		 * +
		 * "CwYDVR0PBAQDAgXgMA0GCSqGSIb3DQEBCwUAA4IBAQAy3XSX5ODMwtVBCQDwz7eY\n"
		 * +
		 * "zGYWIweNazR54JSI5mGlyx+iynL3E/tCC1idZMq6Xz331nFHVXK2j5FsNzOpUEJK\n"
		 * +
		 * "OqsdtovirrGgJ+RqxieGP+vIfAgFhAG+VjhH6UvvLftSvbTp5Kum8KSI8H5k6H4F\n"
		 * +
		 * "AQjuqiqao5nji7IO+PLFJ9tEIybGK99riKJOKcUnr+px9fHxrphYOgCSw5o0zl8G\n"
		 * +
		 * "dHdO8dmM0WLLKxbm11rzxSmn3lS6dUF6+SVrVUJd4Ln0Ujkt/mxUPdzKRbIi/aVh\n"
		 * +
		 * "qNHgsx8lHUoenasijd4sJPnj3YKz2Q9lHjSIOgMK41PSgVymOY2W7y2ANoNNKR0Q\n"
		 * + "-----END CERTIFICATE-----";
		 */
		if (!cert.contains(VidmSamlConstants.BEGIN_CERT)) {
			cert = convertCertToPemFormat(cert);
		}

		InputStream certinputstream = new ByteArrayInputStream(cert.getBytes());
		CertificateFactory cf = null;
		X509Certificate x509 = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			x509 = (X509Certificate) cf.generateCertificate(certinputstream);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			log.error("Caught CertificateException", e);
			return null;
		}

		return x509;
	}

	 /**
     * generate issuer
     *
     * @param	issuerName  name of issuer
     * @return 	issuer with this name
     */
	public static Issuer generateIssuer(String issuerName) {
		Issuer issuer = SAMLUtil.buildSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);

		issuer.setValue(issuerName);
		issuer.setFormat(NameIDType.ENTITY);

		return issuer;
	}
}
