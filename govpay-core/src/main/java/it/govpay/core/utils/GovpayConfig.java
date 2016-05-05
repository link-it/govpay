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
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private String logoDir;
	private VersioneAvviso versioneAvviso;
	private int dimensionePool;
	private String ksLocation, ksPassword, ksAlias;
	
	
	public GovpayConfig() {
		// Default values:
		this.logoDir = null;
		this.versioneAvviso = VersioneAvviso.v002;
		this.dimensionePool = 10;
		this.log4j2Config = null;
		this.ksAlias = null;
		this.ksLocation = null;
		this.ksPassword = null;
		
		try {
			InputStream is = GovpayConfig.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(is);
			
			try {
				String resourceDir = getProperty("it.govpay.resource.path", props, false);
				if(resourceDir != null) {
					File resourceDirFile = new File(resourceDir);
					if(!resourceDirFile.isDirectory())
						throw new Exception("Il path indicato nella property \"it.govpay.resource.path\" (" + resourceDir + ") non esiste o non e' un folder.");
					
					File log4j2ConfigFile = new File(resourceDir + File.separatorChar + "log4j2.xml");
					
					if(log4j2ConfigFile.exists()) {
						this.log4j2Config = log4j2ConfigFile.toURI();
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
			
		} catch (Exception e) {
			log.warn("Errore di inizializzazione " + e.getMessage() + ". Impostati valori di default."); 
		}
	}

	private static String getProperty(String name, Properties props, boolean required) throws Exception {
		String value = props.getProperty(name);
		if(value == null) {
			if(required)
				throw new Exception("Property ["+name+"] non trovata");
			else return null;
		}
		
		return value.trim();
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

}
