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
package it.govpay.rs.v1.authentication.oauth2.server.jwt;

import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.util.Assert;

/**
 * Validator per il controllo del campo "issuer" del token JWT
 * 
 * based on {@link JwtIssuerValidator}
 * 
 * @author pintori@link.it
 */
public class GovPayJwtIssuerValidator implements OAuth2TokenValidator<Jwt> {
	
	private boolean skipCheck = false;
	
	private final JwtClaimValidator<Object> validator;

	private final Logger logger = LoggerWrapperFactory.getLogger(GovPayJwtIssuerValidator.class);
	
	/**
	 * Constructs a {@link GovPayJwtIssuerValidator} using the provided parameters
	 * @param issuer - The issuer that each {@link Jwt} should have.
	 */
	public GovPayJwtIssuerValidator(String issuer) {
		Assert.notNull(issuer, "issuer cannot be null");
		
		if (StringUtils.isEmpty(issuer)) {
			this.skipCheck = true;
			this.logger.warn("Controllo Issuer disabilitato.");
		}

		Predicate<Object> testClaimValue = (claimValue) -> (claimValue != null) && issuer.equals(claimValue.toString());
		this.validator = new JwtClaimValidator<>(JwtClaimNames.ISS, testClaimValue);
	}

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		Assert.notNull(token, "token cannot be null");
		if (this.skipCheck) {
			return OAuth2TokenValidatorResult.success();
		}
		return this.validator.validate(token);
	}
}
