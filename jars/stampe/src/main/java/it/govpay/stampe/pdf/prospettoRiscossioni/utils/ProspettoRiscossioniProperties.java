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
package it.govpay.stampe.pdf.prospettoRiscossioni.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.exceptions.PropertyNotFoundException;
import it.govpay.stampe.pdf.Costanti;

public class ProspettoRiscossioniProperties {
	
	private static final String PROPERTIES_FILE = "/prospettoRiscossioni.properties";
	public static final String DEFAULT_PROPS = "default";

	private static ProspettoRiscossioniProperties instance;

	private static Logger log = LoggerWrapperFactory.getLogger(ProspettoRiscossioniProperties.class);

	private String govpayResourceDir = null;

	private Map<String, Properties> propMap = new HashMap<>();

	public static ProspettoRiscossioniProperties getInstance() {
		return instance;
	}

	public static synchronized ProspettoRiscossioniProperties newInstance(String govpayResourceDir) throws ConfigException {
		if(instance == null)
			instance = new ProspettoRiscossioniProperties(govpayResourceDir);
		return instance;
	}

	private Properties[] props  = null;

	public ProspettoRiscossioniProperties(String govpayResourceDir) throws ConfigException {
		try {

			// Recupero il property all'interno dell'EAR/WAR
			InputStream is = ProspettoRiscossioniProperties.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props1 = new Properties();
			props1.load(is);

			this.propMap.put(DEFAULT_PROPS, props1);

			Properties props0 = null;

			this.props = new Properties[2];
			this.props[0] = props0;
			this.props[1] = props1;

			// Recupero la configurazione della working dir
			// Se e' configurata, la uso come prioritaria

			try {
				this.govpayResourceDir = govpayResourceDir;

				if(this.govpayResourceDir != null) {
					File resourceDirFile = new File(this.govpayResourceDir);
					if(!resourceDirFile.isDirectory())
						throw new ConfigException(MessageFormat.format(Costanti.ERROR_MSG_IL_PATH_PASSATO_COME_PARAMTERO_0_NON_ESISTE_O_NON_E_UN_FOLDER, this.govpayResourceDir));


					File gpConfigFile = new File(this.govpayResourceDir + PROPERTIES_FILE);
					if(gpConfigFile.exists()) {
						props0 = new Properties();
						try(InputStream isExt = new FileInputStream(gpConfigFile)) {
							props0.load(isExt);
						} catch (IOException e) {
							throw new ConfigException(e);
						} 
						this.propMap.put(DEFAULT_PROPS, props0);
						log.info(Costanti.INFO_MSG_INDIVIDUATA_CONFIGURAZIONE_PRIORITARIA_0, gpConfigFile.getAbsolutePath());
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(Costanti.ERROR_MSG_ERRORE_DI_INIZIALIZZAZIONE_0_PROPERTY_IGNORATA, e.getMessage()));
			}

			// carico tutti i file che definiscono configurazioni diverse di avvisi pagamento
			if(this.govpayResourceDir != null) {
				File resourceDirFile = new File(this.govpayResourceDir);
				File[] listFiles = resourceDirFile.listFiles();
				if(listFiles != null)
					for(File f : listFiles) {
						if(!f.getName().startsWith("prospettoRiscossioni") && !f.getName().endsWith("properties")) {
							// Non e' un file di properties. lo salto
							continue;
						}
						Properties p = new Properties();
						try(InputStream isExt = new FileInputStream(f)) {
							p.load(isExt);
						} catch (FileNotFoundException e) {
							throw new ConfigException(e);
						} catch (IOException e) {
							throw new ConfigException(e);
						} 
						String key = f.getName().replaceAll(".properties", "");
						key = key.replaceAll("prospettoRiscossioni.", "");
						// la configurazione di defaut e' gia'stata caricata
						if(!key.equals("prospettoRiscossioni")) {
							log.info(Costanti.DEBUG_MSG_CARICATA_CONFIGURAZIONE_PROSPETTO_RISCOSSIONI_CON_CHIAVE_0, key);
							this.propMap.put(key, p);
						}
					}
			}
		}  catch (IOException e) {
			log.error(Costanti.ERROR_MSG_ERRORE_DI_INIZIALIZZAZIONE_0, e.getMessage());
			throw new ConfigException(e);
		}
	}

	private static String getProperty(String name, Properties props, boolean required) throws PropertyNotFoundException {
		String value = System.getProperty(name);

		if(value == null) {
			if(props != null) value = props.getProperty(name);
			if(value == null) {
				if(required) 
					throw new PropertyNotFoundException(MessageFormat.format(Costanti.ERROR_MSG_PROPRIETA_0_NON_TROVATA, name));
				else return null;
			} else {
				log.debug(Costanti.DEBUG_MSG_LETTA_PROPRIETA_DI_CONFIGURAZIONE_0_1, name, value);
			}
		} else {
			log.debug(Costanti.DEBUG_MSG_LETTA_PROPRIETA_DI_SISTEMA_0_1, name, value);
		}

		return value.trim();
	}

	public String getProperty(String idprops, String name, boolean required) throws PropertyNotFoundException {
		String value = null;
		Properties p = this.getProperties(idprops);

		try { value = getProperty(name, p, required); } catch (Exception e) { }
		if(value != null && !value.trim().isEmpty()) {
			return value;
		}

		log.debug(Costanti.DEBUG_MSG_PROPRIETA_NON_TROVATA_IN_CONFIGURAZIONE, name, idprops);

		if(required) 
			throw new PropertyNotFoundException(MessageFormat.format(Costanti.ERROR_MSG_PROPRIETA_0_NON_TROVATA_IN_CONFIGURAZIONE_1, name, idprops));
		else 
			return null;
	}


	public String getPropertyEnte(String idprop, String name) throws PropertyNotFoundException {
		String property = this.getProperty(idprop, name, false);
		return property != null ? property : "";
	}

	public Properties getProperties(String id) throws PropertyNotFoundException {
		if(id == null) id = DEFAULT_PROPS;
		Properties p = this.propMap.get(id);

		if(p == null) {
			log.debug(Costanti.ERROR_MSG_CONFIGURAZIONE_NON_TROVATA, id);
			throw new PropertyNotFoundException(MessageFormat.format(Costanti.ERROR_MSG_CONFIGURAZIONE_0_NON_TROVATA, id));
		}

		return p;
	}

	public Properties getPropertiesPerDominio(String codDominio,Logger log) throws PropertyNotFoundException {
		return this.getPropertiesPerDominioTributo(codDominio, null, log);
	}

	public Properties getPropertiesPerDominioTributo(String codDominio,String codTributo,Logger log) throws PropertyNotFoundException {
		Properties p = null;
		String key = null;
	
		// 1. ricerca delle properties per la chiave "codDominio -> codTributo"
		if(StringUtils.isNotEmpty(codTributo) && StringUtils.isNotEmpty(codDominio)) {
			key = codDominio + "." + codTributo;
			try{
				log.debug(Costanti.DEBUG_MSG_RICERCA_DELLE_PROPERTIES_PER_LA_CHIAVE_0, key);
				p = this.getProperties(key);
			}catch(Exception e){
				log.debug(Costanti.DEBUG_MSG_NON_SONO_STATE_TROVATE_PROPERTIES_PER_LA_CHIAVE_0_1, key, e.getMessage());
			}
		}

		// 2 . ricerca per codDominio
		if(StringUtils.isNotEmpty(codDominio)) {
			if(p == null){
				key = codDominio;
				try{
					log.debug(Costanti.DEBUG_MSG_RICERCA_DELLE_PROPERTIES_PER_LA_CHIAVE_0, key);
					p = this.getProperties(key);
				}catch(Exception e){
					log.debug(Costanti.DEBUG_MSG_NON_SONO_STATE_TROVATE_PROPERTIES_PER_LA_CHIAVE_0_1, key, e.getMessage());
				}
			}
		}

		// utilizzo le properties di default
		try{
			log.debug(Costanti.DEBUG_MSG_RICERCA_DELLE_PROPERTIES_DI_DEFAULT);
			p = this.getProperties(null);
		}catch(PropertyNotFoundException e){
			log.debug(Costanti.DEBUG_MSG_NON_SONO_STATE_TROVATE_PROPERTIES_DI_DEFAULT_0, e.getMessage());
			throw e;
		}

		return p;
	}
}
