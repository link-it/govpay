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
package it.govpay.orm.dao.jdbc;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.Connection;

import javax.sql.DataSource;

import it.govpay.orm.dao.ITracciatoXMLServiceSearch;
import it.govpay.orm.dao.ITracciatoXMLService;
import it.govpay.orm.dao.IDominioServiceSearch;
import it.govpay.orm.dao.IDominioService;
import it.govpay.orm.dao.ITabellaContropartiServiceSearch;
import it.govpay.orm.dao.ITabellaContropartiService;
import it.govpay.orm.dao.IContoAccreditoServiceSearch;
import it.govpay.orm.dao.IContoAccreditoService;
import it.govpay.orm.dao.IPspServiceSearch;
import it.govpay.orm.dao.IPspService;
import it.govpay.orm.dao.ICanaleServiceSearch;
import it.govpay.orm.dao.ICanaleService;
import it.govpay.orm.dao.IAnagraficaServiceSearch;
import it.govpay.orm.dao.IAnagraficaService;
import it.govpay.orm.dao.IMailTemplateServiceSearch;
import it.govpay.orm.dao.IMailTemplateService;
import it.govpay.orm.dao.IEnteServiceSearch;
import it.govpay.orm.dao.IEnteService;
import it.govpay.orm.dao.IPortaleServiceSearch;
import it.govpay.orm.dao.IPortaleService;
import it.govpay.orm.dao.IConnettoreServiceSearch;
import it.govpay.orm.dao.IConnettoreService;
import it.govpay.orm.dao.IIntermediarioServiceSearch;
import it.govpay.orm.dao.IIntermediarioService;
import it.govpay.orm.dao.IStazioneServiceSearch;
import it.govpay.orm.dao.IStazioneService;
import it.govpay.orm.dao.IApplicazioneServiceSearch;
import it.govpay.orm.dao.IApplicazioneService;
import it.govpay.orm.dao.IIbanAccreditoServiceSearch;
import it.govpay.orm.dao.IIbanAccreditoService;
import it.govpay.orm.dao.ITributoServiceSearch;
import it.govpay.orm.dao.ITributoService;
import it.govpay.orm.dao.IOperatoreServiceSearch;
import it.govpay.orm.dao.IOperatoreService;
import it.govpay.orm.dao.IIUVServiceSearch;
import it.govpay.orm.dao.IIUVService;
import it.govpay.orm.dao.IVersamentoServiceSearch;
import it.govpay.orm.dao.IVersamentoService;
import it.govpay.orm.dao.ISingoloVersamentoServiceSearch;
import it.govpay.orm.dao.ISingoloVersamentoService;
import it.govpay.orm.dao.IRPTServiceSearch;
import it.govpay.orm.dao.IRPTService;
import it.govpay.orm.dao.ICarrelloServiceSearch;
import it.govpay.orm.dao.ICarrelloService;
import it.govpay.orm.dao.IRTServiceSearch;
import it.govpay.orm.dao.IRTService;
import it.govpay.orm.dao.IEsitoServiceSearch;
import it.govpay.orm.dao.IEsitoService;
import it.govpay.orm.dao.IEventoServiceSearch;
import it.govpay.orm.dao.IEventoService;
import it.govpay.orm.dao.ISLAServiceSearch;
import it.govpay.orm.dao.ISLAService;
import it.govpay.orm.dao.IRilevamentoServiceSearch;
import it.govpay.orm.dao.IRilevamentoService;
import it.govpay.orm.dao.IMediaRilevamentoServiceSearch;
import it.govpay.orm.dao.IMediaRilevamentoService;
import it.govpay.orm.dao.IRRServiceSearch;
import it.govpay.orm.dao.IRRService;
import it.govpay.orm.dao.IERServiceSearch;
import it.govpay.orm.dao.IERService;
import it.govpay.orm.dao.ISingolaRevocaServiceSearch;
import it.govpay.orm.dao.ISingolaRevocaService;
import it.govpay.orm.dao.IFRServiceSearch;
import it.govpay.orm.dao.IFRService;
import it.govpay.orm.dao.ISingolaRendicontazioneServiceSearch;
import it.govpay.orm.dao.ISingolaRendicontazioneService;
import it.govpay.orm.dao.IMailServiceSearch;
import it.govpay.orm.dao.IMailService;

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
	 Services relating to the object with name:TracciatoXML type:TracciatoXML
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.TracciatoXML}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.TracciatoXML}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITracciatoXMLServiceSearch getTracciatoXMLServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTracciatoXMLServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TracciatoXML}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TracciatoXML}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITracciatoXMLService getTracciatoXMLService() throws ServiceException,NotImplementedException{
		return new JDBCTracciatoXMLService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:TabellaControparti type:TabellaControparti
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.TabellaControparti}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.TabellaControparti}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITabellaContropartiServiceSearch getTabellaContropartiServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCTabellaContropartiServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TabellaControparti}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TabellaControparti}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ITabellaContropartiService getTabellaContropartiService() throws ServiceException,NotImplementedException{
		return new JDBCTabellaContropartiService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:ContoAccredito type:ContoAccredito
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.ContoAccredito}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.ContoAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IContoAccreditoServiceSearch getContoAccreditoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCContoAccreditoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ContoAccredito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ContoAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IContoAccreditoService getContoAccreditoService() throws ServiceException,NotImplementedException{
		return new JDBCContoAccreditoService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:Anagrafica type:Anagrafica
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Anagrafica}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Anagrafica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IAnagraficaServiceSearch getAnagraficaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCAnagraficaServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Anagrafica}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Anagrafica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IAnagraficaService getAnagraficaService() throws ServiceException,NotImplementedException{
		return new JDBCAnagraficaService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:MailTemplate type:MailTemplate
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.MailTemplate}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.MailTemplate}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMailTemplateServiceSearch getMailTemplateServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCMailTemplateServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.MailTemplate}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.MailTemplate}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMailTemplateService getMailTemplateService() throws ServiceException,NotImplementedException{
		return new JDBCMailTemplateService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Ente type:Ente
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Ente}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Ente}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEnteServiceSearch getEnteServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCEnteServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Ente}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Ente}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEnteService getEnteService() throws ServiceException,NotImplementedException{
		return new JDBCEnteService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:Carrello type:Carrello
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Carrello}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Carrello}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ICarrelloServiceSearch getCarrelloServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCCarrelloServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Carrello}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Carrello}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ICarrelloService getCarrelloService() throws ServiceException,NotImplementedException{
		return new JDBCCarrelloService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:RT type:RT
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.RT}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.RT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRTServiceSearch getRTServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRTServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RT}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRTService getRTService() throws ServiceException,NotImplementedException{
		return new JDBCRTService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Esito type:Esito
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Esito}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Esito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEsitoServiceSearch getEsitoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCEsitoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Esito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Esito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IEsitoService getEsitoService() throws ServiceException,NotImplementedException{
		return new JDBCEsitoService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:SLA type:SLA
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.SLA}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.SLA}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISLAServiceSearch getSLAServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCSLAServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SLA}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SLA}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISLAService getSLAService() throws ServiceException,NotImplementedException{
		return new JDBCSLAService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Rilevamento type:Rilevamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Rilevamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Rilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRilevamentoServiceSearch getRilevamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCRilevamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rilevamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IRilevamentoService getRilevamentoService() throws ServiceException,NotImplementedException{
		return new JDBCRilevamentoService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:MediaRilevamento type:MediaRilevamento
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMediaRilevamentoServiceSearch getMediaRilevamentoServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCMediaRilevamentoServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMediaRilevamentoService getMediaRilevamentoService() throws ServiceException,NotImplementedException{
		return new JDBCMediaRilevamentoService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:ER type:ER
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.ER}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.ER}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IERServiceSearch getERServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCERServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ER}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ER}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IERService getERService() throws ServiceException,NotImplementedException{
		return new JDBCERService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:SingolaRevoca type:SingolaRevoca
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingolaRevocaServiceSearch getSingolaRevocaServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCSingolaRevocaServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingolaRevocaService getSingolaRevocaService() throws ServiceException,NotImplementedException{
		return new JDBCSingolaRevocaService(this.unlimitedJdbcServiceManager);
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
	 Services relating to the object with name:SingolaRendicontazione type:SingolaRendicontazione
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingolaRendicontazioneServiceSearch getSingolaRendicontazioneServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCSingolaRendicontazioneServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public ISingolaRendicontazioneService getSingolaRendicontazioneService() throws ServiceException,NotImplementedException{
		return new JDBCSingolaRendicontazioneService(this.unlimitedJdbcServiceManager);
	}
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Mail type:Mail
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Mail}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Mail}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMailServiceSearch getMailServiceSearch() throws ServiceException,NotImplementedException{
		return new JDBCMailServiceSearch(this.unlimitedJdbcServiceManager);
	}
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Mail}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Mail}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	@Override
	public IMailService getMailService() throws ServiceException,NotImplementedException{
		return new JDBCMailService(this.unlimitedJdbcServiceManager);
	}
	
	
	
}