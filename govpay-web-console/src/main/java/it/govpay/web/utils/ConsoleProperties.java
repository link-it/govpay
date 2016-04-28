package it.govpay.web.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleProperties {

	private static final String PROPERTIES_FILE = "/govpayConsole.properties";

	private static ConsoleProperties instance;
	
	private static Logger log = LogManager.getLogger();
	
	private int numeroRisultatiPerPagina;
	private boolean nascondiRicerca;
	
	private String urlDARS;
	
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
			InputStream is = ConsoleProperties.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(is);
			
			String nascondiRicercaString = this.getProperty("it.govpay.console.nascondiRicerca", props, false);
			this.nascondiRicerca = Boolean.parseBoolean(nascondiRicercaString);
			
			String num = this.getProperty("it.govpay.console.numeroRisultatiPerPagina", props, false);
			this.numeroRisultatiPerPagina = num != null ? Integer.parseInt(num) : 25;
			
			this.urlDARS = this.getProperty("it.govpay.dars.url", props, false);
			
		} catch (Exception e) {
			log.warn("Errore di inizializzazione " + e.getMessage() + ". Impostati valori di default."); 
		}
	}
	
	private String getProperty(String name, Properties props, boolean required) throws Exception {
		String value = props.getProperty(name);
		if(value == null) {
			if(required)
				throw new Exception("Property ["+name+"] non trovata");
			else return null;
		}
		
		return value.trim();
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
	
}
