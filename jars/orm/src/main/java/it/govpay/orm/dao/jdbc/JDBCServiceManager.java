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

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.utils.ServiceManagerProperties;
import org.slf4j.Logger;

import it.govpay.orm.dao.IACLService;
import it.govpay.orm.dao.IACLServiceSearch;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.IApplicazioneServiceSearch;
import it.govpay.orm.dao.IAuditService;
import it.govpay.orm.dao.IAuditServiceSearch;
import it.govpay.orm.dao.IBatchService;
import it.govpay.orm.dao.IBatchServiceSearch;
import it.govpay.orm.dao.IConfigurazioneService;
import it.govpay.orm.dao.IConfigurazioneServiceSearch;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IConnettoreServiceSearch;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.IDominioServiceSearch;
import it.govpay.orm.dao.IEsitoAvvisaturaService;
import it.govpay.orm.dao.IEsitoAvvisaturaServiceSearch;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.IEventoServiceSearch;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.IFRServiceSearch;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IIUVServiceSearch;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.IIbanAccreditoServiceSearch;
import it.govpay.orm.dao.IIncassoService;
import it.govpay.orm.dao.IIncassoServiceSearch;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.IIntermediarioServiceSearch;
import it.govpay.orm.dao.INotificaService;
import it.govpay.orm.dao.INotificaServiceSearch;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IOperatoreServiceSearch;
import it.govpay.orm.dao.IOperazioneService;
import it.govpay.orm.dao.IOperazioneServiceSearch;
import it.govpay.orm.dao.IPagamentoPortaleService;
import it.govpay.orm.dao.IPagamentoPortaleServiceSearch;
import it.govpay.orm.dao.IPagamentoPortaleVersamentoService;
import it.govpay.orm.dao.IPagamentoPortaleVersamentoServiceSearch;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.IPagamentoServiceSearch;
import it.govpay.orm.dao.IPromemoriaService;
import it.govpay.orm.dao.IPromemoriaServiceSearch;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.IRPTServiceSearch;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IRRServiceSearch;
import it.govpay.orm.dao.IRendicontazionePagamentoServiceSearch;
import it.govpay.orm.dao.IRendicontazioneService;
import it.govpay.orm.dao.IRendicontazioneServiceSearch;
import it.govpay.orm.dao.IServiceManager;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.ISingoloVersamentoServiceSearch;
import it.govpay.orm.dao.IStampaService;
import it.govpay.orm.dao.IStampaServiceSearch;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.IStazioneServiceSearch;
import it.govpay.orm.dao.ITipoTributoService;
import it.govpay.orm.dao.ITipoTributoServiceSearch;
import it.govpay.orm.dao.ITipoVersamentoDominioService;
import it.govpay.orm.dao.ITipoVersamentoDominioServiceSearch;
import it.govpay.orm.dao.ITipoVersamentoService;
import it.govpay.orm.dao.ITipoVersamentoServiceSearch;
import it.govpay.orm.dao.ITracciatoService;
import it.govpay.orm.dao.ITracciatoServiceSearch;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.ITributoServiceSearch;
import it.govpay.orm.dao.IUoService;
import it.govpay.orm.dao.IUoServiceSearch;
import it.govpay.orm.dao.IUtenzaDominioService;
import it.govpay.orm.dao.IUtenzaDominioServiceSearch;
import it.govpay.orm.dao.IUtenzaService;
import it.govpay.orm.dao.IUtenzaServiceSearch;
import it.govpay.orm.dao.IUtenzaTipoVersamentoService;
import it.govpay.orm.dao.IUtenzaTipoVersamentoServiceSearch;
import it.govpay.orm.dao.IVersamentoIncassoServiceSearch;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.IVersamentoServiceSearch;
import it.govpay.orm.dao.IVistaEventiPagamentoService;
import it.govpay.orm.dao.IVistaEventiPagamentoServiceSearch;
import it.govpay.orm.dao.IVistaEventiRptService;
import it.govpay.orm.dao.IVistaEventiRptServiceSearch;
import it.govpay.orm.dao.IVistaEventiVersamentoService;
import it.govpay.orm.dao.IVistaEventiVersamentoServiceSearch;
import it.govpay.orm.dao.IVistaPagamentoPortaleServiceSearch;
import it.govpay.orm.dao.IVistaRendicontazioneServiceSearch;
import it.govpay.orm.dao.IVistaRiscossioniServiceSearch;

