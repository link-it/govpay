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
import java.sql.SQLException;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.anagrafica.AuditBD;
import it.govpay.model.BasicModel;

import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.IDBIntermediarioService;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.IDBStazioneService;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IDBDominioService;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IDBIbanAccreditoService;
import it.govpay.orm.dao.ITipoTributoService;
import it.govpay.orm.dao.IDBTipoTributoService;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.IDBTributoService;
import it.govpay.orm.dao.IUtenzaService;
import it.govpay.orm.dao.IDBUtenzaService;
import it.govpay.orm.dao.IUtenzaDominioService;
import it.govpay.orm.dao.IDBUtenzaDominioService;
import it.govpay.orm.dao.IUtenzaTributoService;
import it.govpay.orm.dao.IDBUtenzaTributoService;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.IDBApplicazioneService;
import it.govpay.orm.dao.IUoService;
import it.govpay.orm.dao.IDBUoService;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IDBOperatoreService;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IDBConnettoreService;
import it.govpay.orm.dao.IACLService;
import it.govpay.orm.dao.IDBACLService;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.IDBVersamentoService;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.IDBSingoloVersamentoService;
import it.govpay.orm.dao.IPagamentoPortaleService;
import it.govpay.orm.dao.IDBPagamentoPortaleService;
import it.govpay.orm.dao.IPagamentoPortaleVersamentoService;
import it.govpay.orm.dao.IDBPagamentoPortaleVersamentoService;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IDBRPTService;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IDBRRService;
import it.govpay.orm.dao.INotificaService;
import it.govpay.orm.dao.IDBNotificaService;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IDBIUVService;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IDBFRService;
import it.govpay.orm.dao.IIncassoService;
import it.govpay.orm.dao.IDBIncassoService;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.IDBPagamentoService;
import it.govpay.orm.dao.IRendicontazioneService;
import it.govpay.orm.dao.IDBRendicontazioneService;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IDBEventoService;
import it.govpay.orm.dao.IBatchService;
import it.govpay.orm.dao.IDBBatchService;
import it.govpay.orm.dao.ITracciatoService;
import it.govpay.orm.dao.IDBTracciatoService;
import it.govpay.orm.dao.IOperazioneService;
import it.govpay.orm.dao.IDBOperazioneService;
import it.govpay.orm.dao.IAuditService;
import it.govpay.orm.dao.IDBAuditService;
import it.govpay.orm.dao.IAvvisoService;
import it.govpay.orm.dao.IDBAvvisoService;

import it.govpay.orm.dao.IRendicontazionePagamentoServiceSearch;

import it.govpay.orm.dao.jdbc.JDBCServiceManager;

public class BasicBD {
	
	private JDBCServiceManager serviceManager;
	private JDBCServiceManagerProperties jdbcProperties;
	
	private IIntermediarioService intermediarioService;
	private IStazioneService stazioneService;
	private IDominioService dominioService;
	private IIbanAccreditoService ibanAccreditoService;
	private ITipoTributoService tipoTributoService;
	private ITributoService tributoService;
	private IUtenzaService utenzaService;
	private IUtenzaDominioService utenzaDominioService;
	private IUtenzaTributoService utenzaTributoService;
	private IApplicazioneService applicazioneService;
	private IUoService uoService;
	private IOperatoreService operatoreService;
	private IConnettoreService connettoreService;
	private IACLService aclService;
	private IVersamentoService versamentoService;
	private ISingoloVersamentoService singoloVersamentoService;
	private IPagamentoPortaleService pagamentoPortaleService;
	private IPagamentoPortaleVersamentoService pagamentoPortaleVersamentoService;
	private IRPTService rptService;
	private IRRService rrService;
	private INotificaService notificaService;
	private IIUVService iuvService;
	private IFRService frService;
	private IIncassoService incassoService;
	private IPagamentoService pagamentoService;
	private IRendicontazioneService rendicontazioneService;
	private IEventoService eventoService;
	private IBatchService batchService;
	private ITracciatoService tracciatoService;
	private IOperazioneService operazioneService;
	private IAuditService auditService;
	private IAvvisoService avvisoService;
	
