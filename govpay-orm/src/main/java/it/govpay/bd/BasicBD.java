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

import it.govpay.orm.dao.jdbc.JDBCServiceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

public class BasicBD {
	
	private JDBCServiceManager serviceManager;
	private Connection connection;
	private String token;
	private boolean isClosed;
	private static Logger log = Logger.getLogger(JDBCServiceManager.class);
	
	BasicBD father;
	
	public BasicBD(BasicBD basicBD) {
		father = basicBD;
	}
	
	public static BasicBD getInstance() throws ServiceException {
		return new BasicBD();
	}
	
	private BasicBD() throws ServiceException {
		this.isClosed = true;
		this.setupConnection();
	}
	
	public void setupConnection() throws ServiceException {
		if(father != null) {
			father.setupConnection();
			return;
		}
		if(isClosed) {
			this.connection = ConnectionManager.getConnection();
			this.serviceManager = new JDBCServiceManager(this.connection, ConnectionManager.getJDBCServiceManagerProperties(), log);
			this.isClosed = false;
			if(log.getLevel().equals(Level.TRACE)) {
				token = UUID.randomUUID().toString();
				for(StackTraceElement ele : Thread.currentThread().getStackTrace()){
					if(ele.getClassName().contains("it.govpay.batch")
							|| ele.getClassName().contains("it.govpay.business") 
							|| ele.getClassName().contains("it.govpay.exception") 
							|| ele.getClassName().contains("it.govpay.plugin") 
							|| ele.getClassName().contains("it.govpay.thread") 
							|| ele.getClassName().contains("it.govpay.utils") 
							|| ele.getClassName().contains("it.govpay.web")) {
						log.trace("**" + token + "**: OPEN DB CONNECTION " + ele.getClassName() + ":" + ele.getLineNumber());
						break;
					}
				}
			}
		}
	}

	
	public JDBCServiceManager getServiceManager() throws ServiceException{
		if(father != null) {
			return father.getServiceManager();
		}
		return this.serviceManager;
	}


	public void setAutoCommit(boolean autoCommit) throws ServiceException {
		if(father != null) {
			father.setAutoCommit(autoCommit);
			return;
		}
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			log.error("Errore durante l'impostazione dell'AutoCommit a " + autoCommit, e);
			throw new ServiceException("Errore durante la gestione dell'AutoCommit della connesione.");
		}
	}
	
	public void closeConnection() {
		if(father != null) {
			father.closeConnection();
			return;
		}
		
		try {
			if(this.connection != null && !isClosed) {
				this.connection.close();
				isClosed = true;
				if(log.getLevel().equals(Level.TRACE)) {
					boolean logged = false;
					for(StackTraceElement ele : Thread.currentThread().getStackTrace()){
						if(ele.getClassName().contains("it.govpay.batch")
								|| ele.getClassName().contains("it.govpay.business") 
								|| ele.getClassName().contains("it.govpay.exception") 
								|| ele.getClassName().contains("it.govpay.plugin") 
								|| ele.getClassName().contains("it.govpay.thread") 
								|| ele.getClassName().contains("it.govpay.utils") 
								|| ele.getClassName().contains("it.govpay.web")) {
							log.trace("**" + token + "**: CLOSE DB CONNECTION " + ele.getClassName() + ":" + ele.getLineNumber());
							logged = true;
							break;
						}
					}
					if(!logged) log.trace(token + " CLOSE DB CONNECTION ");
				}
			}
		} catch (SQLException e) {
			log.error("Errore durante la chiusura della connessione.", e);
		}
	}
	
	public void commit() throws ServiceException{
		if(father != null) {
			father.commit();
			return;
		}
		
		try {
			this.connection.commit();
		} catch (SQLException e) {
			log.error("Errore durante la commit.", e);
			throw new ServiceException("Errore durante la gestione della commit della connesione.", e);
		}
	}
	
	public void rollback() {
		if(father != null) {
			father.rollback();
			return;
		}
		
		try {
			if(this.connection != null && !this.connection.getAutoCommit() && !isClosed)
				this.connection.rollback();
		} catch (SQLException e) {
			log.error("Errore durante la rollback.", e);
		}
	}

	public Connection getConnection() {
		if(father != null) {
			return father.getConnection();
		}
		return this.connection;
	}
	
}
