package com.vmware.samltookit.idp;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.vmware.eucenablement.saml.service.AbstractSAMLService;

public class SAMLIDPConf {
	private String _issuer ;
	public String getIssuer(){
		return this._issuer;
	}



	/**
	 *
	 * @param issuer like https://localhost:8443/SamlSample/idp.xml
	 * @param keystorepath
	 * @param keystorepwd
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 */
	public SAMLIDPConf(String issuer, InputStream keystoreStream, String keystorepwd) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		this._issuer = issuer;


		 KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

		    // get user password and file input stream
		    char[] password = keystorepwd.toCharArray();

		    try {

		        ks.load(keystoreStream, password);
		    } finally {
		        if (keystoreStream != null) {
		        	keystoreStream.close();
		        }
		    }
		    AbstractSAMLService.setPrivateCredential( ks, ks.aliases().nextElement(), keystorepwd);
	}

}
