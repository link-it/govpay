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
package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.logger.constants.Severity;
import org.openspcoop2.utils.logger.log4j.Log4JLoggerWithProxyContext;

public class GovpayConfig {

	public enum VersioneAvviso {
		v001, v002;
	}

	private static final String PROPERTIES_FILE = "/govpay.properties";

	private static GovpayConfig instance;

	private static Logger log = LogManager.getLogger();

	public static GovpayConfig getInstance() {
		return instance;
	}

	public static GovpayConfig newInstance() throws Exception {
		instance = new GovpayConfig();
		return instance;
	}

	private URI log4j2Config;
	private URL urlPddVerifica;
	private String logoDir;
	private VersioneAvviso versioneAvviso;
	private int dimensionePool;
	private String ksLocation, ksPassword, ksAlias;
	private String mLogClass, mLogDS;
	private Severity mLogLevel;
	private TipiDatabase mLogDBType;
	private boolean mLogOnLog4j, mLogOnDB, mLogSql;


	public GovpayConfig() {
		// Default values:
		this.logoDir = null;
		this.versioneAvviso = VersioneAvviso.v002;
		this.dimensionePool = 10;
		this.log4j2Config = null;
		this.urlPddVerifica = null;
		this.ksAlias = null;
		this.ksLocation = null;
		this.ksPassword = null;
		this.mLogClass = Log4JLoggerWithProxyContext.class.getName();
		this.mLogLevel = Severity.INFO;
		this.mLogOnDB = false;
		this.mLogOnLog4j = true;
		this.mLogSql = false;
		this.mLogDBType = null;
		this.mLogDS = null;
		
		
		try {
			
			// Recupero il property all'interno dell'EAR
			InputStream is = GovpayConfig.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props1 = new Properties();
			props1.load(is);
			
			Properties props0 = null;
			
			Properties[] props = new Properties[2];
			props[0] = props0;
			props[1] = props1;
			
			// Recupero la configurazione della working dir
			// Se e' configurata, la uso come prioritaria
			
			try {
				String resourceDir = getProperty("it.govpay.resource.path", props1, false);
				
				if(resourceDir != null) {
					File resourceDirFile = new File(resourceDir);
					if(!resourceDirFile.isDirectory())
						throw new Exception("Il path indicato nella property \"it.govpay.resource.path\" (" + resourceDir + ") non esiste o non e' un folder.");

					File log4j2ConfigFile = new File(resourceDir + File.separatorChar + "log4j2.xml");

					if(log4j2ConfigFile.exists()) {
						log.info("Caricata configurazione logger: " + log4j2ConfigFile.getAbsolutePath());
						this.log4j2Config = log4j2ConfigFile.toURI();
					}
					
					File gpConfigFile = new File(resourceDir + File.separatorChar + "govpay.properties");
					if(gpConfigFile.exists()) {
						props0 = new Properties();
						props0.load(new FileInputStream(gpConfigFile));
						log.info("Individuata configurazione prioritaria: " + gpConfigFile.getAbsolutePath());
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
			}

			try {
				this.logoDir = getProperty("it.govpay.psp.logo.path", props, false);
				if(this.logoDir != null) {
					File logoDirFile = new File(logoDir);
					if(!logoDirFile.isDirectory())
						throw new Exception("Il path indicato nella property \"it.govpay.psp.logo.path\" (" + logoDir + ") non esiste o non e' un folder.");
					File logoDirFile80 = new File(logoDir + "/80x40");
					if(!logoDirFile80.isDirectory())
						throw new Exception("Il folder indicato nella property \"it.govpay.psp.logo.path\" (" + logoDir + ") non contiene il subfolder (80x40).");
					File logoDirFile160 = new File(logoDir + "/160x80");
					if(!logoDirFile160.isDirectory())
						throw new Exception("Il folder indicato nella property \"it.govpay.psp.logo.path\" (" + logoDir + ") non contiene il subfolder (160x80).");
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
				this.logoDir = null;
			}

			try {
				String versioneAvvisoProperty = getProperty("it.govpay.avviso.versione", props, false);
				if(versioneAvvisoProperty != null && !versioneAvvisoProperty.trim().isEmpty()) {
					try {
						this.versioneAvviso = VersioneAvviso.valueOf(versioneAvvisoProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.avviso.versione\" non consetito [" + versioneAvvisoProperty + "]. Valori ammessi: " + Arrays.toString(VersioneAvviso.values()));
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + VersioneAvviso.v002);
				this.versioneAvviso = VersioneAvviso.v002;
			}

			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool", props, false);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePool = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + 10);
				this.dimensionePool = 10;
			}
			
			String urlPddVerificaProperty = getProperty("it.govpay.check.urlVerificaPDD", props, true);
			try {
				this.urlPddVerifica = new URL(urlPddVerificaProperty.trim());
			} catch (Exception e) {
				throw new Exception("Valore ["+urlPddVerificaProperty.trim()+"] non consentito per la property \"it.govpay.check.urlVerificaPDD\": " +e.getMessage());
			}
			
			String mLogClassString = getProperty("it.govpay.mlog.class", props, false);
			if(mLogClassString != null && !mLogClassString.isEmpty()) 
				this.mLogClass = mLogClassString;
			
			String mLogOnLog4jString = getProperty("it.govpay.mlog.log4j", props, false);
			if(mLogOnLog4jString != null && !Boolean.valueOf(mLogOnLog4jString))
				this.mLogOnLog4j = false;
			
			try {
				this.mLogLevel = Severity.valueOf(getProperty("it.govpay.mlog.level", props, false));
			} catch (Exception e) {
				log.warn("Valore ["+getProperty("it.govpay.mlog.level", props, false)+"] non consentito per la property \"it.govpay.mlog.level\". Assunto valore di default \"INFO\".");
			}
			
			String mLogOnDBString = getProperty("it.govpay.mlog.db", props, false);
			if(mLogOnDBString != null && Boolean.valueOf(mLogOnDBString))
				this.mLogOnDB = true;
			
			if(this.mLogOnDB) {
				String mLogDBTypeString = getProperty("it.govpay.mlog.db.type", props, true);
				try {
					this.mLogDBType = TipiDatabase.valueOf(mLogDBTypeString);
				} catch (IllegalArgumentException e) {
					throw new Exception("Valore ["+mLogDBTypeString.trim()+"] non consentito per la property \"it.govpay.mlog.db.type\": " +e.getMessage());
				}
				
				this.mLogDS = getProperty("it.govpay.mlog.db.ds", props, true);
				
				String mLogSqlString = getProperty("it.govpay.mlog.showSql", props, false);
				if(mLogSqlString != null)
					this.mLogOnLog4j = Boolean.valueOf(mLogSqlString);
			}
			
			
		} catch (Exception e) {
			log.warn("Errore di inizializzazione " + e.getMessage() + ". Impostati valori di default."); 
		}
	}

	public URL getUrlPddVerifica() {
		return urlPddVerifica;
	}

	private static String getProperty(String name, Properties props, boolean required) throws Exception {
		String value = System.getProperty(name);

		if(value == null) {
			if(props != null) value = props.getProperty(name);
			if(value == null) {
				if(required) 
					throw new Exception("Proprieta ["+name+"] non trovata");
				else return null;
			} else {
				log.debug("Letta proprieta di configurazione " + name + ": " + value);
			}
		} else {
			log.debug("Letta proprieta di sistema " + name + ": " + value);
		}

		return value.trim();
	}
	
	private static String getProperty(String name, Properties[] props, boolean required) throws Exception {
		String value = null;
		for(Properties p : props) {
			try { value = getProperty(name, p, required); } catch (Exception e) { }
			if(value != null && !value.trim().isEmpty()) {
				return value;
			}
		}
		
		log.debug("Proprieta " + name + " non trovata");
		
		if(required) 
			throw new Exception("Proprieta ["+name+"] non trovata");
		else 
			return null;
	}

	public String getLogoDir() {
		return logoDir;
	}

	public VersioneAvviso getVersioneAvviso() {
		return versioneAvviso;
	}

	public URI getLog4j2Config() {
		return log4j2Config;
	}

	public int getDimensionePool() {
		return dimensionePool;
	}

	public String getKsLocation() {
		return ksLocation;
	}

	public String getKsPassword() {
		return ksPassword;
	}

	public String getKsAlias() {
		return ksAlias;
	}

	public String getmLogClass() {
		return mLogClass;
	}

	public String getmLogDS() {
		return mLogDS;
	}

	public Severity getmLogLevel() {
		return mLogLevel;
	}

	public TipiDatabase getmLogDBType() {
		return mLogDBType;
	}

	public boolean ismLogOnLog4j() {
		return mLogOnLog4j;
	}

	public boolean ismLogOnDB() {
		return mLogOnDB;
	}

	public boolean ismLogSql() {
		return mLogSql;
	}
}
