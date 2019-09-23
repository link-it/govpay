package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

public class EventiUtils {

	public static final String MAPPING_TIPI_EVENTO_PROPERTIES_FILE = File.separatorChar + "mappingTipiEvento.properties";
	private static EventiUtils instance;
	private Properties[] props;
	private String resourceDir;
	private Map<String, String> mappingLabelTipoEvento = null;

	public static EventiUtils getInstance() {
		return instance;
	}

	public static EventiUtils newInstance(InputStream is) throws Exception {
		instance = new EventiUtils(is);
		return instance;
	}

	public EventiUtils(InputStream is) throws Exception {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		try {
			this.mappingLabelTipoEvento = new HashMap<>();

			log.debug("Caricamento Mapping Label TipoEvento..."); 
			this.resourceDir = GovpayConfig.getInstance().getResourceDir();
			// Recupero il property all'interno dell'EAR
			this.props = new Properties[2];
			Properties props1 = new Properties();
			props1.load(is);
			this.props[1] = props1;

			Properties props0 = null;
			this.props[0] = props0;

			File gpConfigFile = new File(this.resourceDir + MAPPING_TIPI_EVENTO_PROPERTIES_FILE);
			if(gpConfigFile.exists()) {
				props0 = new Properties();
				props0.load(new FileInputStream(gpConfigFile));
				log.debug("Individuata configurazione prioritaria Mapping Label TipoEvento: " + gpConfigFile.getAbsolutePath());
				this.props[0] = props0;
			}

			this.mappingLabelTipoEvento = getProperties(props, false, log);
			log.debug("Caricamento Mapping Label TipoEvento completato."); 
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

	public Map<String, String> getMappingLabelTipoEvento() {
		return mappingLabelTipoEvento;
	}
	
	
}
