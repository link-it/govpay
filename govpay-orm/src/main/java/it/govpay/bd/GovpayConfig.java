/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.bd;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.generic_project.exception.ServiceException;

public class GovpayConfig {
	
	private static final String PROPERTIES_FILE = "/govpay-orm.properties";

	private static GovpayConfig instance;
	public static GovpayConfig getInstance() throws Exception {
		if(instance == null) {
			instance = new GovpayConfig();
		}
		return instance;
	}
	
	private String databaseType;
	private boolean databaseShowSql;
	private String dataSourceJNDIName;
	private String dataSourceAppName;
	private Map<String, String> nativeQueries;
	
	public GovpayConfig() throws Exception {
		InputStream is = GovpayConfig.class.getResourceAsStream(PROPERTIES_FILE);
		Properties props = new Properties();
		props.load(is);
		
		this.databaseType = getProperty("it.govpay.orm.databaseType", props, true);
		String databaseShowSqlString = getProperty("it.govpay.orm.showSql", props, true);
		this.databaseShowSql = Boolean.parseBoolean(databaseShowSqlString);
		this.dataSourceJNDIName = getProperty("it.govpay.orm.dataSourceJNDIName", props, true);
		this.dataSourceAppName = getProperty("it.govpay.orm.dataSourceAppName", props, true);

		String nativeQueriesList = getProperty("it.govpay.orm.nativeQuery.list", props, false);

		this.nativeQueries = new HashMap<String,String>();

		if(nativeQueriesList != null && !nativeQueriesList.isEmpty()) {
			String[] nativeQueriesSplit = nativeQueriesList.split(",");
			for(String nativeQueryProp: nativeQueriesSplit) {
				String nativeQuery = getProperty("it.govpay.orm.nativeQuery." +nativeQueryProp +"." +this.databaseType, props, false); //INIT solo le native queries per il tipo database correntemente usato
				if(nativeQuery != null && !nativeQuery.isEmpty()) {
					this.nativeQueries.put(nativeQueryProp, nativeQuery);
				}
			}
		}
		
	}

	private static String getProperty(String name, Properties props, boolean required) throws Exception {
		String value = props.getProperty(name);
		if(value == null) {
			if(required)
				throw new Exception("Property ["+name+"] non trovata");
			else return null;
		}
		
		return value.trim();
	}
	
	public String getNativeQuery(String nativeQueryKey) throws ServiceException {
		if(!this.nativeQueries.containsKey(nativeQueryKey)) {
			throw new ServiceException("Query nativa ["+nativeQueryKey+"] non trovata");
		}
		
		return this.nativeQueries.get(nativeQueryKey);
	}
	
	public String getDatabaseType() {
		return databaseType;
	}

	public boolean isDatabaseShowSql() {
		return databaseShowSql;
	}

	public String getDataSourceJNDIName() {
		return dataSourceJNDIName;
	}

	public String getDataSourceAppName() {
		return dataSourceAppName;
	}
}
