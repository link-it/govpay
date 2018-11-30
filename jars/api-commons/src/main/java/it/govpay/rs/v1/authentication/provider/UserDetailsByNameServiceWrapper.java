package it.govpay.rs.v1.authentication.provider;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

public class UserDetailsByNameServiceWrapper <T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
	
	private AuthenticationUserDetailsService<T> authenticationUserDetailsService = null;

	/**
	 * Constructs an empty wrapper for compatibility with Spring Security 2.0.x's method
	 * of using a setter.
	 */
	public UserDetailsByNameServiceWrapper() {
		// constructor for backwards compatibility with 2.0
	}

	/**
	 * Constructs a new wrapper using the supplied
	 * {@link org.springframework.security.core.userdetails.UserDetailsService} as the
	 * service to delegate to.
	 *
	 * @param userDetailsService the UserDetailsService to delegate to.
	 */
	public UserDetailsByNameServiceWrapper(final AuthenticationUserDetailsService<T> authenticationUserDetailsService) {
		Assert.notNull(authenticationUserDetailsService, "authenticationUserDetailsService cannot be null.");
		this.authenticationUserDetailsService = authenticationUserDetailsService;
	}

	/**
	 * Check whether all required properties have been set.
	 *
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.authenticationUserDetailsService, "AuthenticationUserDetailsService must be set");
	}

	/**
	 * Get the UserDetails object from the wrapped UserDetailsService implementation
	 */
	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		return this.authenticationUserDetailsService.loadUserDetails(authentication);
	}

	/**
	 * Set the wrapped UserDetailsService implementation
	 *
	 * @param aUserDetailsService The wrapped UserDetailsService to set
	 */
	public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService<T> authenticationUserDetailsService) {
		this.authenticationUserDetailsService = authenticationUserDetailsService;
	}

}
