/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package com.vmware.eucenablement.saml.service;

import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.params.AllClientPNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.metadata.resolver.impl.HTTPMetadataResolver;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml.saml2.metadata.SingleLogoutService;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.security.credential.UsageType;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.saml.api.IdpMainMetadata;
import com.vmware.eucenablement.saml.impl.IdpMainMetadataImpl;
import com.vmware.eucenablement.saml.impl.SAMLUtil;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;

public class IdpDiscoveryService {
	private static Logger log = LoggerFactory.getLogger(IdpDiscoveryService.class);
	private static final String IDP_URI_SUFFIX = "/SAAS/API/1.0/GET/metadata/idp.xml";
	private EntityDescriptor idpEntityDescriptor = null;
	private String idpURL = null;

	public IdpDiscoveryService(String idpURL, boolean bypassSSLCertValidation)
			throws InitializationException, ResolverException, ComponentInitializationException {
		if (idpURL == null) {
			throw new InitializationException("IDP URL can't be null");
		}
		idpURL = idpURL.trim();
		if (idpURL.endsWith("idp.xml")) {
			this.idpURL = idpURL;
		} else {
			log.warn("Append SUFFIX to IDP URL");
			this.idpURL = idpURL + IDP_URI_SUFFIX;
		}
		log.info("IDPURL:" + this.idpURL);
		InitializationService.initialize();
		this.loadMetaData(bypassSSLCertValidation);
	}

	private boolean loadMetaData(boolean bypassSSLCertValidation)
			throws ResolverException, ComponentInitializationException {
		String metadataUri = this.idpURL;

		String entityID = metadataUri;
		String componetID = "VidmComponet";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			// accept un-trusted ssl certificate
			X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			X509TrustManager tm = new X509TrustManager() {

				// @Override
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}

				// @Override
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}

				// @Override
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

			};

			if (bypassSSLCertValidation) {
				log.debug("bypass cert validation");
				try {
					SSLContext ctx;
					ctx = SSLContext.getInstance("TLS");
					ctx.init(null, new TrustManager[] { tm }, null);
					SSLSocketFactory sslSocketFactory = new SSLSocketFactory(ctx);
					sslSocketFactory.setHostnameVerifier(hostnameVerifier);
					Scheme https = new Scheme("https", sslSocketFactory, 443);
					httpClient.getConnectionManager().getSchemeRegistry().register(https);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("Init SSL property failed: " + e.toString());
				}
			}

			httpClient.getParams().setIntParameter(AllClientPNames.CONNECTION_TIMEOUT, 1000 * 5);
			HTTPMetadataResolver provider = new HTTPMetadataResolver(httpClient, metadataUri);
			BasicParserPool pool = new BasicParserPool();
			pool.initialize();
			provider.setParserPool(pool);
			provider.setId(componetID);
			CriteriaSet criteriaSet = null;

			provider.initialize();
			criteriaSet = new CriteriaSet(new EntityIdCriterion(entityID));

			this.idpEntityDescriptor = provider.resolveSingle(criteriaSet);
		} catch (ResolverException e) {
			log.error("Cannot resolve single to get entity descriptor", e);
			return false;
		} catch (ComponentInitializationException e) {
			log.error("Caught ComponentInitializationException", e);
			return false;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

		return true;
	}

	public IdpMainMetadata getMainMetadata() {
		if (idpEntityDescriptor == null) {
			log.warn("Idp entity descriptor is null.");
			return null;
		}

		IDPSSODescriptor idpsso = idpEntityDescriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS);
		if (idpsso == null) {
			log.warn("Cannot get IDP SSO Descriptor");
			return null;
		}

		IdpMainMetadataImpl metadata = new IdpMainMetadataImpl();
		for (KeyDescriptor key : idpsso.getKeyDescriptors()) {
			X509Data x509 = key.getKeyInfo().getX509Datas().get(0);
			if (key.getUse() == UsageType.SIGNING) {
				X509Certificate x509Cert = x509.getX509Certificates().get(0);
				String certValue = SAMLUtil.convertCertToPemFormat(x509Cert.getValue());
				metadata.setSigningKey(certValue);
			} else if (key.getUse() == UsageType.ENCRYPTION) {
				X509Certificate x509Cert = x509.getX509Certificates().get(0);
				String certValue = SAMLUtil.convertCertToPemFormat(x509Cert.getValue());
				metadata.setEncrpytionKey(certValue);
			}
		}

		List<SingleSignOnService> serviceList = idpsso.getSingleSignOnServices();
		for (SingleSignOnService srv : serviceList) {
			metadata.addSsoMetadata(srv.getBinding(), srv.getLocation());
		}

		List<SingleLogoutService> logoutList = idpsso.getSingleLogoutServices();
		for (SingleLogoutService srv : logoutList) {
			metadata.addLogoutMetadata(srv.getBinding(), srv.getLocation(), srv.getResponseLocation());
		}

		metadata.setIdpUri(this.idpURL);

		return metadata;
	}

}
