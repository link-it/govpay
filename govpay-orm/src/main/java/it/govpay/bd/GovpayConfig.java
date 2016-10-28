/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.pagamento.util.CustomIuv;

public class GovpayConfig {

	private static final String PROPERTIES_FILE = "/govpay-orm.properties";

	private static GovpayConfig instance;

	public static GovpayConfig getInstance() {
		return instance;
	}

	public static GovpayConfig newInstance() throws Exception {
		instance = new GovpayConfig();
		return instance;
	}

	private String databaseType;
	private boolean databaseShowSql;
	private String dataSourceJNDIName;
	private String dataSourceAppName;
	private Map<String, String> nativeQueries;
	private Properties[] props;
	private String resourceDir;
	private CustomIuv defaultCustomIuvGenerator;

	public GovpayConfig() throws Exception {

		Logger log = LogManager.getLogger("boot");

		// Recupero il property all'interno dell'EAR
		InputStream is = GovpayConfig.class.getResourceAsStream(PROPERTIES_FILE);

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
			LogManager.getLogger("boot").warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
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

		this.databaseType = getProperty("it.govpay.orm.databaseType", props, true);
		String databaseShowSqlString = getProperty("it.govpay.orm.showSql", props, true);
		this.databaseShowSql = Boolean.parseBoolean(databaseShowSqlString);
		this.dataSourceJNDIName = getProperty("it.govpay.orm.dataSourceJNDIName", props, true);
		this.dataSourceAppName = getProperty("it.govpay.orm.dataSourceAppName", props, true);

		String nativeQueriesList = getProperty("it.govpay.orm.nativeQuery.list", props, false);

		this.nativeQueries = new HashMap<String,String>();

		if(nativeQueriesList != null && !nativeQueriesList.isEmpty()) {
			String[] nativeQueriesSplit = nativeQueriesList.split(",");
			for(String nativeQueryProp: nativeQueriesSplit) {
				String nativeQuery = getProperty("it.govpay.orm.nativeQuery." +nativeQueryProp +"." +this.databaseType, props, false, true); //INIT solo le native queries per il tipo database correntemente usato
				if(nativeQuery != null && !nativeQuery.isEmpty()) {
					this.nativeQueries.put(nativeQueryProp, nativeQuery);
				}
			}
		}

		String defaultCustomIuvGeneratorClass = getProperty("it.govpay.defaultCustomIuvGenerator.class", props, false);
		if(defaultCustomIuvGeneratorClass != null && !defaultCustomIuvGeneratorClass.isEmpty()) {
			Class<?> c = null;
			try {
				c = this.getClass().getClassLoader().loadClass(defaultCustomIuvGeneratorClass);
			} catch (ClassNotFoundException e) {
				throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] specificata per la gestione di IUV non e' presente nel classpath");
			}
			Object instance = c.newInstance();
			if(!(instance instanceof CustomIuv)) {
				throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] specificata per la gestione di IUV deve estendere la classe " + CustomIuv.class.getName());
			}
			this.defaultCustomIuvGenerator = (CustomIuv) instance;
		} else {
			this.defaultCustomIuvGenerator = new CustomIuv();
		}

	}

	private String getProperty(String name, Properties props, boolean required, boolean logDebug) throws Exception {
		Logger log = LogManager.getLogger("boot");

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
		Logger log = LogManager.getLogger("boot");

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

	public String getNativeQuery(String nativeQueryKey) throws ServiceException {
		if(!this.nativeQueries.containsKey(nativeQueryKey)) {
			throw new ServiceException("Query nativa ["+nativeQueryKey+"] non trovata");
		}

		return this.nativeQueries.get(nativeQueryKey);
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public boolean isDatabaseShowSql() {
		return databaseShowSql;
	}

	public String getDataSourceJNDIName() {
		return dataSourceJNDIName;
	}

	public String getDataSourceAppName() {
		return dataSourceAppName;
	}

	public CustomIuv getDefaultCustomIuvGenerator() {
		return defaultCustomIuvGenerator;
	} 

}
