package com.vmware.eucenablement.sample.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.eucenablement.sample.idp.MyIDP;
import com.vmware.samltoolkit.idp.SAMLSsoRequest;

public class MyIDPServlet implements Servlet  {

	private static Logger log = LoggerFactory.getLogger(MyIDPServlet.class);

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

		Request request = (Request) req;

		// set session
		  //TODO: FIXME: DON'T use "setRequestedSessionId". Each session should its unified JSESSIONID. If you want to pass a message to the other session, you can use a in-memory global container (Map, Cache, Set, etc.).
		//This is just a sample. But if you are writing a production, you may consider database or message bus/message queue for multiple servers.

		String jsessionid=request.getParameter("JSESSIONID");
		if (jsessionid!=null) {
			request.setRequestedSessionId(jsessionid);
			request.setSession(request.getSessionHandler().getHttpSession(jsessionid));
		}

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
			String ssoresponse = "";
            //TODO: FIXME: For We-chat OAuth Sample, please get username from session, DON'T trust username in request.
			//You might ask why I trust username in my previous Sample_Authserver. Well, what I wrote is a "Auth Server Sample", there should be a password parameter together with the username paramter, and the developer who refers to this Sample should verify username/password pair in this function by himself.
			//But WeChat OAuth Sample is different, you are writing a OAuth Client Sample. That's why I ask you to write a new Sample, don't change this Sample.

			//handle the login request
			//no matter what you input, we regard it successful here
			String user = request.getParameter("username");
			if (user!=null && user.length()>20)
				user=user.substring(0, 20);
			log.info("user name is successful "+user);
			SAMLSsoRequest ssoRequest = (SAMLSsoRequest) session.getAttribute("request");
			if (ssoRequest==null){
				//IDP initiated sso, check vidm
				String vidm = request.getParameter("vidmURL");
				if (vidm ==null){
					throw new ServletException("Invalid Access!");
				}
				//TODO: relay
				String relay = "";
				ssoresponse = MyIDP.getIDPService().getSSOResponseByPostBinding(vidm, user, relay);
			}else{
				 ssoresponse = MyIDP.getIDPService().getSSOResponseByPostBinding(ssoRequest, user);

			}

			log.info(ssoresponse);
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(ssoresponse.getBytes());
		}


	}


}
