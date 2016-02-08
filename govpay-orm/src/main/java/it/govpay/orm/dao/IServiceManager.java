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
package it.govpay.orm.dao;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.NotImplementedException;


/**	
 * Manager with which 'can get the service for the management of the objects defined in the package it.govpay.orm 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public interface IServiceManager {

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
	public ITracciatoXMLServiceSearch getTracciatoXMLServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TracciatoXML}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TracciatoXML}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ITracciatoXMLService getTracciatoXMLService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IDominioServiceSearch getDominioServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Dominio}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Dominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IDominioService getDominioService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ITabellaContropartiServiceSearch getTabellaContropartiServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TabellaControparti}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TabellaControparti}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ITabellaContropartiService getTabellaContropartiService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IContoAccreditoServiceSearch getContoAccreditoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ContoAccredito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ContoAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IContoAccreditoService getContoAccreditoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IPspServiceSearch getPspServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Psp}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Psp}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IPspService getPspService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ICanaleServiceSearch getCanaleServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Canale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Canale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ICanaleService getCanaleService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IAnagraficaServiceSearch getAnagraficaServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Anagrafica}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Anagrafica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IAnagraficaService getAnagraficaService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IMailTemplateServiceSearch getMailTemplateServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.MailTemplate}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.MailTemplate}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IMailTemplateService getMailTemplateService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IEnteServiceSearch getEnteServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Ente}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Ente}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IEnteService getEnteService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IPortaleServiceSearch getPortaleServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Portale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Portale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IPortaleService getPortaleService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IConnettoreServiceSearch getConnettoreServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Connettore}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Connettore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IConnettoreService getConnettoreService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IIntermediarioServiceSearch getIntermediarioServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Intermediario}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Intermediario}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IIntermediarioService getIntermediarioService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IStazioneServiceSearch getStazioneServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Stazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IStazioneService getStazioneService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IApplicazioneServiceSearch getApplicazioneServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Applicazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Applicazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IApplicazioneService getApplicazioneService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IIbanAccreditoServiceSearch getIbanAccreditoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.IbanAccredito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.IbanAccredito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IIbanAccreditoService getIbanAccreditoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ITributoServiceSearch getTributoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tributo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ITributoService getTributoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IOperatoreServiceSearch getOperatoreServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operatore}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operatore}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IOperatoreService getOperatoreService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IIUVServiceSearch getIUVServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.IUV}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.IUV}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IIUVService getIUVService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IVersamentoServiceSearch getVersamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Versamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Versamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IVersamentoService getVersamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ISingoloVersamentoServiceSearch getSingoloVersamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingoloVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ISingoloVersamentoService getSingoloVersamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRPTServiceSearch getRPTServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RPT}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RPT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IRPTService getRPTService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ICarrelloServiceSearch getCarrelloServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Carrello}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Carrello}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ICarrelloService getCarrelloService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRTServiceSearch getRTServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RT}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RT}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IRTService getRTService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IEsitoServiceSearch getEsitoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Esito}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Esito}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IEsitoService getEsitoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IEventoServiceSearch getEventoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Evento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IEventoService getEventoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ISLAServiceSearch getSLAServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SLA}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SLA}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ISLAService getSLAService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRilevamentoServiceSearch getRilevamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rilevamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IRilevamentoService getRilevamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IMediaRilevamentoServiceSearch getMediaRilevamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.MediaRilevamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IMediaRilevamentoService getMediaRilevamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRRServiceSearch getRRServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.RR}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.RR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IRRService getRRService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IERServiceSearch getERServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ER}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ER}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IERService getERService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ISingolaRevocaServiceSearch getSingolaRevocaServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRevoca}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ISingolaRevocaService getSingolaRevocaService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IFRServiceSearch getFRServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.FR}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.FR}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IFRService getFRService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ISingolaRendicontazioneServiceSearch getSingolaRendicontazioneServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.SingolaRendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ISingolaRendicontazioneService getSingolaRendicontazioneService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IMailServiceSearch getMailServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Mail}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Mail}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IMailService getMailService() throws ServiceException,NotImplementedException;
	
	
	
	
}