/**     
 * Manager that allows you to obtain the services of research and management of objects
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class JDBCServiceManager extends org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManager implements IServiceManager {

	protected Connection get_Connection() throws ServiceException {
		return this.connection;
	}
	protected DataSource get_Datasource() throws ServiceException {
		return this.datasource;
	}
	protected JDBCServiceManagerProperties get_JdbcProperties(){
		return this.jdbcProperties;
	}
	protected Logger get_Logger(){
		return this.log;
	}
	@Override
	protected Connection getConnection() throws ServiceException {
		return super.getConnection();
	}
	@Override
	protected void closeConnection(Connection connection) throws ServiceException {
		super.closeConnection(connection);
	}

	protected JDBCServiceManager(){}

	public JDBCServiceManager(String jndiName, Properties contextJNDI,
			ServiceManagerProperties smProperties) throws ServiceException {
		super(jndiName, contextJNDI, smProperties);
	}
	public JDBCServiceManager(String jndiName, Properties contextJNDI,
			JDBCServiceManagerProperties jdbcProperties) throws ServiceException {
		super(jndiName, contextJNDI, jdbcProperties);
	}
	public JDBCServiceManager(String jndiName, Properties contextJNDI,
			ServiceManagerProperties smProperties, Logger alog) throws ServiceException {
		super(jndiName, contextJNDI, smProperties, alog);
	}
	public JDBCServiceManager(String jndiName, Properties contextJNDI,
			JDBCServiceManagerProperties jdbcProperties, Logger alog) throws ServiceException {
		super(jndiName, contextJNDI, jdbcProperties, alog);
	}
	
	
	public JDBCServiceManager(DataSource ds, ServiceManagerProperties smProperties)
			throws ServiceException {
		super(ds, smProperties);
	}
	public JDBCServiceManager(DataSource ds, JDBCServiceManagerProperties jdbcProperties)
			throws ServiceException {
		super(ds, jdbcProperties);
	}
	public JDBCServiceManager(DataSource ds, ServiceManagerProperties smProperties, Logger alog)
			throws ServiceException {
		super(ds, smProperties, alog);
	}
	public JDBCServiceManager(DataSource ds, JDBCServiceManagerProperties jdbcProperties, Logger alog)
			throws ServiceException {
		super(ds, jdbcProperties, alog);
	}
	
	
	public JDBCServiceManager(String connectionUrl, String driverJDBC,
			String username, String password, ServiceManagerProperties smProperties)
			throws ServiceException {
		super(connectionUrl, driverJDBC, username, password, smProperties);
	}
	public JDBCServiceManager(String connectionUrl, String driverJDBC,
			String username, String password, JDBCServiceManagerProperties jdbcProperties)
			throws ServiceException {
		super(connectionUrl, driverJDBC, username, password, jdbcProperties);
	}
	public JDBCServiceManager(String connectionUrl, String driverJDBC,
			String username, String password, ServiceManagerProperties smProperties, Logger alog)
			throws ServiceException {
		super(connectionUrl, driverJDBC, username, password, smProperties, alog);
	}
	public JDBCServiceManager(String connectionUrl, String driverJDBC,
			String username, String password, JDBCServiceManagerProperties jdbcProperties, Logger alog)
			throws ServiceException {
		super(connectionUrl, driverJDBC, username, password, jdbcProperties, alog);
	}
	
	
	public JDBCServiceManager(Connection connection, ServiceManagerProperties smProperties)
			throws ServiceException {
		super(connection, smProperties);
	}
	public JDBCServiceManager(Connection connection, JDBCServiceManagerProperties jdbcProperties)
			throws ServiceException {
		super(connection, jdbcProperties);
	}
	public JDBCServiceManager(Connection connection, ServiceManagerProperties smProperties, Logger alog) throws ServiceException {
		super(connection, smProperties, alog);
	}
	public JDBCServiceManager(Connection connection, JDBCServiceManagerProperties jdbcProperties, Logger alog) throws ServiceException {
		super(connection, jdbcProperties, alog);
	}


	


	/*
	 =====================================================================================================================
	 Services relating to the object with name:Configurazione type:Configurazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Configurazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Configurazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IConfigurazioneServiceSearch getConfigurazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCConfigurazioneServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Configurazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Configurazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IConfigurazioneService getConfigurazioneService() throws ServiceException,NotImplementedException{
		return new JDBCConfigurazioneService(this);
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
		return new JDBCDominioServiceSearch(this);
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
		return new JDBCDominioService(this);
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
		return new JDBCUoServiceSearch(this);
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
		return new JDBCUoService(this);
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
		return new JDBCConnettoreServiceSearch(this);
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
		return new JDBCConnettoreService(this);
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
		return new JDBCIntermediarioServiceSearch(this);
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
		return new JDBCIntermediarioService(this);
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
		return new JDBCStazioneServiceSearch(this);
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
		return new JDBCStazioneService(this);
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
		return new JDBCApplicazioneServiceSearch(this);
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
		return new JDBCApplicazioneService(this);
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
		return new JDBCIbanAccreditoServiceSearch(this);
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
		return new JDBCIbanAccreditoService(this);
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
		return new JDBCTributoServiceSearch(this);
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
		return new JDBCTributoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:TipoTributo type:TipoTributo
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
		return new JDBCTipoTributoServiceSearch(this);
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
		return new JDBCTipoTributoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Audit type:Audit
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Audit}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Audit}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IAuditServiceSearch getAuditServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCAuditServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Audit}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Audit}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IAuditService getAuditService() throws ServiceException,NotImplementedException{
		return new JDBCAuditService(this);
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
		return new JDBCACLServiceSearch(this);
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
		return new JDBCACLService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:UtenzaDominio type:UtenzaDominio
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaDominioServiceSearch getUtenzaDominioServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaDominioServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaDominioService getUtenzaDominioService() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaDominioService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:UtenzaTipoVersamento type:UtenzaTipoVersamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaTipoVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaTipoVersamentoServiceSearch getUtenzaTipoVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaTipoVersamentoServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaTipoVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaTipoVersamentoService getUtenzaTipoVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaTipoVersamentoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Utenza type:Utenza
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Utenza}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Utenza}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaServiceSearch getUtenzaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Utenza}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Utenza}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IUtenzaService getUtenzaService() throws ServiceException,NotImplementedException{
		return new JDBCUtenzaService(this);
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
		return new JDBCOperatoreServiceSearch(this);
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
		return new JDBCOperatoreService(this);
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
		return new JDBCIUVServiceSearch(this);
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
		return new JDBCIUVService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:TipoVersamento type:TipoVersamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.TipoVersamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.TipoVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoVersamentoServiceSearch getTipoVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTipoVersamentoServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoVersamentoService getTipoVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCTipoVersamentoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:TipoVersamentoDominio type:TipoVersamentoDominio
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.TipoVersamentoDominio}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.TipoVersamentoDominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoVersamentoDominioServiceSearch getTipoVersamentoDominioServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTipoVersamentoDominioServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoVersamentoDominio}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoVersamentoDominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITipoVersamentoDominioService getTipoVersamentoDominioService() throws ServiceException,NotImplementedException{
		return new JDBCTipoVersamentoDominioService(this);
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
		return new JDBCVersamentoServiceSearch(this);
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
		return new JDBCVersamentoService(this);
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
		return new JDBCEventoServiceSearch(this);
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
		return new JDBCEventoService(this);
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
		return new JDBCSingoloVersamentoServiceSearch(this);
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
		return new JDBCSingoloVersamentoService(this);
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
		return new JDBCRPTServiceSearch(this);
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
		return new JDBCRPTService(this);
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
		return new JDBCRRServiceSearch(this);
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
		return new JDBCRRService(this);
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
		return new JDBCRendicontazioneServiceSearch(this);
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
		return new JDBCRendicontazioneService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:PagamentoPortaleVersamento type:PagamentoPortaleVersamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoPortaleVersamentoServiceSearch getPagamentoPortaleVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoPortaleVersamentoServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoPortaleVersamentoService getPagamentoPortaleVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoPortaleVersamentoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:PagamentoPortale type:PagamentoPortale
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoPortaleServiceSearch getPagamentoPortaleServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoPortaleServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPagamentoPortaleService getPagamentoPortaleService() throws ServiceException,NotImplementedException{
		return new JDBCPagamentoPortaleService(this);
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
		return new JDBCPagamentoServiceSearch(this);
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
		return new JDBCPagamentoService(this);
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
		return new JDBCNotificaServiceSearch(this);
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
		return new JDBCNotificaService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Promemoria type:Promemoria
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Promemoria}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Promemoria}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPromemoriaServiceSearch getPromemoriaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCPromemoriaServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Promemoria}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Promemoria}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IPromemoriaService getPromemoriaService() throws ServiceException,NotImplementedException{
		return new JDBCPromemoriaService(this);
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
		return new JDBCIncassoServiceSearch(this);
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
		return new JDBCIncassoService(this);
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
		return new JDBCFRServiceSearch(this);
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
		return new JDBCFRService(this);
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
		return new JDBCRendicontazionePagamentoServiceSearch(this);
	}
	
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaRendicontazione type:VistaRendicontazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.VistaRendicontazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.VistaRendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaRendicontazioneServiceSearch getVistaRendicontazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaRendicontazioneServiceSearch(this);
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
		return new JDBCBatchServiceSearch(this);
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
		return new JDBCBatchService(this);
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
		return new JDBCOperazioneServiceSearch(this);
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
		return new JDBCOperazioneService(this);
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
		return new JDBCTracciatoServiceSearch(this);
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
		return new JDBCTracciatoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:EsitoAvvisatura type:EsitoAvvisatura
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.EsitoAvvisatura}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.EsitoAvvisatura}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEsitoAvvisaturaServiceSearch getEsitoAvvisaturaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCEsitoAvvisaturaServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.EsitoAvvisatura}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.EsitoAvvisatura}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEsitoAvvisaturaService getEsitoAvvisaturaService() throws ServiceException,NotImplementedException{
		return new JDBCEsitoAvvisaturaService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Stampa type:Stampa
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Stampa}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Stampa}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IStampaServiceSearch getStampaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCStampaServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stampa}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stampa}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IStampaService getStampaService() throws ServiceException,NotImplementedException{
		return new JDBCStampaService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VersamentoIncasso type:VersamentoIncasso
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.VersamentoIncasso}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.VersamentoIncasso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVersamentoIncassoServiceSearch getVersamentoIncassoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVersamentoIncassoServiceSearch(this);
	}
	
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaRiscossioni type:VistaRiscossioni
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.VistaRiscossioni}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.VistaRiscossioni}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaRiscossioniServiceSearch getVistaRiscossioniServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaRiscossioniServiceSearch(this);
	}
	
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaEventiVersamento type:Evento
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
	public IVistaEventiVersamentoServiceSearch getVistaEventiVersamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiVersamentoServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaEventiVersamentoService getVistaEventiVersamentoService() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiVersamentoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaEventiPagamento type:Evento
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
	public IVistaEventiPagamentoServiceSearch getVistaEventiPagamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiPagamentoServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaEventiPagamentoService getVistaEventiPagamentoService() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiPagamentoService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaEventiRpt type:Evento
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
	public IVistaEventiRptServiceSearch getVistaEventiRptServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiRptServiceSearch(this);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaEventiRptService getVistaEventiRptService() throws ServiceException,NotImplementedException{
		return new JDBCVistaEventiRptService(this);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:VistaPagamentoPortale type:VistaPagamentoPortale
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.VistaPagamentoPortale}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.VistaPagamentoPortale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IVistaPagamentoPortaleServiceSearch getVistaPagamentoPortaleServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCVistaPagamentoPortaleServiceSearch(this);
	}
	
	
	
	
	

}
