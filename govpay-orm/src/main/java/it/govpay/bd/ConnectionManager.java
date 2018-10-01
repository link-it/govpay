/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.bd;

import java.sql.Connection;
import java.util.Properties;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsAlreadyExistsException;
import org.openspcoop2.utils.datasource.DataSource;
import org.openspcoop2.utils.datasource.DataSourceFactory;
import org.openspcoop2.utils.datasource.DataSourceParams;
import org.slf4j.Logger;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.exception.ServiceException;

public class ConnectionManager {

	private static Logger log;
	private static JDBCServiceManagerProperties jdbcProperties;
	private static JDBCServiceManagerProperties guiJdbcProperties;
	private static DataSource ds;
	private static boolean initialized = false;
	
	
	public static void initialize() throws Exception {
		if(initialized) return;
		
		ConnectionManager.log = LoggerWrapperFactory.getLogger(ConnectionManager.class);
		ConnectionManager.log.info("Init ConnectionManager");
		ConnectionManager.jdbcProperties = new JDBCServiceManagerProperties();
		ConnectionManager.jdbcProperties.setDatabaseType(GovpayConfig.getInstance().getDatabaseType());
		ConnectionManager.jdbcProperties.setShowSql(GovpayConfig.getInstance().isDatabaseShowSql());
		ConnectionManager.jdbcProperties.setAutomaticTransactionManagement(false);
		
		DataSourceParams dsParams = new DataSourceParams();
		dsParams.setBindJmx(true);
		dsParams.setWrapOriginalMethods(true); // per poter usare anche getConnection e getConnection(String,String)
		dsParams.setDatabaseType(ConnectionManager.jdbcProperties.getDatabase());
		
		
		dsParams.setApplicativeId(GovpayConfig.getInstance().getDataSourceAppName());
		dsParams.setJmxDomain("it.govpay.core.connection");
		dsParams.setJmxName(GovpayConfig.getInstance().getDataSourceAppName());
		
		try{
			ConnectionManager.ds = DataSourceFactory.newInstance(GovpayConfig.getInstance().getDataSourceJNDIName(), new Properties(), dsParams);
		} catch(Exception e) {
			 if(e instanceof UtilsAlreadyExistsException){
				 log.debug("DataSource [" + GovpayConfig.getInstance().getDataSourceJNDIName() +"] gia' inizializzato.");
			 }else{
				 log.error("DataSource [" + GovpayConfig.getInstance().getDataSourceJNDIName() +"] non presente, provo a cercarlo col seguente nome [java:/" + GovpayConfig.getInstance().getDataSourceJNDIName() + "]");
                 try {
                	 ConnectionManager.ds = DataSourceFactory.newInstance("java:/"+GovpayConfig.getInstance().getDataSourceJNDIName(), new Properties(), dsParams);    	 
                 }catch(Exception e2) {
                     if(e instanceof UtilsAlreadyExistsException){
                    	 log.debug("DataSource [java:/" + GovpayConfig.getInstance().getDataSourceJNDIName() +"] gia' inizializzato.");
	                 }else{
	                	 log.error("DataSource [java:/" + GovpayConfig.getInstance().getDataSourceJNDIName() +"] non presente.");
	                 }
                 }
			 }
		}
        try{
            if(ConnectionManager.ds==null){
            	log.debug("DataSource [" + GovpayConfig.getInstance().getDataSourceAppName() +"] getInstance in corso...");
                ConnectionManager.ds =  DataSourceFactory.getInstance(GovpayConfig.getInstance().getDataSourceAppName());
                log.debug("DataSource [" + GovpayConfig.getInstance().getDataSourceAppName() +"] getInstance completata.");
            }
	    }catch(Exception e){
            log.error(e.getMessage());//,e);
            throw e;
	    }
		
		ConnectionManager.log.info("Init ConnectionManager terminata");
		initialized = true;
	}
	
	public static void shutdown() throws Exception {
		if(!initialized) return;
		DataSourceFactory.closeResources();
		initialized = false;
	}
	
	public static Connection getConnection(String idTransaction, String idModulo) throws ServiceException {
		try {
			initialize();
			return ConnectionManager.ds.getConnection(idTransaction, idModulo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static DataSource getDataSource() throws ServiceException {
		try {
			return ConnectionManager.ds;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static JDBCServiceManagerProperties getJDBCServiceManagerProperties(){
		return ConnectionManager.jdbcProperties;
	}
	
	public static JDBCServiceManagerProperties getGuiJDBCServiceManagerProperties(){
		return ConnectionManager.guiJdbcProperties;
	}
}
