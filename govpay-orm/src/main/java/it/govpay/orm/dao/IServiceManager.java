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
package it.govpay.orm.dao;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;


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
	public IUoServiceSearch getUoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Uo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Uo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IUoService getUoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ITipoTributoServiceSearch getTipoTributoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoTributo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.TipoTributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ITipoTributoService getTipoTributoService() throws ServiceException,NotImplementedException;
	
	
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
	public IAuditServiceSearch getAuditServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Audit}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Audit}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IAuditService getAuditService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IACLServiceSearch getACLServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.ACL}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.ACL}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IACLService getACLService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IUtenzaDominioServiceSearch getUtenzaDominioServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaDominio}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IUtenzaDominioService getUtenzaDominioService() throws ServiceException,NotImplementedException;
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:UtenzaTributo type:UtenzaTributo
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaTributo}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.UtenzaTributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IUtenzaTributoServiceSearch getUtenzaTributoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaTributo}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.UtenzaTributo}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IUtenzaTributoService getUtenzaTributoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IUtenzaServiceSearch getUtenzaServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Utenza}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Utenza}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IUtenzaService getUtenzaService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRendicontazioneServiceSearch getRendicontazioneServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rendicontazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Rendicontazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IRendicontazioneService getRendicontazioneService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IPagamentoPortaleVersamentoServiceSearch getPagamentoPortaleVersamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortaleVersamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IPagamentoPortaleVersamentoService getPagamentoPortaleVersamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IPagamentoPortaleServiceSearch getPagamentoPortaleServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.PagamentoPortale}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IPagamentoPortaleService getPagamentoPortaleService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IPagamentoServiceSearch getPagamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Pagamento}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Pagamento}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IPagamentoService getPagamentoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public INotificaServiceSearch getNotificaServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Notifica}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Notifica}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public INotificaService getNotificaService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IIncassoServiceSearch getIncassoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Incasso}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Incasso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IIncassoService getIncassoService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IRendicontazionePagamentoServiceSearch getRendicontazionePagamentoServiceSearch() throws ServiceException,NotImplementedException;
	
	
	
	
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
	public IBatchServiceSearch getBatchServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Batch}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Batch}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IBatchService getBatchService() throws ServiceException,NotImplementedException;
	
	
	
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
	public IOperazioneServiceSearch getOperazioneServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operazione}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Operazione}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IOperazioneService getOperazioneService() throws ServiceException,NotImplementedException;
	
	
	
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
	public ITracciatoServiceSearch getTracciatoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tracciato}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Tracciato}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public ITracciatoService getTracciatoService() throws ServiceException,NotImplementedException;
	
	
	
	/*
	 =====================================================================================================================
	 Services relating to the object with name:Avviso type:Avviso
	 =====================================================================================================================
	*/
	
	/**
	 * Return a service used to research on the backend on objects of type {@link it.govpay.orm.Avviso}
	 *
	 * @return Service used to research on the backend on objects of type {@link it.govpay.orm.Avviso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IAvvisoServiceSearch getAvvisoServiceSearch() throws ServiceException,NotImplementedException;
	
	/**
	 * Return a service used to research and manage on the backend on objects of type {@link it.govpay.orm.Avviso}
	 *
	 * @return Service used to research and manage on the backend on objects of type {@link it.govpay.orm.Avviso}	
	 * @throws ServiceException Exception thrown when an error occurs during processing of the request
	 * @throws NotImplementedException Exception thrown when the method is not implemented
	 */
	public IAvvisoService getAvvisoService() throws ServiceException,NotImplementedException;
	
	
	
	
}
