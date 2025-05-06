/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.authentication.oauth2.server.jwt;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;

import it.govpay.core.autorizzazione.beans.GovPayLdapJwt;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.autorizzazione.BaseAutenticazioneDAO;


/***
 * Classe basata su {@link JwtAuthenticationConverter}, riscritta perche' non si poteva fare l'override del metodo AbstractAuthenticationToken convert(Jwt jwt).
 * Estende le funzionalita' della classe originale per caricare le informazioni relative all'utenza GovPay all'interno dell'oggetto {@link AbstractAuthenticationToken}
 *
 *
 * @author pintori@link.it
 *
 */
public class GovPayJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	private String principalClaimName = JwtClaimNames.SUB;

	private BaseAutenticazioneDAO userDetailService;

	private Logger logger = LoggerWrapperFactory.getLogger(GovPayJwtAuthenticationConverter.class);

	@Override
	public final AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authoritiesFromSuperClass = this.jwtGrantedAuthoritiesConverter.convert(jwt);

		logger.debug("Ricerca principal all'interno dei claim {}", this.principalClaimName);

		Set<Entry<String,Object>> entrySet = jwt.getClaims().entrySet();
		logger.trace("Claims disponibili: ");
		for (Entry<String, Object> entry : entrySet) {
			logger.trace("{}: {}", entry.getKey(), entry.getValue());
		}

		String[] split = this.principalClaimName.split(",");

		String principalClaimValue = null;
		for (String claimName : split) {
			principalClaimValue = jwt.getClaimAsString(claimName);
			if(principalClaimValue != null) {
				break;
			}
		}
		
		logger.debug("Trovato principal {}", principalClaimValue);

		GovPayLdapJwt govPayJwt = new GovPayLdapJwt(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), jwt.getClaims());

		// creo un utenza ldap fittizzia per caricare le informazioni utente
		LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
		essence.setAccountNonExpired(true);
		essence.setAccountNonLocked(true);
		essence.setCredentialsNonExpired(true);
		essence.setEnabled(true);
		essence.setUsername(principalClaimValue);
		essence.setPassword(AutorizzazioneUtils.generaPasswordUtenza());
		essence.setAuthorities(authoritiesFromSuperClass);
		essence.setDn(principalClaimValue);

		LdapUserDetails createUserDetails = essence.createUserDetails();
		govPayJwt.setLdapUserDetailsImpl(createUserDetails);

		// leggo le informazioni sull'utenza nel formato GovPay
		GovpayLdapUserDetails details = new GovpayLdapUserDetails();
		details.setLdapUserDetailsImpl(createUserDetails);
		UserDetails loadUserByLdapUserDetail = this.userDetailService.loadUserByLdapUserDetail(govPayJwt.getUsername(), details);
		if(loadUserByLdapUserDetail instanceof GovpayLdapUserDetails govpayDetails) {
			govPayJwt.setUtenza(govpayDetails.getUtenza());
			govPayJwt.setApplicazione(govpayDetails.getApplicazione());
			govPayJwt.setOperatore(govpayDetails.getOperatore());
			govPayJwt.setIdTransazioneAutenticazione(govpayDetails.getIdTransazioneAutenticazione());
			govPayJwt.setTipoUtenza(govpayDetails.getTipoUtenza());
		}

		return new JwtAuthenticationToken(govPayJwt, authoritiesFromSuperClass, principalClaimValue);
	}

	/**
	 * Sets the {@link Converter Converter&lt;Jwt, Collection&lt;GrantedAuthority&gt;&gt;}
	 * to use. Defaults to {@link JwtGrantedAuthoritiesConverter}.
	 * @param jwtGrantedAuthoritiesConverter The converter
	 * @since 5.2
	 * @see JwtGrantedAuthoritiesConverter
	 */
	public void setJwtGrantedAuthoritiesConverter(
			Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
		Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
		this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
	}

	/**
	 * Sets the principal claim name. Defaults to {@link JwtClaimNames#SUB}.
	 * @param principalClaimName The principal claim name
	 * @since 5.4
	 */
	public void setPrincipalClaimName(String principalClaimName) {
		Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
		this.principalClaimName = principalClaimName;
	}

	public BaseAutenticazioneDAO getUserDetailService() {
		return userDetailService;
	}

	public void setUserDetailService(BaseAutenticazioneDAO userDetailService) {
		this.userDetailService = userDetailService;
	}
}
