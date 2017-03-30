package com.vmware.samltookit.idp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.saml.service.SAMLIDPService;

/**
*
* This is the identity provider service for vIDM.
* vIDM plays the role of service provider, and this IDPService should be created and managed by some identity provider
* Step 1, This IDPService decodes SAML request (containing service info) from vIDM,
* Step 2, The identity provider handles authentication as is
* Step 3, This IDPService generates SAML response (with user info) to vIDM
*
*/
public class IDPService {

	private final SAMLIDPConf config;

	private SAMLIDPService _service;

	public SAMLIDPConf getSAMLToolkitConf() {
		return config;
	}

	/**
	 *
	 * @param conf
	 * @throws Exception
	 */
	public IDPService(SAMLIDPConf conf) throws Exception {

		this.config = conf;
		this._service = new SAMLIDPService(conf);

	}

	/**
	 *
	 * @param samlrequest from the http request parameter like ?SAMLRequest=
	 * @return
	 * @throws Exception
	 */
	public SAMLSsoRequest decodeSAMLRequest(String samlrequest) throws Exception {

			return this._service.decodeSAMLRequest(samlrequest);
	}


	public SAMLSsoRequest decodeSAMLRequestWithRelay(String samlrequest, String relay) throws Exception {

		return this._service.decodeSAMLRequest(samlrequest);
}



	/**
	 *
	 * @param request
	 * @param userID
	 * @return
	 */
	public String getSSOResponseByPostBinding(SAMLSsoRequest request, String userID)  {
		return this._service.getSSOResponseByPostBinding(request, userID);
	}





	private static Logger log = LoggerFactory.getLogger(IDPService.class);



}
