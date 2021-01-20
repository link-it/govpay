package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.BaseExceptionV1.CategoriaEnum;

public class SeveritaProperties {

	private transient Logger log = null;
	
	public static final String MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME = "erroriSeverita.properties";
	public static final String MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE = "/" + MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE_NAME;
	private static SeveritaProperties instance;
	private Properties[] props;
	private String resourceDir;
	private Map<String, String> mappingSeveritaErrori = null;
	

	public static SeveritaProperties getInstance() {
		return instance;
	}

	public static SeveritaProperties newInstance(InputStream is) throws Exception {
		instance = new SeveritaProperties(is);
		return instance;
	}

	public SeveritaProperties(InputStream is) throws Exception {
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
				props0.load(new FileInputStream(gpConfigFile));
				log.debug("Individuata configurazione prioritaria Mapping Severita Errori: " + gpConfigFile.getAbsolutePath());
				this.props[0] = props0;
			}

			this.mappingSeveritaErrori = getProperties(props, false, log);
			log.debug("Caricamento Mapping Severita Errori completato."); 
		} catch (Exception e) {
			throw e;
		}

	}
	
	private static Map<String,String> getProperties(Properties[] props, boolean required, Logger log) throws Exception {
		Map<String, String> valori = new HashMap<>();
		String value = null;
		for(int i=0; i<props.length; i++) {
			if(props[i] != null) {
				for (Object nameObj : props[i].keySet()) {
					String key = (String) nameObj;
					try { value = getProperty(key, props[i], required, i==1, log); } catch (Exception e) { }
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

	private static String getProperty(String name, Properties props, boolean required, boolean fromInternalConfig, Logger log) throws Exception {
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
				throw new Exception("TipoEvento ["+name+"] non trovata");
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
		
		try{
			severita = Integer.parseInt(severitaS);
		} catch(Throwable t) {
			this.log.error("Lettura del livello di severita' per la BaseException.CategoriaEnum '"+categoria+"' terminata con errore: "+t.getMessage(),t);
			severita = null;
		}
		
		return severita;
	}
	
}
