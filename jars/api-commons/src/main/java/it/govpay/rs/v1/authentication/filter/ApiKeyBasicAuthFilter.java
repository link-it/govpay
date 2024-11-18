/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs.v1.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Based on {@link BasicAuthenticationFilter}
 * 
 * Classe ridefinita poiche' non e' possibile impostare un authenticationConverter personalizzato
 * 
 * @author Giuliano Pintori
 *
 */
public class ApiKeyBasicAuthFilter extends OncePerRequestFilter {

	private static Logger log = LoggerWrapperFactory.getLogger(ApiKeyBasicAuthFilter.class);

	private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	private AuthenticationEntryPoint authenticationEntryPoint;

	private AuthenticationManager authenticationManager;

	private RememberMeServices rememberMeServices = new NullRememberMeServices();

	private boolean ignoreFailure = false;

	private String credentialsCharset = "UTF-8";

	private ApiKeyBasicAuthenticationConverter authenticationConverter = new ApiKeyBasicAuthenticationConverter();

	private SecurityContextRepository securityContextRepository = new NullSecurityContextRepository();

	/**
	 * Creates an instance which will authenticate against the supplied
	 * {@code AuthenticationManager} and which will ignore failed authentication attempts,
	 * allowing the request to proceed down the filter chain.
	 * @param authenticationManager the bean to submit authentication requests to
	 */
	public ApiKeyBasicAuthFilter(AuthenticationManager authenticationManager) {
		Assert.notNull(authenticationManager, "authenticationManager cannot be null");
		this.authenticationManager = authenticationManager;
		this.ignoreFailure = true;
	}

	/**
	 * Creates an instance which will authenticate against the supplied
	 * {@code AuthenticationManager} and use the supplied {@code AuthenticationEntryPoint}
	 * to handle authentication failures.
	 * @param authenticationManager the bean to submit authentication requests to
	 * @param authenticationEntryPoint will be invoked when authentication fails.
	 * Typically an instance of {@link BasicAuthenticationEntryPoint}.
	 */
	public ApiKeyBasicAuthFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		Assert.notNull(authenticationManager, "authenticationManager cannot be null");
		Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint cannot be null");
		this.authenticationManager = authenticationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/**
	 * Sets the {@link SecurityContextRepository} to save the {@link SecurityContext} on
	 * authentication success. The default action is not to save the
	 * {@link SecurityContext}.
	 * @param securityContextRepository the {@link SecurityContextRepository} to use.
	 * Cannot be null.
	 */
	public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
		Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
		this.securityContextRepository = securityContextRepository;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
		if (!isIgnoreFailure()) {
			Assert.notNull(this.authenticationEntryPoint, "An AuthenticationEntryPoint is required");
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);
			if (authRequest == null) {
				this.logger.trace("Did not process authentication request since failed to find "
						+ "username and password in Basic Authorization header");
				chain.doFilter(request, response);
				return;
			}
			String username = authRequest.getName();
			this.logger.trace(LogMessage.format("Found username '%s' in Basic Authorization header", username));
			if (authenticationIsRequired(username)) {
				Authentication authResult = this.authenticationManager.authenticate(authRequest);
				SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
				context.setAuthentication(authResult);
				this.securityContextHolderStrategy.setContext(context);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
				}
				this.rememberMeServices.loginSuccess(request, response, authResult);
				this.securityContextRepository.saveContext(context, request, response);
				onSuccessfulAuthentication(request, response, authResult);
			}
		}
		catch (AuthenticationException ex) {
			this.securityContextHolderStrategy.clearContext();
			this.logger.debug("Failed to process authentication request", ex);
			this.rememberMeServices.loginFail(request, response);
			onUnsuccessfulAuthentication(request, response, ex);
			if (this.ignoreFailure) {
				chain.doFilter(request, response);
			}
			else {
				this.authenticationEntryPoint.commence(request, response, ex);
			}
			return;
		}

		chain.doFilter(request, response);
	}

	private boolean authenticationIsRequired(String username) {
		// Only reauthenticate if username doesn't match SecurityContextHolder and user
		// isn't authenticated (see SEC-53)
		Authentication existingAuth = this.securityContextHolderStrategy.getContext().getAuthentication();
		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			return true;
		}
		// Limit username comparison to providers which use usernames (ie
		// UsernamePasswordAuthenticationToken) (see SEC-348)
		if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
			return true;
		}
		// Handle unusual condition where an AnonymousAuthenticationToken is already
		// present. This shouldn't happen very often, as BasicProcessingFitler is meant to
		// be earlier in the filter chain than AnonymousAuthenticationFilter.
		// Nevertheless, presence of both an AnonymousAuthenticationToken together with a
		// BASIC authentication request header should indicate reauthentication using the
		// BASIC protocol is desirable. This behaviour is also consistent with that
		// provided by form and digest, both of which force re-authentication if the
		// respective header is detected (and in doing so replace/ any existing
		// AnonymousAuthenticationToken). See SEC-610.
		return (existingAuth instanceof AnonymousAuthenticationToken);
	}

	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		//donothing
	}

	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		//donothing
	}

	protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return this.authenticationEntryPoint;
	}

	protected AuthenticationManager getAuthenticationManager() {
		return this.authenticationManager;
	}

	protected boolean isIgnoreFailure() {
		return this.ignoreFailure;
	}

	/**
	 * Sets the {@link SecurityContextHolderStrategy} to use. The default action is to use
	 * the {@link SecurityContextHolderStrategy} stored in {@link SecurityContextHolder}.
	 *
	 * @since 5.8
	 */
	public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
		Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
		this.securityContextHolderStrategy = securityContextHolderStrategy;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		this.authenticationConverter.setAuthenticationDetailsSource(authenticationDetailsSource);
	}

	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		Assert.notNull(rememberMeServices, "rememberMeServices cannot be null");
		this.rememberMeServices = rememberMeServices;
	}

	public void setCredentialsCharset(String credentialsCharset) {
		Assert.hasText(credentialsCharset, "credentialsCharset cannot be null or empty");
		this.credentialsCharset = credentialsCharset;
	}

	protected String getCredentialsCharset(HttpServletRequest httpRequest) {
		return this.credentialsCharset;
	}

}
