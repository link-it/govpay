package it.govpay.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class ConsoleProperties {

	private static final String PROPERTIES_FILE = "/govpayConsole.properties";

	private static ConsoleProperties instance;
	
	private static Logger log = LogManager.getLogger();
	
	private int numeroRisultatiPerPagina;
	private boolean nascondiRicerca;
	
	private String urlDARS;
	
	private String dominioOperazioniJMX;
	private String tipoOperazioniJMX;
	private String nomeRisorsaOperazioniJMX;
	private String[] operazioniJMXDisponibili;
	private URI log4j2Config;
	private String pathEstrattoContoPdfLoghi;
	
	public static ConsoleProperties getInstance() {
		if(instance == null)
			init();
		
		return instance;
	}
	
	private static synchronized void init(){
		if(instance == null)
			instance = new ConsoleProperties();
	}
	
	public ConsoleProperties() {
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
				String resourceDir = getProperty("it.govpay.console.resource.path", props1, false);
				
				if(resourceDir != null) {
					File resourceDirFile = new File(resourceDir);
					if(!resourceDirFile.isDirectory())
						throw new Exception("Il path indicato nella property \"it.govpay.console.resource.path\" (" + resourceDir + ") non esiste o non e' un folder.");

					File log4j2ConfigFile = new File(resourceDir + File.separatorChar + "log4j2.xml");

					if(log4j2ConfigFile.exists()) {
						log.info("Caricata configurazione logger: " + log4j2ConfigFile.getAbsolutePath());
						this.log4j2Config = log4j2ConfigFile.toURI();
					}
					
					File gpConfigFile = new File(resourceDir + File.separatorChar + "govpayConsole.properties");
					if(gpConfigFile.exists()) {
						props0 = new Properties();
						props0.load(new FileInputStream(gpConfigFile));
						props[0] = props0;
						log.info("Individuata configurazione prioritaria: " + gpConfigFile.getAbsolutePath());
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
			}
			
			String nascondiRicercaString = ConsoleProperties.getProperty("it.govpay.console.nascondiRicerca", props, false);
			this.nascondiRicerca = Boolean.parseBoolean(nascondiRicercaString);
			
			String num = ConsoleProperties.getProperty("it.govpay.console.numeroRisultatiPerPagina", props, false);
			this.numeroRisultatiPerPagina = num != null ? Integer.parseInt(num) : 25;
			
			this.urlDARS = ConsoleProperties.getProperty("it.govpay.dars.url", props, false);
			
			this.dominioOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.dominio", props, false);
			this.tipoOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.tipo", props, false);
			this.nomeRisorsaOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.nomeRisorsa", props, false);
			String operazioniAsString = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.operazioniDisponibili", props, false);
			if(StringUtils.isNotEmpty(operazioniAsString))
				this.operazioniJMXDisponibili = operazioniAsString.split(",");
			
			this.pathEstrattoContoPdfLoghi = ConsoleProperties.getProperty("it.govpay.console.pdf.pathLoghi", props, false);
			
		} catch (Exception e) {
			log.warn("Errore di inizializzazione " + e.getMessage() + ". Impostati valori di default."); 
		}
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

	public int getNumeroRisultatiPerPagina() {
		return this.numeroRisultatiPerPagina;
	}

	public boolean isNascondiRicerca() {
		return this.nascondiRicerca;
	}

	public String getUrlDARS() {
		return this.urlDARS;
	}

	public String getDominioOperazioniJMX() {
		return dominioOperazioniJMX;
	}

	public String getTipoOperazioniJMX() {
		return tipoOperazioniJMX;
	}

	public String getNomeRisorsaOperazioniJMX() {
		return nomeRisorsaOperazioniJMX;
	}

	public String[] getOperazioniJMXDisponibili() {
		return operazioniJMXDisponibili;
	}
	
	public URI getLog4j2Config() {
		return log4j2Config;
	}

	public String getPathEstrattoContoPdfLoghi() {
		return pathEstrattoContoPdfLoghi;
	}

}
