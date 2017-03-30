package com.vmware.eucenablement.saml.sample.idp;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.saml.sample.ConsumerServlet;
import com.vmware.samltookit.idp.SAMLSsoRequest;

public class MyIDPServlet implements Servlet  {

	private static Logger log = LoggerFactory.getLogger(ConsumerServlet.class);

	@Override
	public void destroy() {


	}

	@Override
	public ServletConfig getServletConfig() {

		return null;
	}

	@Override
	public String getServletInfo() {

		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {


	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String s = request.getParameter("SAMLRequest");
		HttpSession session = request.getSession();

		if (s!=null && s.length()>0){
			//handle samle request
			try {

				String relay = request.getParameter("RelayState");
				SAMLSsoRequest ssoRequest = MyIDP.getIDPService().decodeSAMLRequest(s);
				if (relay!=null && relay.length()>0){
					ssoRequest.setRelay(relay);
				}


				if (ssoRequest != null && ssoRequest.isValid() ) {
					session.setAttribute("request", ssoRequest);
					response.sendRedirect("idpLogin.jsp");
					return;
				}

				log.error("Failed to get valid sso response!");
				throw new ServletException("Failed to login: Invalid SSO");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new ServletException(e);
			}
		}else{
			//handle the login request
			//no matter what you input, we regard it successful here
			String user = request.getParameter("username");
			log.info("user name is successful "+user);
			SAMLSsoRequest ssoRequest = (SAMLSsoRequest) session.getAttribute("request");
			if (ssoRequest==null){
				throw new ServletException("Failed to login: Invalid SAML request in session!");
			}

			String ssoresponse = MyIDP.getIDPService().getSSOResponseByPostBinding(ssoRequest, user);

			log.info(ssoresponse);
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(ssoresponse.getBytes());
		}


	}


}
