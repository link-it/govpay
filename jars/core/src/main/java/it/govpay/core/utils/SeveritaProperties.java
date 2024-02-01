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
package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.BaseExceptionV1.CategoriaEnum;
import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.exceptions.PropertyNotFoundException;

public class SeveritaProperties {

	private transient Logger log = LoggerWrapperFactory.getLogger(SeveritaProperties.class);;
	
	public static final String MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME = "erroriSeverita.properties";
	public static final String MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE = "/" + MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME;
	private static SeveritaProperties instance;
	private Properties[] props;
	private String resourceDir;
	private Map<String, String> mappingSeveritaErrori = null;
	

	public static SeveritaProperties getInstance() {
		return instance;
	}

	public static SeveritaProperties newInstance(InputStream is) throws ConfigException {
		instance = new SeveritaProperties(is);
		return instance;
	}

	public SeveritaProperties(InputStream is) throws ConfigException {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		try {
			this.mappingSeveritaErrori = new HashMap<>();

			log.debug("Caricamento Mapping Severita Errori..."); 
			this.resourceDir = GovpayConfig.getInstance().getResourceDir();
			// Recupero il property all'interno dell'EAR
			this.props = new Properties[2];
			Properties props1 = new Properties();
			props1.load(is);
			this.props[1] = props1;

			Properties props0 = null;
			this.props[0] = props0;

			File gpConfigFile = new File(this.resourceDir + File.separatorChar + MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME);
			if(gpConfigFile.exists()) {
				props0 = new Properties();
				try(InputStream isExt = new FileInputStream(gpConfigFile)) {
					props0.load(isExt);
				} catch (FileNotFoundException e) {
					throw new ConfigException(e);
				} catch (IOException e) {
					throw new ConfigException(e);
				} 
				log.debug("Individuata configurazione prioritaria Mapping Severita Errori: " + gpConfigFile.getAbsolutePath());
				this.props[0] = props0;
			}

			this.mappingSeveritaErrori = getProperties(props, false, log);
			log.debug("Caricamento Mapping Severita Errori completato."); 
		}  catch (PropertyNotFoundException e) {
			log.error("Errore di inizializzazione gestore Mapping Severita Errori: " + e.getMessage(), e); 
			throw new ConfigException(e);
		} catch (IOException e) {
			log.error("Errore di inizializzazione gestore Mapping Severita Errori: " + e.getMessage(), e); 
			throw new ConfigException(e);
		}

	}
	
	private static Map<String,String> getProperties(Properties[] props, boolean required, Logger log) throws PropertyNotFoundException {
		Map<String, String> valori = new HashMap<>();
		String value = null;
		for(int i=0; i<props.length; i++) {
			if(props[i] != null) {
				for (Object nameObj : props[i].keySet()) {
					String key = (String) nameObj;
					try { value = getProperty(key, props[i], required, i==1, log); } catch (PropertyNotFoundException e) { }
					if(value != null && !value.trim().isEmpty()) {
						if(!valori.containsKey(key)) {
							valori.put(key, value.trim());
						}
					}
				}
			}
		}

		return valori;
	}

	private static String getProperty(String name, Properties props, boolean required, boolean fromInternalConfig, Logger log) throws PropertyNotFoundException {
		String value = null;
//		String logString = "";
//		if(fromInternalConfig) logString = "da file interno ";
//		else logString = "da file esterno ";

		if(props != null) {
			value = props.getProperty(name);
			if(value != null && value.trim().isEmpty()) {
				value = null;
			}
		}
		if(value == null) {
			if(required) 
				throw new PropertyNotFoundException("TipoEvento ["+name+"] non trovata");
			else return null;
		} else {
			//if(log != null) log.trace("Letta proprieta di configurazione " + logString + name + ": " + value);
		}

		return value.trim();
	}

	public Map<String, String> getSeveritaErrori() {
		return mappingSeveritaErrori;
	}
	
	public Integer getSeverita(EsitoOperazione esito) {
		String severitaS = this.getSeveritaErrori().get(esito.toString());
		Integer severita = null;
		
		if(severitaS == null) {
			this.log.warn("Livello di severita' per l'EsitoOperazione '"+esito+"' non trovato all'interno del file "+MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME + ", verra' impostato il default '5'.");
			severitaS = "5";
		}
		
		try{
			severita = Integer.parseInt(severitaS);
		} catch(Throwable t) {
			this.log.error("Lettura del livello di severita' per l'EsitoOperazione '"+esito+"' terminata con errore: "+t.getMessage(),t);
			severita = null;
		}
		
		return severita;
	}
	
	public Integer getSeverita(CategoriaEnum categoria) {
		String severitaS = this.getSeveritaErrori().get(categoria.toString());
		Integer severita = null;
		
		if(severitaS == null) {
			this.log.warn("Livello di severita' per la BaseException.CategoriaEnum '"+categoria+"' non trovato all'interno del file "+MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME + ", verra' impostato il default '5'.");
			severitaS = "5";
		}
		
		try{
			severita = Integer.parseInt(severitaS);
		} catch(Throwable t) {
			this.log.error("Lettura del livello di severita' per la BaseException.CategoriaEnum '"+categoria+"' terminata con errore: "+t.getMessage(),t);
			severita = null;
		}
		
		return severita;
	}
	
}
