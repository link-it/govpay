/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

import it.govpay.core.exceptions.ConfigException;

public class CSVSerializerProperties {
	
	private static final String PROPERTIES_PATH = "/csv_serializer.properties";

	
	private static CSVSerializerProperties instance;
	private Properties reader;
	
	private CSVSerializerProperties(Logger log) throws ConfigException, IOException {
		this.reader = new Properties();
		java.io.InputStream properties = null;
		try{  
			properties = CSVSerializerProperties.class.getResourceAsStream(CSVSerializerProperties.PROPERTIES_PATH);
			if(properties==null){
				throw new ConfigException("Properties "+CSVSerializerProperties.PROPERTIES_PATH+" not found");
			}
			this.reader.load(properties);
			properties.close();
		}catch(IOException e) {
			LogUtils.logError(log, "Riscontrato errore durante la lettura del file '"+CSVSerializerProperties.PROPERTIES_PATH+"': "+e.getMessage(),e);
			try{
				properties.close();
			}catch(Exception er){
				//donothing
			}
			throw e;
		}	
	}

	
	
	private static synchronized void initialize(org.slf4j.Logger log) throws ConfigException, IOException {

		if(CSVSerializerProperties.instance==null)
			CSVSerializerProperties.instance = new CSVSerializerProperties(log);	

	}

	public static synchronized CSVSerializerProperties getInstance(org.slf4j.Logger log) throws ConfigException, IOException {

		if(CSVSerializerProperties.instance==null)
			CSVSerializerProperties.initialize(log);

		return CSVSerializerProperties.instance;
	}

	
	public Properties getProperties(){
		return this.reader;
	}
}
