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

import it.govpay.orm.dao.IACLService;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.ICanaleService;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IDBACLService;
import it.govpay.orm.dao.IDBApplicazioneService;
import it.govpay.orm.dao.IDBCanaleService;
import it.govpay.orm.dao.IDBConnettoreService;
import it.govpay.orm.dao.IDBDominioService;
import it.govpay.orm.dao.IDBEventoService;
import it.govpay.orm.dao.IDBFRService;
import it.govpay.orm.dao.IDBIUVService;
import it.govpay.orm.dao.IDBIbanAccreditoService;
import it.govpay.orm.dao.IDBIntermediarioService;
import it.govpay.orm.dao.IDBNotificaService;
import it.govpay.orm.dao.IDBOperatoreService;
import it.govpay.orm.dao.IDBPagamentoService;
import it.govpay.orm.dao.IDBPortaleService;
import it.govpay.orm.dao.IDBPspService;
import it.govpay.orm.dao.IDBRPTService;
import it.govpay.orm.dao.IDBRRService;
import it.govpay.orm.dao.IDBRendicontazioneService;
import it.govpay.orm.dao.IDBSingoloVersamentoService;
import it.govpay.orm.dao.IDBStazioneService;
import it.govpay.orm.dao.IDBTipoTributoService;
import it.govpay.orm.dao.IDBTributoService;
import it.govpay.orm.dao.IDBUoService;
import it.govpay.orm.dao.IDBVersamentoService;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.INotificaService;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.IPortaleService;
import it.govpay.orm.dao.IPspService;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IRendicontazionePagamentoServiceSearch;
import it.govpay.orm.dao.IRendicontazioneService;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.ITipoTributoService;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.IUoService;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class BasicBD {
	
	private JDBCServiceManager serviceManager;
	private JDBCServiceManagerProperties jdbcProperties;
	
	private IApplicazioneService applicazioneService;
	private IACLService aclService;
	private ICanaleService canaleService;
	private IConnettoreService connettoreService;
	private IDominioService dominioService;
	private IEventoService eventoService;
	private IFRService frService;
	private IIbanAccreditoService ibanAccreditoService;
	private IIntermediarioService intermediarioService;
	private IIUVService iuvService;
	private INotificaService notificaService;
	private IOperatoreService operatoreService;
	private IPagamentoService pagamentoService;
	private IPortaleService portaleService;
	private IPspService pspService;
	private IRendicontazionePagamentoServiceSearch rendicontazionePagamentoServiceSearch;
	private IRendicontazioneService rendicontazioneService;
	private IRPTService rptService;
	private IRRService rrService;
	private ISingoloVersamentoService singoloVersamentoService;
	private IStazioneService stazioneService;
	private ITipoTributoService tipoTributoService;
	private ITributoService tributoService;
	private IUoService uoService;
	private IVersamentoService versamentoService;
	
	private String idTransaction;
	private String idModulo;
	
	private Connection connection;
	private boolean isClosed;
	private static Logger log = Logger.getLogger(JDBCServiceManager.class);
	
	BasicBD father;
	
	public BasicBD(BasicBD basicBD) {
		father = basicBD;
	}
	
	public static BasicBD newInstance(String idTransaction) throws ServiceException {
		return new BasicBD(idTransaction);
	}
	
	private BasicBD(String idTransaction) throws ServiceException {
		this.isClosed = true;
		this.idTransaction = idTransaction;
		this.idModulo = getCaller();
		this.setupConnection(idTransaction, idModulo);
	}
	
	public void setupConnection(String idTransaction) throws ServiceException {
		this.idModulo = getCaller();
		this.setupConnection(idTransaction, idModulo);
	}

	private void setupConnection(String idTransaction, String idModulo) throws ServiceException {
		if(father != null) {
			father.setupConnection(idTransaction);
			return;
		}
		if(isClosed) {
			this.connection = ConnectionManager.getConnection(idTransaction, idModulo);
			this.serviceManager = new JDBCServiceManager(this.connection, ConnectionManager.getJDBCServiceManagerProperties(), log);
			this.jdbcProperties = this.serviceManager.getJdbcProperties();
			
			try {
				this.applicazioneService = this.serviceManager.getApplicazioneService();
				this.aclService = this.serviceManager.getACLService();
				this.canaleService = this.serviceManager.getCanaleService();
				this.connettoreService = this.serviceManager.getConnettoreService();
				this.dominioService = this.serviceManager.getDominioService();
				this.eventoService = this.serviceManager.getEventoService();
				this.frService = this.serviceManager.getFRService();
				this.ibanAccreditoService = this.serviceManager.getIbanAccreditoService();
				this.intermediarioService = this.serviceManager.getIntermediarioService();
				this.iuvService = this.serviceManager.getIUVService();
				this.notificaService = this.serviceManager.getNotificaService();
				this.operatoreService = this.serviceManager.getOperatoreService();
				this.portaleService = this.serviceManager.getPortaleService();
				this.pagamentoService = this.serviceManager.getPagamentoService();
				this.pspService = this.serviceManager.getPspService();
				this.rendicontazionePagamentoServiceSearch = this.serviceManager.getRendicontazionePagamentoServiceSearch();
				this.rendicontazioneService = this.serviceManager.getRendicontazioneService();
				this.rptService = this.serviceManager.getRPTService();
				this.rrService = this.serviceManager.getRRService();
				this.singoloVersamentoService = this.serviceManager.getSingoloVersamentoService();
				this.stazioneService = this.serviceManager.getStazioneService();
				this.tipoTributoService = this.serviceManager.getTipoTributoService();
				this.tributoService = this.serviceManager.getTributoService();
				this.uoService = this.serviceManager.getUoService();
				this.versamentoService = this.serviceManager.getVersamentoService();
			} catch(NotImplementedException e) {
				throw new ServiceException(e);
			}
			this.isClosed = false;
		}
	}

	
	public void enableSelectForUpdate() throws ServiceException {
		if(this.father != null) {
			this.father.enableSelectForUpdate();
			return;
		}
		try {
			((IDBApplicazioneService)this.applicazioneService).enableSelectForUpdate();
			((IDBACLService)this.aclService).enableSelectForUpdate();
			((IDBCanaleService)this.canaleService).enableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).enableSelectForUpdate();
			((IDBDominioService)this.dominioService).enableSelectForUpdate();
			((IDBEventoService)this.eventoService).enableSelectForUpdate();
			((IDBFRService)this.frService).enableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).enableSelectForUpdate();
			((IDBIntermediarioService)this.intermediarioService).enableSelectForUpdate();
			((IDBIUVService)this.iuvService).enableSelectForUpdate();
			((IDBNotificaService)this.notificaService).enableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).enableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).enableSelectForUpdate();
			((IDBPortaleService)this.portaleService).enableSelectForUpdate();
			((IDBPspService)this.pspService).enableSelectForUpdate();
			((IDBRPTService)this.rptService).enableSelectForUpdate();
			((IDBRRService)this.rrService).enableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).enableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).enableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).enableSelectForUpdate();
			((IDBTipoTributoService)this.tipoTributoService).enableSelectForUpdate();
			((IDBTributoService)this.tributoService).enableSelectForUpdate();
			((IDBUoService)this.uoService).enableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).enableSelectForUpdate();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void disableSelectForUpdate() throws ServiceException {
		if(this.father != null) {
			this.father.disableSelectForUpdate();
			return;
		}
		try {
			((IDBApplicazioneService)this.applicazioneService).disableSelectForUpdate();
			((IDBACLService)this.aclService).disableSelectForUpdate();
			((IDBCanaleService)this.canaleService).disableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).disableSelectForUpdate();
			((IDBDominioService)this.dominioService).disableSelectForUpdate();
			((IDBEventoService)this.eventoService).disableSelectForUpdate();
			((IDBFRService)this.frService).disableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).disableSelectForUpdate();
			((IDBIntermediarioService)this.intermediarioService).disableSelectForUpdate();
			((IDBIUVService)this.iuvService).disableSelectForUpdate();
			((IDBNotificaService)this.notificaService).disableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).disableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).disableSelectForUpdate();
			((IDBPortaleService)this.portaleService).disableSelectForUpdate();
			((IDBPspService)this.pspService).disableSelectForUpdate();
			((IDBRPTService)this.rptService).disableSelectForUpdate();
			((IDBRRService)this.rrService).disableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).disableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).disableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).disableSelectForUpdate();
			((IDBTipoTributoService)this.tipoTributoService).disableSelectForUpdate();
			((IDBTributoService)this.tributoService).disableSelectForUpdate();
			((IDBUoService)this.uoService).disableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).disableSelectForUpdate();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public String getIdTransaction() {
		return idTransaction;
	}
	
	public String getIdModulo() {
		return idModulo;
	}
	
	public IApplicazioneService getApplicazioneService() {
		if(father != null) {
			return father.getApplicazioneService();
		}
		return applicazioneService;
	}

	public IACLService getAclService() {
		if(father != null) {
			return father.getAclService();
		}
		return aclService;
	}

	public ICanaleService getCanaleService() {
		if(father != null) {
			return father.getCanaleService();
		}
		return canaleService;
	}

	public IConnettoreService getConnettoreService() {
		if(father != null) {
			return father.getConnettoreService();
		}
		return connettoreService;
	}

	public IDominioService getDominioService() {
		if(father != null) {
			return father.getDominioService();
		}
		return dominioService;
	}
	
	public IEventoService getEventoService() {
		if(father != null) {
			return father.getEventoService();
		}
		return eventoService;
	}

	public IFRService getFrService() {
		if(father != null) {
			return father.getFrService();
		}
		return frService;
	}

	public IIbanAccreditoService getIbanAccreditoService() {
		if(father != null) {
			return father.getIbanAccreditoService();
		}
		return ibanAccreditoService;
	}


	public IIntermediarioService getIntermediarioService() {
		if(father != null) {
			return father.getIntermediarioService();
		}
		return intermediarioService;
	}

	public IIUVService getIuvService() {
		if(father != null) {
			return father.getIuvService();
		}
		return iuvService;
	}
	
	public INotificaService getNotificaService() {
		if(father != null) {
			return father.getNotificaService();
		}
		return notificaService;
	}

	public IOperatoreService getOperatoreService() {
		if(father != null) {
			return father.getOperatoreService();
		}
		return operatoreService;
	}
	
	public IPagamentoService getPagamentoService() {
		if(father != null) {
			return father.getPagamentoService();
		}
		return pagamentoService;
	}

	public IPortaleService getPortaleService() {
		if(father != null) {
			return father.getPortaleService();
		}
		return portaleService;
	}

	public IPspService getPspService() {
		if(father != null) {
			return father.getPspService();
		}
		return pspService;
	}
	
	public IRendicontazionePagamentoServiceSearch getRendicontazionePagamentoServiceSearch() {
		if(father != null) {
			return father.getRendicontazionePagamentoServiceSearch();
		}
		return rendicontazionePagamentoServiceSearch;
	}
	
	public IRendicontazioneService getRendicontazioneService() {
		if(father != null) {
			return father.getRendicontazioneService();
		}
		return rendicontazioneService;
	}
	
	public IRPTService getRptService() {
		if(father != null) {
			return father.getRptService();
		}
		return rptService;
	}
	
	public IRRService getRrService() {
		if(father != null) {
			return father.getRrService();
		}
		return rrService;
	}

	public ISingoloVersamentoService getSingoloVersamentoService() {
		if(father != null) {
			return father.getSingoloVersamentoService();
		}
		return singoloVersamentoService;
	}

	public IStazioneService getStazioneService() {
		if(father != null) {
			return father.getStazioneService();
		}
		return stazioneService;
	}
	
	public ITipoTributoService getTipoTributoService() {
		if(father != null) {
			return father.getTipoTributoService();
		}
		return tipoTributoService;
	}

	public ITributoService getTributoService() {
		if(father != null) {
			return father.getTributoService();
		}
		return tributoService;
	}
	
	public IUoService getUoService() {
		if(father != null) {
			return father.getUoService();
		}
		return uoService;
	}

	public IVersamentoService getVersamentoService() {
		if(father != null) {
			return father.getVersamentoService();
		}
		return versamentoService;
	}

	public void setAutoCommit(boolean autoCommit) throws ServiceException {
		if(father != null) {
			father.setAutoCommit(autoCommit);
			return;
		}
		
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new ServiceException("Errore durante la gestione dell'AutoCommit della connesione.", e);
		}
	}
	
	private String getCaller() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for(int i=0; i < stackTraceElements.length; i++) {
			if(stackTraceElements[i].getClassName().startsWith("it.govpay.core") 
					|| stackTraceElements[i].getClassName().startsWith("it.govpay.web") 
					|| stackTraceElements[i].getClassName().startsWith("it.govpay.ejb"))
				return stackTraceElements[i].getClassName() + ":" + stackTraceElements[i].getLineNumber();
		}
		return "?";
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
			}
		} catch (Throwable e) {
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
		} catch (Throwable e) {
			log.error("Errore durante la gestione della commit della connesione.", e);
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
		} catch (Throwable e) {
			log.error("Errore durante la rollback.", e);
		}
	}

	public Connection getConnection() {
		if(father != null) {
			return father.getConnection();
		}
		return this.connection;
	}

	public JDBCServiceManagerProperties getJdbcProperties() {
		if(father != null) {
			return father.getJdbcProperties();
		}
		return jdbcProperties;
	}

	public boolean isAutoCommit() throws ServiceException {
		try {
			return getConnection().getAutoCommit();
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'identificazione dello stato di autocommit.", e);
		}
	}
	
	public boolean isClosed() throws ServiceException {
		if(father != null) {
			return father.isClosed();
		}
		return isClosed;
	}
}

