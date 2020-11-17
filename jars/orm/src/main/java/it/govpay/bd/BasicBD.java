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
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.anagrafica.AuditBD;
import it.govpay.model.BasicModel;
import it.govpay.orm.dao.IACLService;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.IAuditService;
import it.govpay.orm.dao.IBatchService;
import it.govpay.orm.dao.IConfigurazioneService;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IDBACLService;
import it.govpay.orm.dao.IDBApplicazioneService;
import it.govpay.orm.dao.IDBAuditService;
import it.govpay.orm.dao.IDBBatchService;
import it.govpay.orm.dao.IDBConfigurazioneService;
import it.govpay.orm.dao.IDBConnettoreService;
import it.govpay.orm.dao.IDBDocumentoService;
import it.govpay.orm.dao.IDBDominioService;
import it.govpay.orm.dao.IDBEventoService;
import it.govpay.orm.dao.IDBFRService;
import it.govpay.orm.dao.IDBIUVService;
import it.govpay.orm.dao.IDBIbanAccreditoService;
import it.govpay.orm.dao.IDBIncassoService;
import it.govpay.orm.dao.IDBIntermediarioService;
import it.govpay.orm.dao.IDBNotificaAppIOService;
import it.govpay.orm.dao.IDBNotificaService;
import it.govpay.orm.dao.IDBOperatoreService;
import it.govpay.orm.dao.IDBOperazioneService;
import it.govpay.orm.dao.IDBPagamentoPortaleService;
import it.govpay.orm.dao.IDBPagamentoPortaleVersamentoService;
import it.govpay.orm.dao.IDBPagamentoService;
import it.govpay.orm.dao.IDBPromemoriaService;
import it.govpay.orm.dao.IDBRPTService;
import it.govpay.orm.dao.IDBRRService;
import it.govpay.orm.dao.IDBRendicontazioneService;
import it.govpay.orm.dao.IDBSingoloVersamentoService;
import it.govpay.orm.dao.IDBStampaService;
import it.govpay.orm.dao.IDBStazioneService;
import it.govpay.orm.dao.IDBTipoTributoService;
import it.govpay.orm.dao.IDBTipoVersamentoDominioService;
import it.govpay.orm.dao.IDBTipoVersamentoService;
import it.govpay.orm.dao.IDBTracciatoService;
import it.govpay.orm.dao.IDBTributoService;
import it.govpay.orm.dao.IDBUoService;
import it.govpay.orm.dao.IDBUtenzaDominioService;
import it.govpay.orm.dao.IDBUtenzaService;
import it.govpay.orm.dao.IDBUtenzaTipoVersamentoService;
import it.govpay.orm.dao.IDBVersamentoService;
import it.govpay.orm.dao.IDBVistaEventiVersamentoServiceSearch;
import it.govpay.orm.dao.IDBVistaPagamentoPortaleServiceSearch;
import it.govpay.orm.dao.IDBVistaRendicontazioneServiceSearch;
import it.govpay.orm.dao.IDBVistaRptVersamentoServiceSearch;
import it.govpay.orm.dao.IDocumentoService;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IIncassoService;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.INotificaAppIOService;
import it.govpay.orm.dao.INotificaService;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IOperazioneService;
import it.govpay.orm.dao.IPagamentoPortaleService;
import it.govpay.orm.dao.IPagamentoPortaleVersamentoService;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.IPromemoriaService;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IRendicontazioneService;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.IStampaService;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.ITipoTributoService;
import it.govpay.orm.dao.ITipoVersamentoDominioService;
import it.govpay.orm.dao.ITipoVersamentoService;
import it.govpay.orm.dao.ITracciatoService;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.IUoService;
import it.govpay.orm.dao.IUtenzaDominioService;
import it.govpay.orm.dao.IUtenzaService;
import it.govpay.orm.dao.IUtenzaTipoVersamentoService;
import it.govpay.orm.dao.IVersamentoIncassoServiceSearch;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.IVistaEventiVersamentoServiceSearch;
import it.govpay.orm.dao.IVistaPagamentoPortaleServiceSearch;
import it.govpay.orm.dao.IVistaRendicontazioneServiceSearch;
import it.govpay.orm.dao.IVistaRiscossioniServiceSearch;
import it.govpay.orm.dao.IVistaRptVersamentoServiceSearch;
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
	private IUtenzaTipoVersamentoService utenzaTipoVersamentoService;
	private IApplicazioneService applicazioneService;
	private IUoService uoService;
	private IOperatoreService operatoreService;
	private IConnettoreService connettoreService;
	private IACLService aclService;
	private IVersamentoService versamentoService;
	private ITipoVersamentoService tipoVersamentoService;
	private ITipoVersamentoDominioService tipoVersamentoDominioService;
	private ISingoloVersamentoService singoloVersamentoService;
	private IPagamentoPortaleService pagamentoPortaleService;
	private IPagamentoPortaleVersamentoService pagamentoPortaleVersamentoService;
	private IRPTService rptService;
	private IRRService rrService;
	private INotificaService notificaService;
	private INotificaAppIOService notificaAppIOService;
	private IIUVService iuvService;
	private IFRService frService;
	private IIncassoService incassoService;
	private IPagamentoService pagamentoService;
	private IRendicontazioneService rendicontazioneService;
	private IEventoService eventoService;
	private IVistaEventiVersamentoServiceSearch vistaEventiVersamentoService;
	private IBatchService batchService;
	private ITracciatoService tracciatoService;
	private IOperazioneService operazioneService;
	private IAuditService auditService;
	private IVersamentoIncassoServiceSearch versamentoIncassoServiceSearch;
	private IVistaRiscossioniServiceSearch vistaRiscossioniServiceSearch;
	private IStampaService stampaService;
	private IConfigurazioneService configurazioneService;
	private IPromemoriaService promemoriaService;
	private IVistaPagamentoPortaleServiceSearch vistaPagamentoPortaleServiceSearch;
	private IVistaRendicontazioneServiceSearch vistaRendicontazioneServiceSearch;
	private IVistaRptVersamentoServiceSearch vistaRptVersamentoServiceSearch;
	private IDocumentoService documentoService;
	
	private String idTransaction;
	private String idModulo;
	
	private Connection connection;
	private boolean isClosed;
	protected static Logger log;
	private Long idOperatore;
	
	BasicBD father;
	private boolean useCache;
	private boolean isSelectForUpdate;
	private boolean isAtomica;
	
	private JDBC_SQLObjectFactory jdbcSqlObjectFactory;
	
	public BasicBD(BasicBD basicBD) {
		this.father = basicBD;
	}
	
	public BasicBD(String idTransaction) {
		this(idTransaction, true);
	}
	
	public static BasicBD newInstance(String idTransaction) throws ServiceException {
		return new BasicBD(idTransaction, true);
	}
	
	public static BasicBD newInstance(String idTransaction, boolean useCache) throws ServiceException {
		return new BasicBD(idTransaction, useCache);
	}
	
	public BasicBD(String idTransaction, boolean useCache) {
		this.isClosed = true;
		this.idTransaction = idTransaction;
		this.idModulo = this.getCaller();
		this.useCache = useCache;
		this.isSelectForUpdate = false;
		this.jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
		if(log == null)
			log = LoggerWrapperFactory.getLogger(JDBCServiceManager.class);
		this.isAtomica = true;
//		this.setupConnection(idTransaction, this.idModulo);
	}
	
	public void setupConnection(String idTransaction) throws ServiceException {
		this.idModulo = this.getCaller();
		this.setupConnection(idTransaction, this.idModulo);
	}

	private void setupConnection(String idTransaction, String idModulo) throws ServiceException {
		if(this.father != null) {
			this.father.setupConnection(idTransaction);
			return;
		}
		if(this.isClosed) {
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
				this.utenzaTipoVersamentoService = this.serviceManager.getUtenzaTipoVersamentoService();
				this.applicazioneService = this.serviceManager.getApplicazioneService();
				this.uoService = this.serviceManager.getUoService();
				this.operatoreService = this.serviceManager.getOperatoreService();
				this.connettoreService = this.serviceManager.getConnettoreService();
				this.aclService = this.serviceManager.getACLService();
				this.tipoVersamentoService = this.serviceManager.getTipoVersamentoService();
				this.tipoVersamentoDominioService = this.serviceManager.getTipoVersamentoDominioService();
				this.versamentoService = this.serviceManager.getVersamentoService();
				this.singoloVersamentoService = this.serviceManager.getSingoloVersamentoService();
				this.pagamentoPortaleService = this.serviceManager.getPagamentoPortaleService();
				this.pagamentoPortaleVersamentoService = this.serviceManager.getPagamentoPortaleVersamentoService();
				this.rptService = this.serviceManager.getRPTService();
				this.rrService = this.serviceManager.getRRService();
				this.notificaService = this.serviceManager.getNotificaService();
				this.notificaAppIOService = this.serviceManager.getNotificaAppIOService();
				this.iuvService = this.serviceManager.getIUVService();
				this.frService = this.serviceManager.getFRService();
				this.incassoService = this.serviceManager.getIncassoService();
				this.pagamentoService = this.serviceManager.getPagamentoService();
				this.rendicontazioneService = this.serviceManager.getRendicontazioneService();
				this.eventoService = this.serviceManager.getEventoService();
				this.vistaEventiVersamentoService = this.serviceManager.getVistaEventiVersamentoServiceSearch();
				this.batchService = this.serviceManager.getBatchService();
				this.tracciatoService = this.serviceManager.getTracciatoService();
				this.operazioneService = this.serviceManager.getOperazioneService();
				this.auditService = this.serviceManager.getAuditService();
				this.versamentoIncassoServiceSearch = this.serviceManager.getVersamentoIncassoServiceSearch();
				this.vistaRiscossioniServiceSearch = this.serviceManager.getVistaRiscossioniServiceSearch();
				this.stampaService = this.serviceManager.getStampaService();
				this.configurazioneService = this.serviceManager.getConfigurazioneService();
				this.promemoriaService = this.serviceManager.getPromemoriaService();
				this.vistaPagamentoPortaleServiceSearch = this.serviceManager.getVistaPagamentoPortaleServiceSearch();
				this.vistaRendicontazioneServiceSearch = this.serviceManager.getVistaRendicontazioneServiceSearch();
				this.vistaRptVersamentoServiceSearch = this.serviceManager.getVistaRptVersamentoServiceSearch();
				this.documentoService = this.serviceManager.getDocumentoService();
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
			((IDBUtenzaTipoVersamentoService)this.utenzaTipoVersamentoService).enableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).enableSelectForUpdate();
			((IDBUoService)this.uoService).enableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).enableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).enableSelectForUpdate();
			((IDBACLService)this.aclService).enableSelectForUpdate();
			((IDBTipoVersamentoService)this.tipoVersamentoService).enableSelectForUpdate();
			((IDBTipoVersamentoDominioService)this.tipoVersamentoDominioService).enableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).enableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).enableSelectForUpdate();
			((IDBPagamentoPortaleService)this.pagamentoPortaleService).enableSelectForUpdate();
			((IDBPagamentoPortaleVersamentoService)this.pagamentoPortaleVersamentoService).enableSelectForUpdate();
			((IDBRPTService)this.rptService).enableSelectForUpdate();
			((IDBRRService)this.rrService).enableSelectForUpdate();
			((IDBNotificaService)this.notificaService).enableSelectForUpdate();
			((IDBNotificaAppIOService)this.notificaAppIOService).enableSelectForUpdate();
			((IDBIUVService)this.iuvService).enableSelectForUpdate();
			((IDBFRService)this.frService).enableSelectForUpdate();
			((IDBIncassoService)this.incassoService).enableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).enableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).enableSelectForUpdate();
			((IDBEventoService)this.eventoService).enableSelectForUpdate();
			((IDBVistaEventiVersamentoServiceSearch)this.vistaEventiVersamentoService).enableSelectForUpdate();
			((IDBBatchService)this.batchService).enableSelectForUpdate();
			((IDBTracciatoService)this.tracciatoService).enableSelectForUpdate();
			((IDBOperazioneService)this.operazioneService).enableSelectForUpdate();
			((IDBAuditService)this.auditService).enableSelectForUpdate();
			((IDBStampaService)this.stampaService).enableSelectForUpdate();
			((IDBConfigurazioneService)this.configurazioneService).enableSelectForUpdate();
			((IDBPromemoriaService)this.promemoriaService).enableSelectForUpdate();
			((IDBVistaPagamentoPortaleServiceSearch)this.vistaPagamentoPortaleServiceSearch).enableSelectForUpdate();
			((IDBVistaRendicontazioneServiceSearch)this.vistaRendicontazioneServiceSearch).enableSelectForUpdate();
			((IDBVistaRptVersamentoServiceSearch)this.vistaRptVersamentoServiceSearch).enableSelectForUpdate();
			((IDBDocumentoService)this.documentoService).enableSelectForUpdate();
			
			this.isSelectForUpdate = true;
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
			((IDBUtenzaTipoVersamentoService)this.utenzaTipoVersamentoService).disableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).disableSelectForUpdate();
			((IDBUoService)this.uoService).disableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).disableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).disableSelectForUpdate();
			((IDBACLService)this.aclService).disableSelectForUpdate();
			((IDBTipoVersamentoService)this.tipoVersamentoService).disableSelectForUpdate();
			((IDBTipoVersamentoDominioService)this.tipoVersamentoDominioService).disableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).disableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).disableSelectForUpdate();
			((IDBPagamentoPortaleService)this.pagamentoPortaleService).disableSelectForUpdate();
			((IDBPagamentoPortaleVersamentoService)this.pagamentoPortaleVersamentoService).disableSelectForUpdate();
			((IDBRPTService)this.rptService).disableSelectForUpdate();
			((IDBRRService)this.rrService).disableSelectForUpdate();
			((IDBNotificaService)this.notificaService).disableSelectForUpdate();
			((IDBNotificaAppIOService)this.notificaAppIOService).disableSelectForUpdate();
			((IDBIUVService)this.iuvService).disableSelectForUpdate();
			((IDBFRService)this.frService).disableSelectForUpdate();
			((IDBIncassoService)this.incassoService).disableSelectForUpdate();
			((IDBPagamentoService)this.pagamentoService).disableSelectForUpdate();
			((IDBRendicontazioneService)this.rendicontazioneService).disableSelectForUpdate();
			((IDBEventoService)this.eventoService).disableSelectForUpdate();
			((IDBVistaEventiVersamentoServiceSearch)this.vistaEventiVersamentoService).disableSelectForUpdate();
			((IDBBatchService)this.batchService).disableSelectForUpdate();
			((IDBTracciatoService)this.tracciatoService).disableSelectForUpdate();
			((IDBOperazioneService)this.operazioneService).disableSelectForUpdate();
			((IDBAuditService)this.auditService).disableSelectForUpdate();
			((IDBStampaService)this.stampaService).disableSelectForUpdate();
			((IDBConfigurazioneService)this.configurazioneService).disableSelectForUpdate();
			((IDBPromemoriaService)this.promemoriaService).disableSelectForUpdate();
			((IDBVistaPagamentoPortaleServiceSearch)this.vistaPagamentoPortaleServiceSearch).disableSelectForUpdate();
			((IDBVistaRendicontazioneServiceSearch)this.vistaRendicontazioneServiceSearch).disableSelectForUpdate();
			((IDBVistaRptVersamentoServiceSearch)this.vistaRptVersamentoServiceSearch).disableSelectForUpdate();
			((IDBDocumentoService)this.documentoService).disableSelectForUpdate();
			
			this.isSelectForUpdate = false;
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public String getIdTransaction() {
		return this.idTransaction;
	}
	
	public String getIdModulo() {
		return this.idModulo;
	}
	
	
	public IIntermediarioService getIntermediarioService() {
		if(this.father != null) {
			return this.father.getIntermediarioService();
		}
		return this.intermediarioService;
	}
	
	public IStazioneService getStazioneService() {
		if(this.father != null) {
			return this.father.getStazioneService();
		}
		return this.stazioneService;
	}
	
	public IDominioService getDominioService() {
		if(this.father != null) {
			return this.father.getDominioService();
		}
		return this.dominioService;
	}
	
	public IIbanAccreditoService getIbanAccreditoService() {
		if(this.father != null) {
			return this.father.getIbanAccreditoService();
		}
		return this.ibanAccreditoService;
	}
	
	public ITipoTributoService getTipoTributoService() {
		if(this.father != null) {
			return this.father.getTipoTributoService();
		}
		return this.tipoTributoService;
	}
	
	public ITributoService getTributoService() {
		if(this.father != null) {
			return this.father.getTributoService();
		}
		return this.tributoService;
	}
	
	public IUtenzaService getUtenzaService() {
		if(this.father != null) {
			return this.father.getUtenzaService();
		}
		return this.utenzaService;
	}
	
	public IUtenzaDominioService getUtenzaDominioService() {
		if(this.father != null) {
			return this.father.getUtenzaDominioService();
		}
		return this.utenzaDominioService;
	}
	
	public IUtenzaTipoVersamentoService getUtenzaTipoVersamentoService() {
		if(this.father != null) {
			return this.father.getUtenzaTipoVersamentoService();
		}
		return this.utenzaTipoVersamentoService;
	}
	
	public IApplicazioneService getApplicazioneService() {
		if(this.father != null) {
			return this.father.getApplicazioneService();
		}
		return this.applicazioneService;
	}
	
	public IUoService getUoService() {
		if(this.father != null) {
			return this.father.getUoService();
		}
		return this.uoService;
	}
	
	public IOperatoreService getOperatoreService() {
		if(this.father != null) {
			return this.father.getOperatoreService();
		}
		return this.operatoreService;
	}
	
	public IConnettoreService getConnettoreService() {
		if(this.father != null) {
			return this.father.getConnettoreService();
		}
		return this.connettoreService;
	}
	
	public IACLService getAclService() {
		if(this.father != null) {
			return this.father.getAclService();
		}
		return this.aclService;
	}
	
	public ITipoVersamentoService getTipoVersamentoService() {
		if(this.father != null) {
			return this.father.getTipoVersamentoService();
		}
		return this.tipoVersamentoService;
	}
	
	public ITipoVersamentoDominioService getTipoVersamentoDominioService() {
		if(this.father != null) {
			return this.father.getTipoVersamentoDominioService();
		}
		return this.tipoVersamentoDominioService;
	}
	
	public IVersamentoService getVersamentoService() {
		if(this.father != null) {
			return this.father.getVersamentoService();
		}
		return this.versamentoService;
	}
	
	public ISingoloVersamentoService getSingoloVersamentoService() {
		if(this.father != null) {
			return this.father.getSingoloVersamentoService();
		}
		return this.singoloVersamentoService;
	}
	
	public IPagamentoPortaleService getPagamentoPortaleService() {
		if(this.father != null) {
			return this.father.getPagamentoPortaleService();
		}
		return this.pagamentoPortaleService;
	}
	
	public IPagamentoPortaleVersamentoService getPagamentoPortaleVersamentoService() {
		if(this.father != null) {
			return this.father.getPagamentoPortaleVersamentoService();
		}
		return this.pagamentoPortaleVersamentoService;
	}
	
	public IRPTService getRptService() {
		if(this.father != null) {
			return this.father.getRptService();
		}
		return this.rptService;
	}
	
	public IRRService getRrService() {
		if(this.father != null) {
			return this.father.getRrService();
		}
		return this.rrService;
	}
	
	public INotificaService getNotificaService() {
		if(this.father != null) {
			return this.father.getNotificaService();
		}
		return this.notificaService;
	}
	
	public INotificaAppIOService getNotificaAppIOService() {
		if(this.father != null) {
			return this.father.getNotificaAppIOService();
		}
		return this.notificaAppIOService;
	}
	
	public IIUVService getIuvService() {
		if(this.father != null) {
			return this.father.getIuvService();
		}
		return this.iuvService;
	}
	
	public IFRService getFrService() {
		if(this.father != null) {
			return this.father.getFrService();
		}
		return this.frService;
	}
	
	public IIncassoService getIncassoService() {
		if(this.father != null) {
			return this.father.getIncassoService();
		}
		return this.incassoService;
	}
	
	public IPagamentoService getPagamentoService() {
		if(this.father != null) {
			return this.father.getPagamentoService();
		}
		return this.pagamentoService;
	}
	
	public IRendicontazioneService getRendicontazioneService() {
		if(this.father != null) {
			return this.father.getRendicontazioneService();
		}
		return this.rendicontazioneService;
	}
	
	public IEventoService getEventoService() {
		if(this.father != null) {
			return this.father.getEventoService();
		}
		return this.eventoService;
	}
	
	public IVistaEventiVersamentoServiceSearch getVistaEventiVersamentoService() {
		if(this.father != null) {
			return this.father.getVistaEventiVersamentoService();
		}
		return this.vistaEventiVersamentoService;
	}
	
	public IBatchService getBatchService() {
		if(this.father != null) {
			return this.father.getBatchService();
		}
		return this.batchService;
	}
	
	public ITracciatoService getTracciatoService() {
		if(this.father != null) {
			return this.father.getTracciatoService();
		}
		return this.tracciatoService;
	}
	
	public IOperazioneService getOperazioneService() {
		if(this.father != null) {
			return this.father.getOperazioneService();
		}
		return this.operazioneService;
	}
	
	public IAuditService getAuditService() {
		if(this.father != null) {
			return this.father.getAuditService();
		}
		return this.auditService;
	}
	
	public IStampaService getStampaService() {
		if(this.father != null) {
			return this.father.getStampaService();
		}
		return this.stampaService;
	}
	
	public IVersamentoIncassoServiceSearch getVersamentoIncassoServiceSearch() {
		if(this.father != null) {
			return this.father.getVersamentoIncassoServiceSearch();
		}
		return this.versamentoIncassoServiceSearch;
	}
	
	public IVistaRiscossioniServiceSearch getVistaRiscossioniServiceSearch() {
		if(this.father != null) {
			return this.father.getVistaRiscossioniServiceSearch();
		}
		return vistaRiscossioniServiceSearch;
	}
	
	public IConfigurazioneService getConfigurazioneService() {
		if(this.father != null) {
			return this.father.getConfigurazioneService();
		}
		return this.configurazioneService;
	}
	
	public IPromemoriaService getPromemoriaService() {
		if(this.father != null) {
			return this.father.getPromemoriaService();
		}
		return this.promemoriaService;
	}
	
	public IVistaPagamentoPortaleServiceSearch getVistaPagamentoPortaleServiceSearch() {
		if(this.father != null) {
			return this.father.getVistaPagamentoPortaleServiceSearch();
		}
		return this.vistaPagamentoPortaleServiceSearch;
	}
	
	public IVistaRendicontazioneServiceSearch getVistaRendicontazioneServiceSearch() {
		if(this.father != null) {
			return this.father.getVistaRendicontazioneServiceSearch();
		}
		return this.vistaRendicontazioneServiceSearch;
	}
	
	public IVistaRptVersamentoServiceSearch getVistaRptVersamentoServiceSearch() {
		if(this.father != null) {
			return this.father.getVistaRptVersamentoServiceSearch();
		}
		return this.vistaRptVersamentoServiceSearch;
	}
	
	public IDocumentoService getDocumentoService() {
		if(this.father != null) {
			return this.father.getDocumentoService();
		}
		return this.documentoService;
	}

	public void setAutoCommit(boolean autoCommit) throws ServiceException {
		if(this.father != null) {
			this.father.setAutoCommit(autoCommit);
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
		if(this.father != null) {
			this.father.closeConnection();
			return;
		}
		
		try {
			if(this.connection != null && !this.isClosed) {
				this.connection.close();
				this.isClosed = true;
			}
		} catch (Throwable e) {
			log.error("Errore durante la chiusura della connessione.", e);
		}
	}
	
	public void commit() throws ServiceException{
		if(this.father != null) {
			this.father.commit();
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
		if(this.father != null) {
			this.father.rollback();
			return;
		}
		
		try {
			if(this.connection != null && !this.connection.getAutoCommit() && !this.isClosed)
				this.connection.rollback();
		} catch (Throwable e) {
			log.error("Errore durante la rollback.", e);
		}
	}

	public Connection getConnection() {
		if(this.father != null) {
			return this.father.getConnection();
		}
		return this.connection;
	}

	public JDBCServiceManagerProperties getJdbcProperties() {
		if(this.father != null) {
			return this.father.getJdbcProperties();
		}
		return this.jdbcProperties;
	}

	public boolean isAutoCommit() throws ServiceException {
		try {
			return this.getConnection().getAutoCommit();
		} catch (SQLException e) {
			throw new ServiceException("Errore nell'identificazione dello stato di autocommit.", e);
		}
	}
	
	public boolean isClosed() throws ServiceException {
		if(this.father != null) {
			return this.father.isClosed();
		}
		return this.isClosed;
	}
	
	protected void emitAudit(BasicModel model){
		if(this.father != null) {
			this.father.emitAudit(model);
		} else {
			if(this.idOperatore != null) {
				AuditBD db = new AuditBD(this);
				db.setAtomica(false);
				db.insertAudit(this.getIdOperatore(), model);
			}
		}
	}

	public long getIdOperatore() {
		if(this.father != null) {
			return this.father.getIdOperatore();
		}
		return this.idOperatore;
	}

	public void setIdOperatore(long idOperatore) {
		this.idOperatore = idOperatore;
	}

	public boolean isUseCache() {
		if(this.father != null) {
			return this.father.isUseCache();
		}
		return useCache;
	}
	
	public boolean isSelectForUpdate() {
		if(this.father != null) {
			return this.father.isSelectForUpdate();
		}
		return isSelectForUpdate;
	}

	public JDBC_SQLObjectFactory getJdbcSqlObjectFactory() {
		if(this.father != null) {
			return this.father.getJdbcSqlObjectFactory();
		}
		return jdbcSqlObjectFactory;
	}
	
	public static <T> T getValueOrNull(Object object, Class<T> returnType) {
		
		if(object != null && returnType.isInstance(object)) {
			return returnType.cast(object);
		}
		
		return null;
	}

	public boolean isAtomica() {
//		if(this.father != null) {
//			return this.father.isAtomica();
//		}
		return isAtomica;
	}

	public void setAtomica(boolean isAtomica) {
//		if(this.father != null) {
//			this.father.setAtomica(isAtomica);
//		}
		this.isAtomica = isAtomica;
	}
	
}
