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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Set;
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
 * Validator per il controllo del campo "aud" del token JWT
 * 
 * based on {@link JwtIssuerValidator}
 * 
 * @author pintori@link.it
 */
public class GovPayJwtAudienceValidator implements OAuth2TokenValidator<Jwt> {
	
	private boolean skipCheck = false;
	
	private final JwtClaimValidator<Object> validator;

	private final Logger logger = LoggerWrapperFactory.getLogger(GovPayJwtAudienceValidator.class);
	
	/**
	 * Constructs a {@link JwtIssuerValidator} using the provided parameters
	 * @param audience - The audience that each {@link Jwt} should have.
	 */
	public GovPayJwtAudienceValidator(String audience) {
		Assert.notNull(audience, "audience cannot be null");
		
		if (StringUtils.isEmpty(audience)) {
			this.skipCheck = true;
			this.logger.warn("Controllo Audience disabilitato.");
		}
		
		Predicate<Object> testClaimValue = createPredicate(audience);
		
		this.validator = new JwtClaimValidator<>(JwtClaimNames.AUD, testClaimValue);
	}

	private Predicate<Object> createPredicate(String audience) {
		Set<String> expectedAudiences = Set.of(audience);
		
		return claimValue -> {
		    if (claimValue == null) return false;

		    if (claimValue instanceof String) {
		        return expectedAudiences.contains(claimValue);
		    }
		    if (claimValue instanceof Collection<?>) {
		        for (Object e : ((Collection<?>) claimValue)) {
		            if (e != null && expectedAudiences.contains(e.toString())) return true;
		        }
		        return false;
		    }
		    if (claimValue.getClass().isArray()) {
		        int len = Array.getLength(claimValue);
		        for (int i = 0; i < len; i++) {
		            Object e = Array.get(claimValue, i);
		            if (e != null && expectedAudiences.contains(e.toString())) return true;
		        }
		        return false;
		    }
		    return expectedAudiences.contains(claimValue.toString());
		};
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
