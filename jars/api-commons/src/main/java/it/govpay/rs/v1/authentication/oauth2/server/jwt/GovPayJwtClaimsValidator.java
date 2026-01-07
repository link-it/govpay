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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

/**
 * Validatore generico per il controllo di claim multipli del token JWT.
 * Permette di specificare per ogni claim una lista di valori validi.
 *
 * Basato su {@link GovPayJwtAudienceValidator}
 *
 * @author pintori@link.it
 */
public class GovPayJwtClaimsValidator implements OAuth2TokenValidator<Jwt> {

	private static final String ERROR_CODE_INVALID_CLAIM = "invalid_token";
	private static final String ERROR_DESCRIPTION_TEMPLATE = "The claim '%s' with value '%s' is not valid. Expected one of: %s";

	private final Logger logger = LoggerWrapperFactory.getLogger(GovPayJwtClaimsValidator.class);

	/**
	 * Mappa che associa il nome del claim ai suoi valori attesi.
	 * Key: nome del claim (es: "scope", "client_id", "groups")
	 * Value: Set di valori validi per quel claim
	 */
	private final Map<String, Set<String>> claimValidationRules;

	/**
	 * Flag per decidere se il validatore deve essere eseguito o saltato
	 */
	private boolean skipValidation = false;

	/**
	 * Costruisce un {@link GovPayJwtClaimsValidator} usando le regole di validazione fornite.
	 *
	 * @param claimValidationRules - Mappa che associa il nome del claim (String)
	 *                                ai valori validi (Collection&lt;String&gt;)
	 */
	public GovPayJwtClaimsValidator(Map<String, Collection<String>> claimValidationRules) {
		Assert.notNull(claimValidationRules, "claimValidationRules cannot be null");

		// Se la mappa è vuota, skippiamo la validazione
		if (claimValidationRules.isEmpty()) {
			this.skipValidation = true;
			this.claimValidationRules = Collections.emptyMap();
			this.logger.warn("Nessuna regola di validazione claim fornita. Validazione claims disabilitata.");
			return;
		}

		// Convertiamo la mappa in una struttura interna immutabile
		this.claimValidationRules = new HashMap<>();
		for (Map.Entry<String, Collection<String>> entry : claimValidationRules.entrySet()) {
			String claimName = entry.getKey();
			Collection<String> expectedValues = entry.getValue();

			if (StringUtils.isEmpty(claimName)) {
				this.logger.warn("Nome claim vuoto ignorato nella configurazione di validazione.");
				continue;
			}

			if (expectedValues == null || expectedValues.isEmpty()) {
				this.logger.warn("Claim '{}': nessun valore atteso fornito. Validazione per questo claim disabilitata.", claimName);
				continue;
			}

			// Filtra valori vuoti o blank dalla lista
			List<String> nonBlankValues = new ArrayList<>();
			for (String value : expectedValues) {
				if (StringUtils.isNotBlank(value)) {
					nonBlankValues.add(value);
				}
			}

			// Se dopo il filtraggio non ci sono valori validi, skippiamo questo claim
			if (nonBlankValues.isEmpty()) {
				this.logger.warn("Claim '{}': tutti i valori sono vuoti/blank. Validazione per questo claim disabilitata.", claimName);
				continue;
			}

			// Convertiamo in Set per ricerca efficiente
			Set<String> expectedValuesSet = Set.copyOf(nonBlankValues);
			this.claimValidationRules.put(claimName, expectedValuesSet);

			this.logger.debug("Registrata regola di validazione per claim '{}' con {} valori attesi",
					claimName, expectedValuesSet.size());
		}

		// Se dopo il filtraggio non ci sono regole valide, skippiamo
		if (this.claimValidationRules.isEmpty()) {
			this.skipValidation = true;
			this.logger.warn("Nessuna regola di validazione claim valida. Validazione claims disabilitata.");
		}
	}

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		Assert.notNull(token, "token cannot be null");

		if (this.skipValidation) {
			this.logger.trace("Validazione claims skippata (nessuna regola configurata)");
			return OAuth2TokenValidatorResult.success();
		}

