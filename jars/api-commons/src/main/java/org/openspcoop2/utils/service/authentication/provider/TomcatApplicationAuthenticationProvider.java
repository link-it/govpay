/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package org.openspcoop2.utils.service.authentication.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.govpay.core.utils.LogUtils;


/**
 * TomcatApplicationAuthenticationProvider
 * 
 * Classe che utilizza le configurazioni utenti create tramite tomcat
 * 
 * ...
 * <b:bean id="tomcatApplicationAuthenticationProvider" class="org.openspcoop2.utils.jaxrs.impl.authentication.provider.TomcatApplicationAuthenticationProvider" >
 *     <!-- <b:property name="userDetailsService" ref="userDetailServiceUtenze"/> -->
 * </b:bean>
 * ...
 * <authentication-manager alias="authenticationManager">
 *		<authentication-provider ref="tomcatApplicationAuthenticationProvider"/>
 * </authentication-manager>
 * ...
 * 
 * @author Andrea Poli (poli@link.it)
 * @author Giuliano Pintori (giuliano.pintori@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
/**
 * @author poli
 *
 */
public class TomcatApplicationAuthenticationProvider implements AuthenticationProvider{

	private Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	private String requiredRole = "pagopa"; // Ruolo richiesto per autenticare l'utente
	private String tomcatUserFileName = "/conf/tomcat-users.xml";
	private String configDir = "catalina.home";

	private UserDetailsService userDetailsService;
	
	public static final String PASSWORD_DEFAULT_VALUE = "UTENZA_SENZA_PASSWORD";
	
	public static String generaPasswordUtenza() {
		return PASSWORD_DEFAULT_VALUE;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		Object passwordObject = authentication.getCredentials();
		String password = (String) passwordObject;

		if(username==null || password==null) {
			throw new AuthenticationCredentialsNotFoundException("Credentials not found");
		}

		String confDir = System.getProperty(this.configDir);
		if(confDir==null) {
			throw new ProviderNotFoundException("Property '"+this.configDir+"' not found");
		}
		File confDirJBoss = new File(confDir);
		if(!confDirJBoss.exists()) {
			throw new ProviderNotFoundException("File '"+confDirJBoss.getAbsolutePath()+"' not exists");
		}
		if(!confDirJBoss.isDirectory()) {
			throw new ProviderNotFoundException("File '"+confDirJBoss.getAbsolutePath()+"' isn't directory");
		}

		// check utenza da file tomcat-users.xml
		File fUsers = new File(confDirJBoss, this.tomcatUserFileName);
		
		log.trace("Lettura file utenti tomcat: {}", fUsers.getAbsolutePath());
		if(!fUsers.exists()) {
			throw new ProviderNotFoundException("File '"+fUsers.getAbsolutePath()+"' not exists");
		}
		if(!fUsers.canRead()) {
			throw new ProviderNotFoundException("File '"+fUsers.getAbsolutePath()+"' cannot read");
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// Protezione XXE (XML External Entity)
			// Disabilita completamente DTD per prevenire XXE attacks
			try {
				factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				factory.setXIncludeAware(false);
				factory.setExpandEntityReferences(false);
			} catch (ParserConfigurationException e) {
				throw new ProviderNotFoundException("Errore durante la configurazione sicura del parser XML: " + e.getMessage());
			}

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fUsers);
			document.getDocumentElement().normalize();

			NodeList userList = document.getElementsByTagName("user");

			boolean found = false;
			String xmlUsername = null;
			String xmlPassword = null;
			List<GrantedAuthority> roles = null;
			for (int i = 0; i < userList.getLength(); i++) {
				Element userElement = (Element) userList.item(i);

				xmlUsername = userElement.getAttribute("username");
				xmlPassword = userElement.getAttribute("password");

				String rolesAttr = userElement.getAttribute("roles");
				roles = new ArrayList<>();
				for (String role : rolesAttr.split(",")) {
					roles.add(new SimpleGrantedAuthority(role.trim()));
				}

				// Verifica se l'utente corrisponde
				if (xmlUsername.equals(username) && xmlPassword.equals(password)) {
					found = true;
					break;
				}
			}

			if(!found) {
				//throw new UsernameNotFoundException("Username '"+username+"' not found")
				// Fix security: Make sure allowing user enumeration is safe here.
				throw new BadCredentialsException("Bad credentials");
			}

			// Verifica se l'utente ha il ruolo richiesto
			if (roles.stream().noneMatch(auth -> auth.getAuthority().equals(this.requiredRole))) {
				throw new BadCredentialsException("L'utente non possiede il ruolo richiesto");
			}

			// Wrap in UsernamePasswordAuthenticationToken
			UsernamePasswordAuthenticationToken userAuth = null;
			if(this.userDetailsService!=null) {
				try {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					userAuth = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
				}catch(UsernameNotFoundException e){
					String msg = "User '"+username+"' unknown: "+e.getMessage();
					LogUtils.logDebugException(this.log,msg,e);
					throw new BadCredentialsException(msg,e);
				}
			}
			else {	
				String passwordGenerata = generaPasswordUtenza();
				User user = new User(username, passwordGenerata, true, true, true, true, roles);
				userAuth = new UsernamePasswordAuthenticationToken(user, passwordGenerata, user.getAuthorities());
			}
			userAuth.setDetails(authentication.getDetails());
			return userAuth;

		} catch (Exception e) {
			LogUtils.logError(this.log, e.getMessage(),e);
			throw new ProviderNotFoundException("Errore durante la lettura del file degli utenti: " + e.getMessage());
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public String getConfigDir() {
		return this.configDir;
	}
	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}

	public UserDetailsService getUserDetailsService() {
		return this.userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public String getRequiredRole() {
		return this.requiredRole;
	}

	public void setRequiredRole(String requiredRole) {
		this.requiredRole = requiredRole;
	}

	public String getTomcatUserFileName() {
		return this.tomcatUserFileName;
	}

	public void setTomcatUserFileName(String tomcatUserFileName) {
		this.tomcatUserFileName = tomcatUserFileName;
	}


}
