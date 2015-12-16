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

import it.govpay.orm.dao.IAnagraficaService;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.ICanaleService;
import it.govpay.orm.dao.ICarrelloService;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IContoAccreditoService;
import it.govpay.orm.dao.IDBAnagraficaService;
import it.govpay.orm.dao.IDBApplicazioneService;
import it.govpay.orm.dao.IDBCanaleService;
import it.govpay.orm.dao.IDBCarrelloService;
import it.govpay.orm.dao.IDBConnettoreService;
import it.govpay.orm.dao.IDBContoAccreditoService;
import it.govpay.orm.dao.IDBDominioService;
import it.govpay.orm.dao.IDBERService;
import it.govpay.orm.dao.IDBEnteService;
import it.govpay.orm.dao.IDBEsitoService;
import it.govpay.orm.dao.IDBEventoService;
import it.govpay.orm.dao.IDBFRService;
import it.govpay.orm.dao.IDBIUVService;
import it.govpay.orm.dao.IDBIbanAccreditoService;
import it.govpay.orm.dao.IDBIntermediarioService;
import it.govpay.orm.dao.IDBMailService;
import it.govpay.orm.dao.IDBMailTemplateService;
import it.govpay.orm.dao.IDBMediaRilevamentoService;
import it.govpay.orm.dao.IDBOperatoreService;
import it.govpay.orm.dao.IDBPortaleService;
import it.govpay.orm.dao.IDBPspService;
import it.govpay.orm.dao.IDBRPTService;
import it.govpay.orm.dao.IDBRRService;
import it.govpay.orm.dao.IDBRTService;
import it.govpay.orm.dao.IDBRilevamentoService;
import it.govpay.orm.dao.IDBSLAService;
import it.govpay.orm.dao.IDBSingolaRendicontazioneService;
import it.govpay.orm.dao.IDBSingolaRevocaService;
import it.govpay.orm.dao.IDBSingoloVersamentoService;
import it.govpay.orm.dao.IDBStazioneService;
import it.govpay.orm.dao.IDBTabellaContropartiService;
import it.govpay.orm.dao.IDBTracciatoXMLService;
import it.govpay.orm.dao.IDBTributoService;
import it.govpay.orm.dao.IDBVersamentoService;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IERService;
import it.govpay.orm.dao.IEnteService;
import it.govpay.orm.dao.IEsitoService;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.IMailService;
import it.govpay.orm.dao.IMailTemplateService;
import it.govpay.orm.dao.IMediaRilevamentoService;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IPortaleService;
import it.govpay.orm.dao.IPspService;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IRTService;
import it.govpay.orm.dao.IRilevamentoService;
import it.govpay.orm.dao.ISLAService;
import it.govpay.orm.dao.ISingolaRendicontazioneService;
import it.govpay.orm.dao.ISingolaRevocaService;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.ITabellaContropartiService;
import it.govpay.orm.dao.ITracciatoXMLService;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class BasicBD {
	
	private JDBCServiceManager serviceManager;
	private JDBCServiceManagerProperties jdbcProperties;
	
	private IAnagraficaService anagraficaService;
	private IApplicazioneService applicazioneService;
	private ICanaleService canaleService;
	private ICarrelloService carrelloService;
	private IConnettoreService connettoreService;
	private IContoAccreditoService contoAccreditoService;
	private IDominioService dominioService;
	private IEnteService enteService;
	private IERService erService;
	private IEsitoService esitoService;
	private IEventoService eventoService;
	private IFRService frService;
	private IIbanAccreditoService ibanAccreditoService;
	private IIntermediarioService intermediarioService;
	private IIUVService iuvService;
	private IMailService mailService;
	private IMailTemplateService mailTemplateService;
	private IMediaRilevamentoService mediaRilevamentoService;
	private IOperatoreService operatoreService;
	private IPortaleService portaleService;
	private IPspService pspService;
	private IRilevamentoService rilevamentoService;
	private IRPTService rptService;
	private IRRService rrService;
	private IRTService rtService;
	private ISingolaRendicontazioneService singolaRendicontazioneService;
	private ISingolaRevocaService singolaRevocaService;
	private ISingoloVersamentoService singoloVersamentoService;
	private ISLAService slaService;
	private IStazioneService stazioneService;
	private ITabellaContropartiService tabellaContropartiService;
	private ITracciatoXMLService tracciatoXMLService;
	private ITributoService tributoService;
	private IVersamentoService versamentoService;
	
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
			this.jdbcProperties = this.serviceManager.getJdbcProperties();
			
			try {
				this.anagraficaService = this.serviceManager.getAnagraficaService();
				this.applicazioneService = this.serviceManager.getApplicazioneService();
				this.canaleService = this.serviceManager.getCanaleService();
				this.carrelloService = this.serviceManager.getCarrelloService();
				this.connettoreService = this.serviceManager.getConnettoreService();
				this.contoAccreditoService = this.serviceManager.getContoAccreditoService();
				this.dominioService = this.serviceManager.getDominioService();
				this.enteService = this.serviceManager.getEnteService();
				this.erService = this.serviceManager.getERService();
				this.esitoService = this.serviceManager.getEsitoService();
				this.eventoService = this.serviceManager.getEventoService();
				this.frService = this.serviceManager.getFRService();
				this.ibanAccreditoService = this.serviceManager.getIbanAccreditoService();
				this.intermediarioService = this.serviceManager.getIntermediarioService();
				this.iuvService = this.serviceManager.getIUVService();
				this.mailService = this.serviceManager.getMailService();
				this.mailTemplateService = this.serviceManager.getMailTemplateService();
				this.mediaRilevamentoService = this.serviceManager.getMediaRilevamentoService();
				this.operatoreService = this.serviceManager.getOperatoreService();
				this.portaleService = this.serviceManager.getPortaleService();
				this.pspService = this.serviceManager.getPspService();
				this.rilevamentoService = this.serviceManager.getRilevamentoService();
				this.rptService = this.serviceManager.getRPTService();
				this.rrService = this.serviceManager.getRRService();
				this.rtService = this.serviceManager.getRTService();
				this.singolaRendicontazioneService = this.serviceManager.getSingolaRendicontazioneService();
				this.singolaRevocaService = this.serviceManager.getSingolaRevocaService();
				this.singoloVersamentoService = this.serviceManager.getSingoloVersamentoService();
				this.slaService = this.serviceManager.getSLAService();
				this.stazioneService = this.serviceManager.getStazioneService();
				this.tabellaContropartiService = this.serviceManager.getTabellaContropartiService();
				this.tracciatoXMLService = this.serviceManager.getTracciatoXMLService();
				this.tributoService = this.serviceManager.getTributoService();
				this.versamentoService = this.serviceManager.getVersamentoService();
			} catch(NotImplementedException e) {
				throw new ServiceException(e);
			}
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

	
//	public JDBCServiceManager getServiceManager() throws ServiceException{
//		if(father != null) {
//			return father.getServiceManager();
//		}
//		return this.serviceManager;
//	}

	
	public void enableSelectForUpdate() throws ServiceException {
		if(this.father != null) {
			this.father.enableSelectForUpdate();
			return;
		}
		try {
			((IDBAnagraficaService)this.anagraficaService).enableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).enableSelectForUpdate();
			((IDBCanaleService)this.canaleService).enableSelectForUpdate();
			((IDBCarrelloService)this.carrelloService).enableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).enableSelectForUpdate();
			((IDBContoAccreditoService)this.contoAccreditoService).enableSelectForUpdate();
			((IDBDominioService)this.dominioService).enableSelectForUpdate();
			((IDBEnteService)this.enteService).enableSelectForUpdate();
			((IDBERService)this.erService).enableSelectForUpdate();
			((IDBEsitoService)this.esitoService).enableSelectForUpdate();
			((IDBEventoService)this.eventoService).enableSelectForUpdate();
			((IDBFRService)this.frService).enableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).enableSelectForUpdate();
			((IDBIntermediarioService)this.intermediarioService).enableSelectForUpdate();
			((IDBIUVService)this.iuvService).enableSelectForUpdate();
			((IDBMailService)this.mailService).enableSelectForUpdate();
			((IDBMailTemplateService)this.mailTemplateService).enableSelectForUpdate();
			((IDBMediaRilevamentoService)this.mediaRilevamentoService).enableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).enableSelectForUpdate();
			((IDBPortaleService)this.portaleService).enableSelectForUpdate();
			((IDBPspService)this.pspService).enableSelectForUpdate();
			((IDBRilevamentoService)this.rilevamentoService).enableSelectForUpdate();
			((IDBRPTService)this.rptService).enableSelectForUpdate();
			((IDBRRService)this.rrService).enableSelectForUpdate();
			((IDBRTService)this.rtService).enableSelectForUpdate();
			((IDBSingolaRendicontazioneService)this.singolaRendicontazioneService).enableSelectForUpdate();
			((IDBSingolaRevocaService)this.singolaRevocaService).enableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).enableSelectForUpdate();
			((IDBSLAService)this.slaService).enableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).enableSelectForUpdate();
			((IDBTabellaContropartiService)this.tabellaContropartiService).enableSelectForUpdate();
			((IDBTracciatoXMLService)this.tracciatoXMLService).enableSelectForUpdate();
			((IDBTributoService)this.tributoService).enableSelectForUpdate();
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
			((IDBAnagraficaService)this.anagraficaService).disableSelectForUpdate();
			((IDBApplicazioneService)this.applicazioneService).disableSelectForUpdate();
			((IDBCanaleService)this.canaleService).disableSelectForUpdate();
			((IDBCarrelloService)this.carrelloService).disableSelectForUpdate();
			((IDBConnettoreService)this.connettoreService).disableSelectForUpdate();
			((IDBContoAccreditoService)this.contoAccreditoService).disableSelectForUpdate();
			((IDBDominioService)this.dominioService).disableSelectForUpdate();
			((IDBEnteService)this.enteService).disableSelectForUpdate();
			((IDBERService)this.erService).disableSelectForUpdate();
			((IDBEsitoService)this.esitoService).disableSelectForUpdate();
			((IDBEventoService)this.eventoService).disableSelectForUpdate();
			((IDBFRService)this.frService).disableSelectForUpdate();
			((IDBIbanAccreditoService)this.ibanAccreditoService).disableSelectForUpdate();
			((IDBIntermediarioService)this.intermediarioService).disableSelectForUpdate();
			((IDBIUVService)this.iuvService).disableSelectForUpdate();
			((IDBMailService)this.mailService).disableSelectForUpdate();
			((IDBMailTemplateService)this.mailTemplateService).disableSelectForUpdate();
			((IDBMediaRilevamentoService)this.mediaRilevamentoService).disableSelectForUpdate();
			((IDBOperatoreService)this.operatoreService).disableSelectForUpdate();
			((IDBPortaleService)this.portaleService).disableSelectForUpdate();
			((IDBPspService)this.pspService).disableSelectForUpdate();
			((IDBRilevamentoService)this.rilevamentoService).disableSelectForUpdate();
			((IDBRPTService)this.rptService).disableSelectForUpdate();
			((IDBRRService)this.rrService).disableSelectForUpdate();
			((IDBRTService)this.rtService).disableSelectForUpdate();
			((IDBSingolaRendicontazioneService)this.singolaRendicontazioneService).disableSelectForUpdate();
			((IDBSingolaRevocaService)this.singolaRevocaService).disableSelectForUpdate();
			((IDBSingoloVersamentoService)this.singoloVersamentoService).disableSelectForUpdate();
			((IDBSLAService)this.slaService).disableSelectForUpdate();
			((IDBStazioneService)this.stazioneService).disableSelectForUpdate();
			((IDBTabellaContropartiService)this.tabellaContropartiService).disableSelectForUpdate();
			((IDBTracciatoXMLService)this.tracciatoXMLService).disableSelectForUpdate();
			((IDBTributoService)this.tributoService).disableSelectForUpdate();
			((IDBVersamentoService)this.versamentoService).disableSelectForUpdate();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public IAnagraficaService getAnagraficaService() {
		if(father != null) {
			return father.getAnagraficaService();
		}
		return anagraficaService;
	}

	public IApplicazioneService getApplicazioneService() {
		if(father != null) {
			return father.getApplicazioneService();
		}
		return applicazioneService;
	}

	public ICanaleService getCanaleService() {
		if(father != null) {
			return father.getCanaleService();
		}
		return canaleService;
	}

	public ICarrelloService getCarrelloService() {
		if(father != null) {
			return father.getCarrelloService();
		}
		return carrelloService;
	}

	public IConnettoreService getConnettoreService() {
		if(father != null) {
			return father.getConnettoreService();
		}
		return connettoreService;
	}

	public IContoAccreditoService getContoAccreditoService() {
		if(father != null) {
			return father.getContoAccreditoService();
		}
		return contoAccreditoService;
	}

	public IDominioService getDominioService() {
		if(father != null) {
			return father.getDominioService();
		}
		return dominioService;
	}

	public IEnteService getEnteService() {
		if(father != null) {
			return father.getEnteService();
		}
		return enteService;
	}

	public IERService getErService() {
		if(father != null) {
			return father.getErService();
		}
		return erService;
	}

	public IEsitoService getEsitoService() {
		if(father != null) {
			return father.getEsitoService();
		}
		return esitoService;
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

	public IMailService getMailService() {
		if(father != null) {
			return father.getMailService();
		}
		return mailService;
	}

	public IMailTemplateService getMailTemplateService() {
		if(father != null) {
			return father.getMailTemplateService();
		}
		return mailTemplateService;
	}

	public IMediaRilevamentoService getMediaRilevamentoService() {
		if(father != null) {
			return father.getMediaRilevamentoService();
		}
		return mediaRilevamentoService;
	}

	public IOperatoreService getOperatoreService() {
		if(father != null) {
			return father.getOperatoreService();
		}
		return operatoreService;
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

	public IRilevamentoService getRilevamentoService() {
		if(father != null) {
			return father.getRilevamentoService();
		}
		return rilevamentoService;
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

	public IRTService getRtService() {
		if(father != null) {
			return father.getRtService();
		}
		return rtService;
	}

	public ISingolaRendicontazioneService getSingolaRendicontazioneService() {
		if(father != null) {
			return father.getSingolaRendicontazioneService();
		}
		return singolaRendicontazioneService;
	}

	public ISingolaRevocaService getSingolaRevocaService() {
		if(father != null) {
			return father.getSingolaRevocaService();
		}
		return singolaRevocaService;
	}

	public ISingoloVersamentoService getSingoloVersamentoService() {
		if(father != null) {
			return father.getSingoloVersamentoService();
		}
		return singoloVersamentoService;
	}

	public ISLAService getSlaService() {
		if(father != null) {
			return father.getSlaService();
		}
		return slaService;
	}

	public IStazioneService getStazioneService() {
		if(father != null) {
			return father.getStazioneService();
		}
		return stazioneService;
	}

	public ITabellaContropartiService getTabellaContropartiService() {
		if(father != null) {
			return father.getTabellaContropartiService();
		}
		return tabellaContropartiService;
	}

	public ITracciatoXMLService getTracciatoXMLService() {
		if(father != null) {
			return father.getTracciatoXMLService();
		}
		return tracciatoXMLService;
	}

	public ITributoService getTributoService() {
		if(father != null) {
			return father.getTributoService();
		}
		return tributoService;
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

	public JDBCServiceManagerProperties getJdbcProperties() {
		if(father != null) {
			return father.getJdbcProperties();
		}
		return jdbcProperties;
	}

}
