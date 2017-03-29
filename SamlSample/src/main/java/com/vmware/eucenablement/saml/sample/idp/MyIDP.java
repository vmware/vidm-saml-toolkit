package com.vmware.eucenablement.saml.sample.idp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.saml.sample.MySSO;
import com.vmware.samltookit.idp.IDPService;
import com.vmware.samltookit.idp.SAMLIDPConf;

public class MyIDP {

	private static IDPService service;

	private static Logger log = LoggerFactory.getLogger(MySSO.class);

	public static IDPService initIDPService() {


		try {
			SAMLIDPConf conf = new SAMLIDPConf();


			service = new IDPService(conf);
		} catch (Exception e) {
			log.error("Error initializing service", e);
			service = null;
		}
		return service;
	}



	public static IDPService getIDPService() {

		if (service == null)
			log.error("IDPService is null!! Please init it again!");

		return service;
	}


}
