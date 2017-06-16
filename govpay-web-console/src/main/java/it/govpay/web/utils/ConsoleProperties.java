package it.govpay.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private int numeroMassimoElementiExport;
	private boolean nascondiRicerca;
	
	private String dominioOperazioniJMX, tipoOperazioniJMX, nomeRisorsaOperazioniJMX, asJMX, usernameJMX, passwordJMX, factoryJMX;
	private String[] operazioniJMXDisponibili;
	private Map<String, String> urlJMX;
	
	private URI log4j2Config;
	private String pathEstrattoContoPdfLoghi;
	private String resourceDir;
	
	private String urlEstrattoConto, usernameEstrattoConto, passwordEstrattoConto;
	
	private Integer sogliaGiorniRitardoPagamenti = null;
	
	private List<String> localeAbilitati= null;
	
	private Long dimensioneMassimaFileTracciato =0L;
	
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
				this.resourceDir = getProperty("it.govpay.console.resource.path", props1, false);
				
				if(this.resourceDir != null) {
					File resourceDirFile = new File(this.resourceDir);
					if(!resourceDirFile.isDirectory())
						throw new Exception("Il path indicato nella property \"it.govpay.console.resource.path\" (" + this.resourceDir + ") non esiste o non e' un folder.");

					File log4j2ConfigFile = new File(this.resourceDir + File.separatorChar + "log4j2.xml");

					if(log4j2ConfigFile.exists()) {
						log.info("Caricata configurazione logger: " + log4j2ConfigFile.getAbsolutePath());
						this.log4j2Config = log4j2ConfigFile.toURI();
					}
					
					File gpConfigFile = new File(this.resourceDir + File.separatorChar + "govpayConsole.properties");
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
			
			this.dominioOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.dominio", props, false);
			this.tipoOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.tipo", props, false);
			this.nomeRisorsaOperazioniJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.nomeRisorsa", props, false);
			String operazioniAsString = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.operazioniDisponibili", props, false);
			if(StringUtils.isNotEmpty(operazioniAsString))
				this.operazioniJMXDisponibili = operazioniAsString.split(",");
			
			this.urlJMX = new HashMap<String, String>();
			String nodiJMXAsString = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.nodi", props, true);
			if(StringUtils.isNotEmpty(nodiJMXAsString)) {
				String[] nodi = nodiJMXAsString.split(",");
				for(String nodo : nodi) {
					this.urlJMX.put(nodo, ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.url."+nodo, props, true));
				}
			}
			
			this.usernameJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.username", props, false);
			this.passwordJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.password", props, false);
			this.asJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.as", props, true);
			this.factoryJMX = ConsoleProperties.getProperty("it.govpay.console.operazioni.jmx.factory", props, true);
			
			this.pathEstrattoContoPdfLoghi = ConsoleProperties.getProperty("it.govpay.console.pdf.pathLoghi", props, false);
			
			
			this.urlEstrattoConto = ConsoleProperties.getProperty("it.govpay.estrattoConto.url", props, false);
			this.usernameEstrattoConto = ConsoleProperties.getProperty("it.govpay.estrattoConto.username", props, false);
			this.passwordEstrattoConto = ConsoleProperties.getProperty("it.govpay.estrattoConto.password", props, false);
			
			String num2 = ConsoleProperties.getProperty("it.govpay.console.numeroMassimoElementiExport", props, false);
			this.numeroMassimoElementiExport = num2 != null ? Integer.parseInt(num2) : 25;
			
			String sogliaS = ConsoleProperties.getProperty("it.govpay.console.pagamenti.sogliaRitardoGiorni", props, false);
			this.sogliaGiorniRitardoPagamenti = sogliaS != null ? Integer.parseInt(sogliaS) : null;
			
			this.localeAbilitati = new ArrayList<String>();
			String localeAsString = ConsoleProperties.getProperty("it.govpay.console.lingueAbilitate", props, true);
			if(StringUtils.isNotEmpty(localeAsString)) {
				String[] lingue = localeAsString.split(","); 
				for(String lingua : lingue) {
					this.localeAbilitati.add(lingua);
				}
			}
			
			String dim = ConsoleProperties.getProperty("it.govpay.console.caricamentoTracciati.dimensioneMassimaFile", props, false);
			this.dimensioneMassimaFileTracciato = num != null ? Long.parseLong(dim) : 10485760;
						
		} catch (Exception e) {
			log.warn("Errore di inizializzazione " + e.getMessage() + ". Impostati valori di default."); 
		}
	}
	
	public String getAsJMX() {
		return this.asJMX;
	}

	public String getUsernameJMX() {
		return this.usernameJMX;
	}

	public String getPasswordJMX() {
		return this.passwordJMX;
	}

	public String getFactoryJMX() {
		return this.factoryJMX;
	}

	public Map<String, String> getUrlJMX() {
		return this.urlJMX;
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
				log.info("Letta proprieta di configurazione " + name + ": " + value);
			}
		} else {
			log.info("Letta proprieta di sistema " + name + ": " + value);
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
		
		log.info("Proprieta " + name + " non trovata");
		
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

	public String getDominioOperazioniJMX() {
		return this.dominioOperazioniJMX;
	}

	public String getTipoOperazioniJMX() {
		return this.tipoOperazioniJMX;
	}

	public String getNomeRisorsaOperazioniJMX() {
		return this.nomeRisorsaOperazioniJMX;
	}

	public String[] getOperazioniJMXDisponibili() {
		return this.operazioniJMXDisponibili;
	}
	
	public URI getLog4j2Config() {
		return this.log4j2Config;
	}

	public String getPathEstrattoContoPdfLoghi() {
		return this.pathEstrattoContoPdfLoghi;
	}

	public String getUrlEstrattoConto() {
		return this.urlEstrattoConto;
	}

	public String getUsernameEstrattoConto() {
		return this.usernameEstrattoConto;
	}

	public String getPasswordEstrattoConto() {
		return this.passwordEstrattoConto;
	}
	public String getResourceDir() {
		return this.resourceDir;
	}
	public int getNumeroMassimoElementiExport() {
		return this.numeroMassimoElementiExport;
	}
	public Integer getSogliaGiorniRitardoPagamenti() {
		return this.sogliaGiorniRitardoPagamenti;
	}

	public List<String> getLocaleAbilitati() {
		return localeAbilitati;
	}
	
	public Long getDimensioneMassimaFileTracciato() {
		return dimensioneMassimaFileTracciato;
	}
}
