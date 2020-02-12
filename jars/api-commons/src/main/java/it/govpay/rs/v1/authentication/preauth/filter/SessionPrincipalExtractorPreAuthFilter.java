package it.govpay.rs.v1.authentication.preauth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import it.govpay.core.autorizzazione.AuthorizationManager;


public class SessionPrincipalExtractorPreAuthFilter extends
AbstractPreAuthenticatedProcessingFilter {
	
	public static final String SESSION_PRINCIPAL_ATTRIBUTE_NAME = AuthorizationManager.SESSION_PRINCIPAL_ATTRIBUTE_NAME;
	public static final String SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME = AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME;

	/**
	 * Public constructor which overrides the default AuthenticationDetails class to be
	 * used.
	 */
	public SessionPrincipalExtractorPreAuthFilter() {
		super();
	}

	// setAuthenticationDetailsSource(new WebSpherePreAuthenticatedWebAuthenticationDetailsSource());
	/**
	 * Return the WebSphere user name.
	 */
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpRequest) {
		if(httpRequest.getSession(false) != null) {
			HttpSession session = httpRequest.getSession(false);
			
			Object principal = session.getAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_ATTRIBUTE_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("PreAuthenticated Govpay session principal: " + principal);
			}
			return principal;
		}

		return null;
	}

	/**
	 * For J2EE container-based authentication there is no generic way to retrieve the
	 * credentials, as such this method returns a fixed dummy value.
	 */
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpRequest) {
		return "N/A";
	}

}