	private IRendicontazionePagamentoServiceSearch rendicontazionePagamentoServiceSearch;

	
	private String idTransaction;
	private String idModulo;
	
	private Connection connection;
	private boolean isClosed;
	private static Logger log;
	private Long idOperatore;
	
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
		if(log == null)
			log = LoggerWrapperFactory.getLogger(JDBCServiceManager.class);
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
				this.intermediarioService = this.serviceManager.getIntermediarioService();
				this.stazioneService = this.serviceManager.getStazioneService();
				this.dominioService = this.serviceManager.getDominioService();
				this.ibanAccreditoService = this.serviceManager.getIbanAccreditoService();
				this.tipoTributoService = this.serviceManager.getTipoTributoService();
				this.tributoService = this.serviceManager.getTributoService();
				this.utenzaService = this.serviceManager.getUtenzaService();
				this.utenzaDominioService = this.serviceManager.getUtenzaDominioService();
				this.utenzaTributoService = this.serviceManager.getUtenzaTributoService();
				this.applicazioneService = this.serviceManager.getApplicazioneService();
				this.uoService = this.serviceManager.getUoService();
				this.operatoreService = this.serviceManager.getOperatoreService();
				this.connettoreService = this.serviceManager.getConnettoreService();
				this.aclService = this.serviceManager.getACLService();
				this.versamentoService = this.serviceManager.getVersamentoService();
				this.singoloVersamentoService = this.serviceManager.getSingoloVersamentoService();
				this.pagamentoPortaleService = this.serviceManager.getPagamentoPortaleService();
				this.pagamentoPortaleVersamentoService = this.serviceManager.getPagamentoPortaleVersamentoService();
				this.rptService = this.serviceManager.getRPTService();
				this.rrService = this.serviceManager.getRRService();
				this.notificaService = this.serviceManager.getNotificaService();
				this.iuvService = this.serviceManager.getIUVService();
				this.frService = this.serviceManager.getFRService();
				this.incassoService = this.serviceManager.getIncassoService();
				this.pagamentoService = this.serviceManager.getPagamentoService();
				this.rendicontazioneService = this.serviceManager.getRendicontazioneService();
				this.eventoService = this.serviceManager.getEventoService();
				this.batchService = this.serviceManager.getBatchService();
				this.tracciatoService = this.serviceManager.getTracciatoService();
				this.operazioneService = this.serviceManager.getOperazioneService();
				this.auditService = this.serviceManager.getAuditService();
				this.avvisoService = this.serviceManager.getAvvisoService();
				
