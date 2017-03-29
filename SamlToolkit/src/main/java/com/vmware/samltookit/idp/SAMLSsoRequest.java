package com.vmware.samltookit.idp;

public interface SAMLSsoRequest {
	public boolean isValid();

	//consumer id in SAML, may be URL or hostname
	public String getConsumer();

	public String getIssuer();

	public String getNameidpolicy();

	public String getID();
}
