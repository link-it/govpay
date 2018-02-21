package it.govpay.core.utils.tracciati;

import java.util.Properties;

import org.slf4j.Logger;

public class CSVReaderProperties {
	
	private transient Logger log = null;
	
	private static final String propertiesPath = "/csv_reader.properties";

	
	private static CSVReaderProperties instance;
	private Properties reader;
	
	public CSVReaderProperties(Logger log) throws Exception{
		this.log = log;
		
		this.reader = new Properties();
		java.io.InputStream properties = null;
		try{  
			properties = CSVReaderProperties.class.getResourceAsStream(CSVReaderProperties.propertiesPath);
			if(properties==null){
				throw new Exception("Properties "+CSVReaderProperties.propertiesPath+" not found");
			}
			this.reader.load(properties);
			properties.close();
		}catch(java.io.IOException e) {
			this.log.error("Riscontrato errore durante la lettura del file '"+CSVReaderProperties.propertiesPath+"': "+e.getMessage(),e);
			try{
				if(properties!=null)
					properties.close();
			}catch(Exception er){}
			throw e;
		}	
	}

	
	
	private static synchronized void initialize(org.slf4j.Logger log) throws Exception{

		if(CSVReaderProperties.instance==null)
			CSVReaderProperties.instance = new CSVReaderProperties(log);	

	}

	public static CSVReaderProperties getInstance(org.slf4j.Logger log) throws Exception{

		if(CSVReaderProperties.instance==null)
			CSVReaderProperties.initialize(log);

		return CSVReaderProperties.instance;
	}

	
	public Properties getProperties(){
		return this.reader;
	}
}
