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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;

/**
 * Provides factory methods for creating {@code OAuth2TokenValidator<Jwt>}
 * 
 * Based on {@link org.springframework.security.oauth2.jwt.JwtValidators}
 *
 * @author pintori@link.it
 */
public class GovPayJwtValidators {

	private GovPayJwtValidators() {
	}

	/**
     * Crea un nuovo validatore per utilizzare i validatori custom di GovPay
	 * @param issuer the issuer
	 * @param audience the audience
	 * @return - a delegating validator containing all standard validators as well as any
	 * supplied
	 */
	public static OAuth2TokenValidator<Jwt> createDefaultWithIssuerAndAudience(String issuer, String audience) {
		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
		validators.add(new JwtTimestampValidator());
		validators.add(new GovPayJwtIssuerValidator(issuer));
		validators.add(new GovPayJwtAudienceValidator(audience));
		return new DelegatingOAuth2TokenValidator<>(validators);
	}

	/**
	 * Crea un validatore completo includendo issuer, audience e claims custom
	 * @param issuer the issuer
	 * @param audience the audience
	 * @param claimValidationRules mappa claim name -> valori validi
	 * @return - a delegating validator containing all standard validators as well as any supplied
	 */
	public static OAuth2TokenValidator<Jwt> createDefaultWithIssuerAudienceAndClaims(
			String issuer,
			String audience,
			Map<String, Collection<String>> claimValidationRules) {
		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
		validators.add(new JwtTimestampValidator());
		validators.add(new GovPayJwtIssuerValidator(issuer));
		validators.add(new GovPayJwtAudienceValidator(audience));
		validators.add(new GovPayJwtClaimsValidator(claimValidationRules));
		return new DelegatingOAuth2TokenValidator<>(validators);
	}

	/**
	 * Crea un validatore con timestamp e claims custom (senza issuer/audience)
	 * @param claimValidationRules mappa claim name -> valori validi
	 * @return - a delegating validator containing timestamp and claims validators
	 */
	public static OAuth2TokenValidator<Jwt> createDefaultWithClaims(
			Map<String, Collection<String>> claimValidationRules) {
		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
		validators.add(new JwtTimestampValidator());
		validators.add(new GovPayJwtClaimsValidator(claimValidationRules));
		return new DelegatingOAuth2TokenValidator<>(validators);
	}

	/**
	 * Crea un validatore completamente custom con timestamp e una lista di validatori
	 * @param customValidators lista di validatori custom da aggiungere
	 * @return - a delegating validator containing all validators
	 */
	public static OAuth2TokenValidator<Jwt> createWithCustomValidators(
			List<OAuth2TokenValidator<Jwt>> customValidators) {
		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
		validators.add(new JwtTimestampValidator());
		if (customValidators != null && !customValidators.isEmpty()) {
			validators.addAll(customValidators);
		}
		return new DelegatingOAuth2TokenValidator<>(validators);
	}

}
