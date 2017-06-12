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
package it.govpay.orm.dao.jdbc;

import it.govpay.orm.dao.IACLService;
import it.govpay.orm.dao.IACLServiceSearch;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.IApplicazioneServiceSearch;
import it.govpay.orm.dao.ICanaleService;
import it.govpay.orm.dao.ICanaleServiceSearch;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IConnettoreServiceSearch;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IDominioServiceSearch;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IEventoServiceSearch;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IFRServiceSearch;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IIUVServiceSearch;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IIbanAccreditoServiceSearch;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.IIntermediarioServiceSearch;
import it.govpay.orm.dao.INotificaService;
import it.govpay.orm.dao.INotificaServiceSearch;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IOperatoreServiceSearch;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.IPagamentoServiceSearch;
import it.govpay.orm.dao.IPortaleService;
import it.govpay.orm.dao.IPortaleServiceSearch;
import it.govpay.orm.dao.IPspService;
import it.govpay.orm.dao.IPspServiceSearch;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IRPTServiceSearch;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IRRServiceSearch;
import it.govpay.orm.dao.IRendicontazionePagamentoServiceSearch;
import it.govpay.orm.dao.IRendicontazioneService;
import it.govpay.orm.dao.IRendicontazioneServiceSearch;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.ISingoloVersamentoServiceSearch;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.IStazioneServiceSearch;
import it.govpay.orm.dao.ITipoTributoService;
import it.govpay.orm.dao.ITipoTributoServiceSearch;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.ITributoServiceSearch;
import it.govpay.orm.dao.IUoService;
import it.govpay.orm.dao.IUoServiceSearch;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.IVersamentoServiceSearch;
import it.govpay.orm.dao.IBatchServiceSearch;
import it.govpay.orm.dao.IBatchService;
import java.sql.Connection;
import it.govpay.orm.dao.IOperazioneServiceSearch;
import it.govpay.orm.dao.IOperazioneService;
import it.govpay.orm.dao.ITracciatoServiceSearch;
import it.govpay.orm.dao.ITracciatoService;


import javax.sql.DataSource;
import it.govpay.orm.dao.IIncassoServiceSearch;
import it.govpay.orm.dao.IIncassoService;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

