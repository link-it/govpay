/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
