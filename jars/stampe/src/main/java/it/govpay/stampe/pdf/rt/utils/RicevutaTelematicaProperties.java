package it.govpay.stampe.pdf.rt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.Utilities;
import org.slf4j.Logger;

public class RicevutaTelematicaProperties {
	
	private static final String PROPERTIES_FILE = "/ricevutaTelematica.properties";
	public static final String DEFAULT_PROPS = "default";

	private static RicevutaTelematicaProperties instance;

	private static Logger log = LoggerWrapperFactory.getLogger(RicevutaTelematicaProperties.class);

	private String govpayResourceDir = null;

	private Map<String, Properties> propMap = new HashMap<>();

	public static RicevutaTelematicaProperties getInstance() {
		return instance;
	}

	public static synchronized RicevutaTelematicaProperties newInstance(String govpayResourceDir) throws Exception {
		if(instance == null)
			instance = new RicevutaTelematicaProperties(govpayResourceDir);
		return instance;
	}

	private Properties[] props  = null;

	public RicevutaTelematicaProperties(String govpayResourceDir) {
		try {

			// Recupero il property all'interno dell'EAR/WAR
			InputStream is = RicevutaTelematicaProperties.class.getResourceAsStream(PROPERTIES_FILE);
			Properties props1 = new Properties();
			props1.load(is);

			this.propMap.put(DEFAULT_PROPS, props1);

			Properties props0 = null;

			this.props = new Properties[2];
			this.props[0] = props0;
			this.props[1] = props1;

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
						this.propMap.put(DEFAULT_PROPS, props0);
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
					if(!f.getName().startsWith("ricevutaTelematica") && !f.getName().endsWith("properties")) {
						// Non e' un file di properties. lo salto
						continue;
					}
					Properties p = new Properties();
					p.load(new FileInputStream(f));
					String key = f.getName().replaceAll(".properties", "");
					key = key.replaceAll("ricevutaTelematica.", "");
					// la configurazione di defaut e' gia'stata caricata
					if(!key.equals("ricevutaTelematica")) {
						log.info("Caricata configurazione avviso di pagamento con chiave " + key);
						this.propMap.put(key, p);
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
		Properties p = this.getProperties(idprops);

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
		String property = this.getProperty(idprop, name, false);
		return property != null ? property : "";
	}

	public Properties getProperties(String id) throws Exception {
		if(id == null) id = DEFAULT_PROPS;
		Properties p = this.propMap.get(id);

		if(p == null) {
			log.debug("Configurazione ["+id+"] non trovata");
			throw new Exception("Configurazione ["+id+"] non trovata");
		}

		return p;
	}

	public Properties getProperties(Properties props, String prefix) throws Exception {
		Properties toRet = Utilities.readProperties(prefix+".", props);
		return toRet;
	}


	public TreeMap<String, String> getPropertiesAsMap(Properties props, String prefix) throws Exception {
		TreeMap<String, String> mappaProperties = new TreeMap<>();

		Properties p = this.getProperties(props, prefix);

		for (Object pKeyObj: p.keySet()) {
			Object pValObj = p.get(pKeyObj);
			mappaProperties.put((String)pKeyObj, (String)pValObj);
		}

		return mappaProperties; 
	}

	public Properties getPropertiesPerDominio(String codDominio,Logger log) throws Exception {
		return this.getPropertiesPerDominioTributo(codDominio, null, log);
	}

	public Properties getPropertiesPerDominioTributo(String codDominio,String codTributo,Logger log) throws Exception {
		Properties p = null;
		String key = null;
	
		// 1. ricerca delle properties per la chiave "codDominio.codTributo";
		if(StringUtils.isNotEmpty(codTributo) && StringUtils.isNotEmpty(codDominio)) {
			key = codDominio + "." + codTributo;
			try{
				log.debug("Ricerca delle properties per la chiave ["+key+"]");
				p = this.getProperties(key);
			}catch(Exception e){
				log.debug("Non sono state trovate properties per la chiave ["+key+"]: " + e.getMessage());
			}
		}

		// 2 . ricerca per codDominio
		if(StringUtils.isNotEmpty(codDominio)) {
			if(p == null){
				key = codDominio;
				try{
					log.debug("Ricerca delle properties per la chiave ["+key+"]");
					p = this.getProperties(key);
				}catch(Exception e){
					log.debug("Non sono state trovate properties per la chiave ["+key+"]: " + e.getMessage());
				}
			}
		}

		// utilizzo le properties di default
		try{
			log.debug("Ricerca delle properties di default");
			p = this.getProperties(null);
		}catch(Exception e){
			log.debug("Non sono state trovate properties di default: " + e.getMessage());
			throw e;
		}

		return p;
	}
}
