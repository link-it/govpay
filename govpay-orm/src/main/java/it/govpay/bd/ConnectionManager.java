/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
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
		
		ConnectionManager.log = Logger.getLogger(ConnectionManager.class);
		ConnectionManager.log.info("Init ConnectionManager");
		ConnectionManager.jdbcProperties = new JDBCServiceManagerProperties();
		ConnectionManager.jdbcProperties.setDatabaseType(GovpayConfig.getInstance().getDatabaseType());
		ConnectionManager.jdbcProperties.setShowSql(GovpayConfig.getInstance().isDatabaseShowSql());
		ConnectionManager.jdbcProperties.setAutomaticTransactionManagement(false);
		
		Context initialContext = new InitialContext();
		try{
			ConnectionManager.ds = (DataSource)initialContext.lookup(GovpayConfig.getInstance().getDataSourceJNDIName());
		} catch(NameNotFoundException e) {
			ConnectionManager.ds = (DataSource)initialContext.lookup("java:/"+GovpayConfig.getInstance().getDataSourceJNDIName());
		}

		ConnectionManager.log.info("Init ConnectionManager terminata");
		initialized = true;
	}
	
	public static Connection getConnection() throws ServiceException {
		try {
			initialize();
			return ConnectionManager.ds.getConnection();
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
