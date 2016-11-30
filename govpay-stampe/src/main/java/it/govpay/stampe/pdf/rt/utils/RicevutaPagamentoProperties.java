package it.govpay.stampe.pdf.rt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.Utilities;

public class RicevutaPagamentoProperties {

public static final String RICEVUTA_PAGAMENTO_CLASSNAME_PROP_KEY = "ricevutaPagamento.className";
	
	private static final String PROPERTIES_FILE = "/ricevutaPagamento.properties";
	public static final String DEFAULT_PROPS = "default";

	private static RicevutaPagamentoProperties instance;

	private static Logger log = LogManager.getLogger();

	private String govpayResourceDir = null;

	private Map<String, Properties> propMap = new HashMap<String, Properties>();

	public static RicevutaPagamentoProperties getInstance() {
		return instance;
	}

	public static RicevutaPagamentoProperties newInstance(String govpayResourceDir) throws Exception {
		instance = new RicevutaPagamentoProperties(govpayResourceDir);
		return instance;
	}

	private Properties[] props  = null;

	public RicevutaPagamentoProperties(String govpayResourceDir) {
		try {

			// Recupero il property all'interno dell'EAR/WAR
			InputStream is = RicevutaPagamentoProperties.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props1 = new Properties();
			props1.load(is);

			propMap.put(DEFAULT_PROPS, props1);

			Properties props0 = null;

			props = new Properties[2];
			props[0] = props0;
			props[1] = props1;

			// Recupero la configurazione della working dir
			// Se e' configurata, la uso come prioritaria

			try {
				this.govpayResourceDir = govpayResourceDir;

				if(this.govpayResourceDir != null) {
					File resourceDirFile = new File(this.govpayResourceDir);
					if(!resourceDirFile.isDirectory())
						throw new Exception("Il path passato come paramtero (" + this.govpayResourceDir + ") non esiste o non e' un folder.");


					File gpConfigFile = new File(this.govpayResourceDir + PROPERTIES_FILE);
					if(gpConfigFile.exists()) {
						props0 = new Properties();
						props0.load(new FileInputStream(gpConfigFile));
						propMap.put(DEFAULT_PROPS, props0);
						log.info("Individuata configurazione prioritaria: " + gpConfigFile.getAbsolutePath());
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
			}

			// carico tutti i file che definiscono configurazioni diverse di avvisi pagamento
			if(this.govpayResourceDir != null) {
				File resourceDirFile = new File(this.govpayResourceDir);
				for(File f : resourceDirFile.listFiles()) {
					if(!(f.getName().startsWith("ricevutaPagamento") && f.getName().endsWith("properties"))) {
						// Non e' un file di properties. lo salto
						continue;
					}
					Properties p = new Properties();
					p.load(new FileInputStream(f));
					String key = f.getName().replaceAll(".properties", "");
					key = key.replaceAll("ricevutaPagamento.", "");
					// la configurazione di defaut e' gia'stata caricata
					if(!key.equals("ricevutaPagamento")) {
						log.info("Caricata configurazione avviso di pagamento con chiave " + key);
						propMap.put(key, p);
					}
				}
			}
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

	public String getProperty(String idprops, String name, boolean required) throws Exception {
		String value = null;
		Properties p = getProperties(idprops);

		try { value = getProperty(name, p, required); } catch (Exception e) { }
		if(value != null && !value.trim().isEmpty()) {
			return value;
		}

		log.debug("Proprieta " + name + " non trovata in configurazione ["+idprops+"]");

		if(required) 
			throw new Exception("Proprieta ["+name+"] non trovata in configurazione ["+idprops+"]");
		else 
			return null;
	}


	public String getPropertyEnte(String idprop, String name) throws Exception {
		String property = getProperty(idprop, name, false);
		return property != null ? property : "";
	}

	public Properties getProperties(String id) throws Exception {
		if(id == null) id = DEFAULT_PROPS;
		Properties p = propMap.get(id);

		if(p == null) {
			log.debug("Configurazione ["+id+"] non trovata");
			throw new Exception("Configurazione ["+id+"] non trovata");
		}

		return p;
	}
	
	public String getDefaultImplClassName() throws Exception{
		return getPropertyEnte(DEFAULT_PROPS, RICEVUTA_PAGAMENTO_CLASSNAME_PROP_KEY);
	}
	
	public Properties getProperties(Properties props, String prefix) throws Exception {
        Properties toRet = Utilities.readProperties(prefix+".", props);
        return toRet;
	}
	
	
	public TreeMap<String, String> getPropertiesAsMap(Properties props, String prefix) throws Exception {
		TreeMap<String, String> mappaProperties = new TreeMap<String, String>();
		
		Properties p = getProperties(props, prefix);
		
		for (Object pKeyObj: p.keySet()) {
			Object pValObj = p.get(pKeyObj);
			mappaProperties.put((String)pKeyObj, (String)pValObj);
		}
		
		return mappaProperties; 
	}
}
