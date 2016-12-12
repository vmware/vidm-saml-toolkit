/*
 * VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

*/
package sample;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.samltoolkit.SAMLToolkitConf;
import com.vmware.samltoolkit.SsoServiceFacade;

public class MySSO {
	private static SsoServiceFacade facade;
	private static Logger log = LoggerFactory.getLogger(MySSO.class);
	public static SsoServiceFacade initSsoServiceFacade(String vidmURL, String consumerURL, boolean byPassCert){
		log.info("Start to init SSO Service "+ vidmURL + " consumer:"+consumerURL+" bypasscert:"+byPassCert);
		try{
			SAMLToolkitConf conf = new SAMLToolkitConf();
			conf.setIdpURL(vidmURL);
			conf.setConsumerURL(consumerURL);
			conf.setByPassSSLCertValidation(byPassCert);
			log.info("Config initiated:"+conf.isReady());

			facade = new SsoServiceFacade(conf);
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			facade =null;
		}

		return facade;
	}

	public static String getvIDMURL(){
		if (facade==null){
			return null;
		}
		return facade.getVIDMURL();
	}

	public static SsoServiceFacade getSSOFacade(){
		if (facade==null){
			log.error("SSOFacade is null!! Please init it again!");
		}
		log.info("Start to return SSO Service:"+facade);
		return facade;
	}


}