		List<OAuth2Error> errors = new ArrayList<>();

		// Iteriamo su tutte le regole di validazione configurate
		for (Map.Entry<String, Set<String>> entry : this.claimValidationRules.entrySet()) {
			String claimName = entry.getKey();
			Set<String> expectedValues = entry.getValue();

			// Otteniamo il valore del claim dal token
			Object claimValue = token.getClaim(claimName);

			if (claimValue == null) {
				this.logger.debug("Claim '{}' non presente nel token", claimName);
				String errorDescription = String.format(
						"The claim '%s' is missing in the token. Expected one of: %s",
						claimName, expectedValues);
				errors.add(new OAuth2Error(ERROR_CODE_INVALID_CLAIM, errorDescription, null));
				continue;
			}

			// Validiamo il valore del claim
			boolean isValid = validateClaimValue(claimName, claimValue, expectedValues);

			if (!isValid) {
				this.logger.debug("Claim '{}' ha valore non valido: {}", claimName, claimValue);
				String errorDescription = String.format(ERROR_DESCRIPTION_TEMPLATE,
						claimName, claimValue, expectedValues);
				errors.add(new OAuth2Error(ERROR_CODE_INVALID_CLAIM, errorDescription, null));
			} else {
				this.logger.trace("Claim '{}' validato con successo", claimName);
			}
		}

		if (!errors.isEmpty()) {
			return OAuth2TokenValidatorResult.failure(errors);
		}

		return OAuth2TokenValidatorResult.success();
	}

	/**
	 * Valida il valore di un claim contro i valori attesi.
	 * Gestisce diversi tipi di valore (String, Collection, Array).
	 *
	 * @param claimName Nome del claim (per logging)
	 * @param claimValue Valore del claim dal token
	 * @param expectedValues Set di valori attesi
	 * @return true se il valore è valido, false altrimenti
	 */
	private boolean validateClaimValue(String claimName, Object claimValue, Set<String> expectedValues) {
		// Caso 1: valore è una stringa singola
		if (claimValue instanceof String) {
			boolean isValid = expectedValues.contains(claimValue);
			this.logger.trace("Claim '{}' (String): '{}' - valido: {}", claimName, claimValue, isValid);
			return isValid;
		}

		// Caso 2: valore è una Collection
		if (claimValue instanceof Collection<?>) {
			for (Object element : ((Collection<?>) claimValue)) {
				if (element != null && expectedValues.contains(element.toString())) {
					this.logger.trace("Claim '{}' (Collection): trovato valore valido '{}'", claimName, element);
					return true;
				}
			}
			this.logger.trace("Claim '{}' (Collection): nessun valore valido trovato in {}", claimName, claimValue);
			return false;
		}

		// Caso 3: valore è un array
		if (claimValue.getClass().isArray()) {
			int length = Array.getLength(claimValue);
			for (int i = 0; i < length; i++) {
				Object element = Array.get(claimValue, i);
				if (element != null && expectedValues.contains(element.toString())) {
					this.logger.trace("Claim '{}' (Array): trovato valore valido '{}'", claimName, element);
					return true;
				}
			}
			this.logger.trace("Claim '{}' (Array): nessun valore valido trovato", claimName);
			return false;
		}

		// Caso 4: altri tipi - convertiamo a stringa e confrontiamo
		String stringValue = claimValue.toString();
		boolean isValid = expectedValues.contains(stringValue);
		this.logger.trace("Claim '{}' ({}): '{}' - valido: {}",
				claimName, claimValue.getClass().getSimpleName(), stringValue, isValid);
		return isValid;
	}

	/**
	 * Ritorna una copia immutabile delle regole di validazione configurate.
	 *
	 * @return Mappa claim name -> valori attesi
	 */
	public Map<String, Set<String>> getClaimValidationRules() {
		return Collections.unmodifiableMap(this.claimValidationRules);
	}

	/**
	 * Indica se la validazione è disabilitata.
	 *
	 * @return true se la validazione è skippata, false altrimenti
	 */
	public boolean isSkipValidation() {
		return this.skipValidation;
	}
}
