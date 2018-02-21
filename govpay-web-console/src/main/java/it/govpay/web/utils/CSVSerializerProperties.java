package it.govpay.web.utils;

import java.util.Properties;

import org.slf4j.Logger;

public class CSVSerializerProperties {
	
	private transient Logger log = null;
	
	private static final String propertiesPath = "/csv_serializer.properties";

	
	private static CSVSerializerProperties instance;
	private Properties reader;
	
	public CSVSerializerProperties(Logger log) throws Exception{
		this.log = log;
		
		this.reader = new Properties();
		java.io.InputStream properties = null;
		try{  
			properties = CSVSerializerProperties.class.getResourceAsStream(CSVSerializerProperties.propertiesPath);
			if(properties==null){
				throw new Exception("Properties "+CSVSerializerProperties.propertiesPath+" not found");
			}
			this.reader.load(properties);
			properties.close();
		}catch(java.io.IOException e) {
			this.log.error("Riscontrato errore durante la lettura del file '"+CSVSerializerProperties.propertiesPath+"': "+e.getMessage(),e);
			try{
				if(properties!=null)
					properties.close();
			}catch(Exception er){}
			throw e;
		}	
	}

	
	
	private static synchronized void initialize(org.slf4j.Logger log) throws Exception{

		if(CSVSerializerProperties.instance==null)
			CSVSerializerProperties.instance = new CSVSerializerProperties(log);	

	}

	public static CSVSerializerProperties getInstance(org.slf4j.Logger log) throws Exception{

		if(CSVSerializerProperties.instance==null)
			CSVSerializerProperties.initialize(log);

		return CSVSerializerProperties.instance;
	}

	
	public Properties getProperties(){
		return this.reader;
	}
}
