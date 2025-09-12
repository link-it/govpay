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
package it.govpay.core.beans;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.exceptions.PropertyNotFoundException;
import it.govpay.core.utils.LogUtils;

public class GovPayExceptionProperties {

	public static final String GOVPAY_EXCEPTION_MESSAGES_PROPERTIES_FILE_NAME = "govpay-exception-messages.properties";
	public static final String GOVPAY_EXCEPTION_MESSAGES_PROPERTIES_FILE = "/" + GOVPAY_EXCEPTION_MESSAGES_PROPERTIES_FILE_NAME;
	private static GovPayExceptionProperties instance;
	private Properties props;
	private Map<String, String> mappingGovPayException = null;


	public static GovPayExceptionProperties getInstance() {
		return instance;
	}

	public static GovPayExceptionProperties newInstance(InputStream is) throws ConfigException {
		instance = new GovPayExceptionProperties(is);
		return instance;
	}

	public GovPayExceptionProperties(InputStream is) throws ConfigException {
		Logger bootLogger = LoggerWrapperFactory.getLogger("boot");
		try {
			this.mappingGovPayException = new HashMap<>();

			bootLogger.debug("Caricamento Mapping GovPay Exception..."); 
			// Recupero il property all'interno dell'JAR
			this.props = new Properties();
			this.props.load(is);

			this.mappingGovPayException = getProperties(this.props, false);
			bootLogger.debug("Caricamento Mapping GovPay Exception completato."); 
		}  catch (PropertyNotFoundException | IOException e) {
			LogUtils.logError(bootLogger, "Errore di inizializzazione gestore Mapping GovPay Exception: " + e.getMessage(), e); 
			throw new ConfigException(e);
		}
	}

	private static Map<String,String> getProperties(Properties props, boolean required) throws PropertyNotFoundException {
		Map<String, String> valori = new HashMap<>();
		String value = null;
		for (Object nameObj : props.keySet()) {
			String key = (String) nameObj;
			try { value = getProperty(key, props, required); } catch (PropertyNotFoundException e) { /* donothing */}
			if(value != null && !value.trim().isEmpty()) {
				valori.put(key, value.trim());
			}
		}

		return valori;
	}

	private static String getProperty(String name, Properties props, boolean required) throws PropertyNotFoundException {
		String value = null;

		if(props != null) {
			value = props.getProperty(name);
			if(value != null && value.trim().isEmpty()) {
				value = null;
			}
		}
		if(value == null) {
			if(required) 
				throw new PropertyNotFoundException("Proprieta' ["+name+"] non trovata");
			else return null;
		}

		return value.trim();
	}

	public String message(String key, Object... args) {
		String pattern = this.getMappingGovPayException().get(key);
		if (pattern == null || pattern.isEmpty()) return "";
		return MessageFormat.format(pattern, args == null ? new Object[0] : args);
	}
	
	/** Shortcut: esito.<CODE>.message */
	public String getMessage(EsitoOperazione code, Object... args) {
		return message(MessageFormat.format("esito.{0}.message", code), args);
	}
	/** Shortcut: esito.<CODE>.description */
	public String getDescrizioneEsito(EsitoOperazione code) {
		return this.getMappingGovPayException().get(MessageFormat.format("esito.{0}.description", code));
	}
	/** Shortcut: esito.<CODE>.status con default. */
	public int getStatusCode(EsitoOperazione code, int defaultValue) {
		String raw = this.getMappingGovPayException().get(MessageFormat.format("esito.{0}.status", code));
		if (raw == null || raw.isEmpty()) return defaultValue;
		try {
			return Integer.parseInt(raw);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public Map<String, String> getMappingGovPayException() {
		return mappingGovPayException;
	}
}
