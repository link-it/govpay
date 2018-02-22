/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.pagamento.util.CustomIuv;


public class GovpayCustomConfig {
	
	private static final String PROPERTIES_FILE = "/govpay.properties";

	private static GovpayCustomConfig instance;
	
	public static GovpayCustomConfig getInstance() {
		return instance;
	}

	public static GovpayCustomConfig newInstance() throws Exception {
		instance = new GovpayCustomConfig();
		return instance;
	}

	private Properties[] props;
	private String resourceDir;
	private CustomIuv defaultCustomIuvGenerator = null;
	
	
	public GovpayCustomConfig() throws Exception {
		
		Logger log = LoggerWrapperFactory.getLogger("boot");
		
		// Recupero il property all'interno dell'EAR
		InputStream is = GovpayCustomConfig.class.getResourceAsStream(PROPERTIES_FILE);

		props = new Properties[2];
		Properties props1 = new Properties();
		props1.load(is);
		props[1] = props1;
		
		// Recupero la configurazione della working dir
		// Se e' configurata, la uso come prioritaria

		try {
			this.resourceDir = getProperty("it.govpay.resource.path", props1, false, false);

			if(this.resourceDir != null) {
				File resourceDirFile = new File(this.resourceDir);
				if(!resourceDirFile.isDirectory())
					throw new Exception("Il path indicato nella property \"it.govpay.resource.path\" (" + this.resourceDir + ") non esiste o non e' un folder.");
			}
		} catch (Exception e) {
			LoggerWrapperFactory.getLogger("boot").warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
		}
		
		Properties props0 = null;
		props[0] = props0;

		File gpConfigFile = new File(this.resourceDir + File.separatorChar + "govpay-orm.properties");
		if(gpConfigFile.exists()) {
			props0 = new Properties();
			props0.load(new FileInputStream(gpConfigFile));
			log.info("Individuata configurazione prioritaria: " + gpConfigFile.getAbsolutePath());
			props[0] = props0;
		}
		
		String defaultCustomIuvGeneratorClass = getProperty("it.govpay.defaultCustomIuvGenerator.class", props, false);
			if(defaultCustomIuvGeneratorClass != null && !defaultCustomIuvGeneratorClass.isEmpty()) {
			Class<?> c = null;
			try {
				c = this.getClass().getClassLoader().loadClass(defaultCustomIuvGeneratorClass);
			} catch (ClassNotFoundException e) {
				throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] specificata come default per la generazione di IUV custom non e' presente nel classpath");
			}
			Object instance = c.newInstance();
			if(!(instance instanceof CustomIuv)) {
				throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] come default per la generazione di IUV custom deve implementare l'interfaccia " + CustomIuv.class.getName());
			}
			this.defaultCustomIuvGenerator = (CustomIuv) instance;
		}
		
	}

	private String getProperty(String name, Properties props, boolean required, boolean logDebug) throws Exception {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		
		String value = System.getProperty(name);

		if(value != null && value.trim().isEmpty()) {
			value = null;
		}

		if(value == null) {
			if(props != null) {
				value = props.getProperty(name);
				if(value != null && value.trim().isEmpty()) {
					value = null;
				}
			}
			if(value == null) {
				if(required) 
					throw new Exception("Proprieta ["+name+"] non trovata");
				else return null;
			} else {
				if(logDebug)
					log.debug("Letta proprieta di configurazione " + name + ": " + value);
				else
					log.info("Letta proprieta di configurazione " + name + ": " + value);
			}
		} else {
			if(logDebug)
				log.debug("Letta proprieta di configurazione " + name + ": " + value);
			else
				log.info("Letta proprieta di configurazione " + name + ": " + value);
		}

		return value.trim();
	}

	private String getProperty(String name, Properties[] props, boolean required) throws Exception {
		return getProperty(name, props, required, false);
	}

	private String getProperty(String name, Properties[] props, boolean required, boolean logDebug) throws Exception {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		
		String value = null;
		for(Properties p : props) {
			try { value = getProperty(name, p, required, logDebug); } catch (Exception e) { }
			if(value != null && !value.trim().isEmpty()) {
				return value;
			}
		}

		if(log != null && !logDebug) log.info("Proprieta " + name + " non trovata");
		if(log != null && logDebug) log.debug("Proprieta " + name + " non trovata");

		if(required) 
			throw new Exception("Proprieta ["+name+"] non trovata");
		else 
			return null;
	}
	
	public CustomIuv getDefaultCustomIuvGenerator() {
		return defaultCustomIuvGenerator;
	} 

}