/**     
 * Manager that allows you to obtain the services of research and management of objects
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class JDBCLimitedServiceManager extends JDBCServiceManager {

	private JDBCServiceManager unlimitedJdbcServiceManager;

	public JDBCLimitedServiceManager(JDBCServiceManager jdbcServiceManager) throws ServiceException {
		this.datasource = jdbcServiceManager.get_Datasource();
		this.connection = jdbcServiceManager.get_Connection();
		this.log = jdbcServiceManager.get_Logger();
		this.jdbcProperties = jdbcServiceManager.get_JdbcProperties();
		this.unlimitedJdbcServiceManager = jdbcServiceManager;
	}
	
	
	@Override
	public Connection getConnection() throws ServiceException {
		throw new ServiceException("Connection managed from framework");
	}
	@Override
	public void closeConnection(Connection connection) throws ServiceException {
		throw new ServiceException("Connection managed from framework");
	}
	@Override
	protected Connection get_Connection() throws ServiceException {
		throw new ServiceException("Connection managed from framework");
	}
	@Override
	protected DataSource get_Datasource() throws ServiceException {
		throw new ServiceException("Connection managed from framework");
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Dominio type:Dominio
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Dominio}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Dominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IDominioServiceSearch getDominioServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCDominioServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Dominio}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Dominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IDominioService getDominioService() throws ServiceException,NotImplementedException{
		return new JDBCDominioService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Psp type:Psp
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Psp}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Psp}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPspServiceSearch getPspServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPspServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Psp}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Psp}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPspService getPspService() throws ServiceException,NotImplementedException{
		return new JDBCPspService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Canale type:Canale
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Canale}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Canale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ICanaleServiceSearch getCanaleServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCCanaleServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Canale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Canale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ICanaleService getCanaleService() throws ServiceException,NotImplementedException{
		return new JDBCCanaleService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Uo type:Uo
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Uo}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Uo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUoServiceSearch getUoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCUoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Uo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Uo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUoService getUoService() throws ServiceException,NotImplementedException{
		return new JDBCUoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Portale type:Portale
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Portale}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Portale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPortaleServiceSearch getPortaleServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPortaleServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Portale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Portale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPortaleService getPortaleService() throws ServiceException,NotImplementedException{
		return new JDBCPortaleService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Connettore type:Connettore
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Connettore}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Connettore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IConnettoreServiceSearch getConnettoreServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCConnettoreServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Connettore}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Connettore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IConnettoreService getConnettoreService() throws ServiceException,NotImplementedException{
		return new JDBCConnettoreService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Intermediario type:Intermediario
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Intermediario}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Intermediario}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIntermediarioServiceSearch getIntermediarioServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCIntermediarioServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Intermediario}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Intermediario}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIntermediarioService getIntermediarioService() throws ServiceException,NotImplementedException{
		return new JDBCIntermediarioService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Stazione type:Stazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Stazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Stazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IStazioneServiceSearch getStazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCStazioneServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IStazioneService getStazioneService() throws ServiceException,NotImplementedException{
		return new JDBCStazioneService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Applicazione type:Applicazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Applicazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Applicazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IApplicazioneServiceSearch getApplicazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCApplicazioneServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Applicazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Applicazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IApplicazioneService getApplicazioneService() throws ServiceException,NotImplementedException{
		return new JDBCApplicazioneService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:IbanAccredito type:IbanAccredito
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.IbanAccredito}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.IbanAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIbanAccreditoServiceSearch getIbanAccreditoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCIbanAccreditoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.IbanAccredito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.IbanAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIbanAccreditoService getIbanAccreditoService() throws ServiceException,NotImplementedException{
		return new JDBCIbanAccreditoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Tributo type:Tributo
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Tributo}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Tributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITributoServiceSearch getTributoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTributoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tributo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITributoService getTributoService() throws ServiceException,NotImplementedException{
		return new JDBCTributoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:tipoTributo type:TipoTributo
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.TipoTributo}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.TipoTributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoTributoServiceSearch getTipoTributoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTipoTributoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoTributo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoTributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoTributoService getTipoTributoService() throws ServiceException,NotImplementedException{
		return new JDBCTipoTributoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:ACL type:ACL
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.ACL}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.ACL}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IACLServiceSearch getACLServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCACLServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ACL}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ACL}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IACLService getACLService() throws ServiceException,NotImplementedException{
		return new JDBCACLService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Operatore type:Operatore
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Operatore}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Operatore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IOperatoreServiceSearch getOperatoreServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCOperatoreServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operatore}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operatore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IOperatoreService getOperatoreService() throws ServiceException,NotImplementedException{
		return new JDBCOperatoreService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:IUV type:IUV
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.IUV}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.IUV}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIUVServiceSearch getIUVServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCIUVServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.IUV}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.IUV}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIUVService getIUVService() throws ServiceException,NotImplementedException{
		return new JDBCIUVService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Versamento type:Versamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Versamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Versamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVersamentoServiceSearch getVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVersamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Versamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Versamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVersamentoService getVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCVersamentoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Evento type:Evento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEventoServiceSearch getEventoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCEventoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEventoService getEventoService() throws ServiceException,NotImplementedException{
		return new JDBCEventoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:SingoloVersamento type:SingoloVersamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingoloVersamentoServiceSearch getSingoloVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCSingoloVersamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingoloVersamentoService getSingoloVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCSingoloVersamentoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:RPT type:RPT
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.RPT}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.RPT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRPTServiceSearch getRPTServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRPTServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RPT}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RPT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRPTService getRPTService() throws ServiceException,NotImplementedException{
		return new JDBCRPTService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:RR type:RR
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.RR}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.RR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRRServiceSearch getRRServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRRServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RR}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRRService getRRService() throws ServiceException,NotImplementedException{
		return new JDBCRRService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Rendicontazione type:Rendicontazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Rendicontazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Rendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRendicontazioneServiceSearch getRendicontazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRendicontazioneServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rendicontazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRendicontazioneService getRendicontazioneService() throws ServiceException,NotImplementedException{
		return new JDBCRendicontazioneService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Pagamento type:Pagamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Pagamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Pagamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoServiceSearch getPagamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Pagamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Pagamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoService getPagamentoService() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Notifica type:Notifica
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Notifica}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Notifica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public INotificaServiceSearch getNotificaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCNotificaServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Notifica}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Notifica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public INotificaService getNotificaService() throws ServiceException,NotImplementedException{
		return new JDBCNotificaService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Incasso type:Incasso
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Incasso}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Incasso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIncassoServiceSearch getIncassoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCIncassoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Incasso}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Incasso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IIncassoService getIncassoService() throws ServiceException,NotImplementedException{
		return new JDBCIncassoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:FR type:FR
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.FR}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.FR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IFRServiceSearch getFRServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCFRServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.FR}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.FR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IFRService getFRService() throws ServiceException,NotImplementedException{
		return new JDBCFRService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:RendicontazionePagamento type:RendicontazionePagamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.RendicontazionePagamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.RendicontazionePagamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRendicontazionePagamentoServiceSearch getRendicontazionePagamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRendicontazionePagamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Batch type:Batch
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Batch}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Batch}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IBatchServiceSearch getBatchServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCBatchServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Batch}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Batch}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IBatchService getBatchService() throws ServiceException,NotImplementedException{
		return new JDBCBatchService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Operazione type:Operazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Operazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Operazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IOperazioneServiceSearch getOperazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCOperazioneServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IOperazioneService getOperazioneService() throws ServiceException,NotImplementedException{
		return new JDBCOperazioneService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Tracciato type:Tracciato
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Tracciato}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Tracciato}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITracciatoServiceSearch getTracciatoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTracciatoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tracciato}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tracciato}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITracciatoService getTracciatoService() throws ServiceException,NotImplementedException{
		return new JDBCTracciatoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
}