				this.rendicontazionePagamentoServiceSearch = this.serviceManager.getRendicontazionePagamentoServiceSearch();
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
			((IDBIntermediarioService)this.intermediarioService).enableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).enableSelectForUpdate();
			((IDBDominioService)this.dominioService).enableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).enableSelectForUpdate();
			((IDBTipoTributoService)this.tipoTributoService).enableSelectForUpdate();
			((IDBTributoService)this.tributoService).enableSelectForUpdate();
			((IDBUtenzaService)this.utenzaService).enableSelectForUpdate();
			((IDBUtenzaDominioService)this.utenzaDominioService).enableSelectForUpdate();
			((IDBUtenzaTributoService)this.utenzaTributoService).enableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).enableSelectForUpdate();
			((IDBUoService)this.uoService).enableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).enableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).enableSelectForUpdate();
			((IDBACLService)this.aclService).enableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).enableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).enableSelectForUpdate();
			((IDBPagamentoPortaleService)this.pagamentoPortaleService).enableSelectForUpdate();
			((IDBPagamentoPortaleVersamentoService)this.pagamentoPortaleVersamentoService).enableSelectForUpdate();
			((IDBRPTService)this.rptService).enableSelectForUpdate();
			((IDBRRService)this.rrService).enableSelectForUpdate();
			((IDBNotificaService)this.notificaService).enableSelectForUpdate();
			((IDBIUVService)this.iuvService).enableSelectForUpdate();
			((IDBFRService)this.frService).enableSelectForUpdate();
			((IDBIncassoService)this.incassoService).enableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).enableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).enableSelectForUpdate();
			((IDBEventoService)this.eventoService).enableSelectForUpdate();
			((IDBBatchService)this.batchService).enableSelectForUpdate();
			((IDBTracciatoService)this.tracciatoService).enableSelectForUpdate();
			((IDBOperazioneService)this.operazioneService).enableSelectForUpdate();
			((IDBAuditService)this.auditService).enableSelectForUpdate();
			((IDBAvvisoService)this.avvisoService).enableSelectForUpdate();
			
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
			((IDBIntermediarioService)this.intermediarioService).disableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).disableSelectForUpdate();
			((IDBDominioService)this.dominioService).disableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).disableSelectForUpdate();
			((IDBTipoTributoService)this.tipoTributoService).disableSelectForUpdate();
			((IDBTributoService)this.tributoService).disableSelectForUpdate();
			((IDBUtenzaService)this.utenzaService).disableSelectForUpdate();
			((IDBUtenzaDominioService)this.utenzaDominioService).disableSelectForUpdate();
			((IDBUtenzaTributoService)this.utenzaTributoService).disableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).disableSelectForUpdate();
			((IDBUoService)this.uoService).disableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).disableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).disableSelectForUpdate();
			((IDBACLService)this.aclService).disableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).disableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).disableSelectForUpdate();
			((IDBPagamentoPortaleService)this.pagamentoPortaleService).disableSelectForUpdate();
			((IDBPagamentoPortaleVersamentoService)this.pagamentoPortaleVersamentoService).disableSelectForUpdate();
			((IDBRPTService)this.rptService).disableSelectForUpdate();
			((IDBRRService)this.rrService).disableSelectForUpdate();
			((IDBNotificaService)this.notificaService).disableSelectForUpdate();
			((IDBIUVService)this.iuvService).disableSelectForUpdate();
			((IDBFRService)this.frService).disableSelectForUpdate();
			((IDBIncassoService)this.incassoService).disableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).disableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).disableSelectForUpdate();
			((IDBEventoService)this.eventoService).disableSelectForUpdate();
			((IDBBatchService)this.batchService).disableSelectForUpdate();
			((IDBTracciatoService)this.tracciatoService).disableSelectForUpdate();
			((IDBOperazioneService)this.operazioneService).disableSelectForUpdate();
			((IDBAuditService)this.auditService).disableSelectForUpdate();
			((IDBAvvisoService)this.avvisoService).disableSelectForUpdate();
			
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
	
	
	public IIntermediarioService getIntermediarioService() {
		if(father != null) {
			return father.getIntermediarioService();
		}
		return this.intermediarioService;
	}
	
	public IStazioneService getStazioneService() {
		if(father != null) {
			return father.getStazioneService();
		}
		return this.stazioneService;
	}
	
	public IDominioService getDominioService() {
		if(father != null) {
			return father.getDominioService();
		}
		return this.dominioService;
	}
	
	public IIbanAccreditoService getIbanAccreditoService() {
		if(father != null) {
			return father.getIbanAccreditoService();
		}
		return this.ibanAccreditoService;
	}
	
	public ITipoTributoService getTipoTributoService() {
		if(father != null) {
			return father.getTipoTributoService();
		}
		return this.tipoTributoService;
	}
	
	public ITributoService getTributoService() {
		if(father != null) {
			return father.getTributoService();
		}
		return this.tributoService;
	}
	
	public IUtenzaService getUtenzaService() {
		if(father != null) {
			return father.getUtenzaService();
		}
		return this.utenzaService;
	}
	
	public IUtenzaDominioService getUtenzaDominioService() {
		if(father != null) {
			return father.getUtenzaDominioService();
		}
		return this.utenzaDominioService;
	}
	
	public IUtenzaTributoService getUtenzaTributoService() {
		if(father != null) {
			return father.getUtenzaTributoService();
		}
		return this.utenzaTributoService;
	}
	
	public IApplicazioneService getApplicazioneService() {
		if(father != null) {
			return father.getApplicazioneService();
		}
		return this.applicazioneService;
	}
	
	public IUoService getUoService() {
		if(father != null) {
			return father.getUoService();
		}
		return this.uoService;
	}
	
	public IOperatoreService getOperatoreService() {
		if(father != null) {
			return father.getOperatoreService();
		}
		return this.operatoreService;
	}
	
	public IConnettoreService getConnettoreService() {
		if(father != null) {
			return father.getConnettoreService();
		}
		return this.connettoreService;
	}
	
	public IACLService getAclService() {
		if(father != null) {
			return father.getAclService();
		}
		return this.aclService;
	}
	
	public IVersamentoService getVersamentoService() {
		if(father != null) {
			return father.getVersamentoService();
		}
		return this.versamentoService;
	}
	
	public ISingoloVersamentoService getSingoloVersamentoService() {
		if(father != null) {
			return father.getSingoloVersamentoService();
		}
		return this.singoloVersamentoService;
	}
	
	public IPagamentoPortaleService getPagamentoPortaleService() {
		if(father != null) {
			return father.getPagamentoPortaleService();
		}
		return this.pagamentoPortaleService;
	}
	
	public IPagamentoPortaleVersamentoService getPagamentoPortaleVersamentoService() {
		if(father != null) {
			return father.getPagamentoPortaleVersamentoService();
		}
		return this.pagamentoPortaleVersamentoService;
	}
	
	public IRPTService getRptService() {
		if(father != null) {
			return father.getRptService();
		}
		return this.rptService;
	}
	
	public IRRService getRrService() {
		if(father != null) {
			return father.getRrService();
		}
		return this.rrService;
	}
	
	public INotificaService getNotificaService() {
		if(father != null) {
			return father.getNotificaService();
		}
		return this.notificaService;
	}
	
	public IIUVService getIuvService() {
		if(father != null) {
			return father.getIuvService();
		}
		return this.iuvService;
	}
	
	public IFRService getFrService() {
		if(father != null) {
			return father.getFrService();
		}
		return this.frService;
	}
	
	public IIncassoService getIncassoService() {
		if(father != null) {
			return father.getIncassoService();
		}
		return this.incassoService;
	}
	
	public IPagamentoService getPagamentoService() {
		if(father != null) {
			return father.getPagamentoService();
		}
		return this.pagamentoService;
	}
	
	public IRendicontazioneService getRendicontazioneService() {
		if(father != null) {
			return father.getRendicontazioneService();
		}
		return this.rendicontazioneService;
	}
	
	public IEventoService getEventoService() {
		if(father != null) {
			return father.getEventoService();
		}
		return this.eventoService;
	}
	
	public IBatchService getBatchService() {
		if(father != null) {
			return father.getBatchService();
		}
		return this.batchService;
	}
	
	public ITracciatoService getTracciatoService() {
		if(father != null) {
			return father.getTracciatoService();
		}
		return this.tracciatoService;
	}
	
	public IOperazioneService getOperazioneService() {
		if(father != null) {
			return father.getOperazioneService();
		}
		return this.operazioneService;
	}
	
	public IAuditService getAuditService() {
		if(father != null) {
			return father.getAuditService();
		}
		return this.auditService;
	}
	
	public IAvvisoService getAvvisoService() {
		if(father != null) {
			return father.getAvvisoService();
		}
		return this.avvisoService;
	}
	

	public IRendicontazionePagamentoServiceSearch getRendicontazionePagamentoServiceSearch() {
		if(father != null) {
			return father.getRendicontazionePagamentoServiceSearch();
		}
		return this.rendicontazionePagamentoServiceSearch;
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
	
	protected void emitAudit(BasicModel model){
		if(father != null) {
			father.emitAudit(model);
		} else {
			if(idOperatore != null) {
				AuditBD db = new AuditBD(this);
				db.insertAudit(getIdOperatore(), model);
			}
		}
	}

	public long getIdOperatore() {
		return idOperatore;
	}

	public void setIdOperatore(long idOperatore) {
		this.idOperatore = idOperatore;
	}
}
