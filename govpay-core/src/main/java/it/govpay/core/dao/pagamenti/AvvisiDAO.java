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
package it.govpay.core.dao.pagamenti;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.AvvisiPagamentoBD;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.exception.AvvisoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.avvisi.AvvisoPagamento;

public class AvvisiDAO extends BaseDAO{

//	public PutAvvisoDTOResponse createOrUpdate(PutAvvisoDTO putAvvisoDTO) throws ServiceException,
//	AvvisoNonTrovatoException,StazioneNonTrovataException,TipoTributoNonTrovatoException{
//		PutAvvisoDTOResponse dominioDTOResponse = new PutAvvisoDTOResponse();
//	BasicBD bd = null;
//	
//	try {
//		bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			// stazione
//			try {
//				Stazione stazione = AnagraficaManager.getStazione(bd, putAvvisoDTO.getCodStazione());
//				putAvvisoDTO.getAvviso().setIdStazione(stazione.getId()); 
//			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//				throw new StazioneNonTrovataException(e.getMessage());
//			} 
//
//			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);
//			AvvisiBD dominiBD = new AvvisiBD(bd);
//			AvvisoFilter filter = dominiBD.newFilter(false);
//			filter.setCodAvviso(putAvvisoDTO.getIdAvviso());
//
//			// flag creazione o update
//			boolean isCreate = dominiBD.count(filter) == 0;
//			dominioDTOResponse.setCreated(isCreate);
//			if(isCreate) {
//				TipoTributo bolloT = null;
//				// bollo telematico
//				try {
//					bolloT = AnagraficaManager.getTipoTributo(bd, it.govpay.model.Tributo.BOLLOT);
//				} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//					throw new TipoTributoNonTrovatoException(e.getMessage());
//				}
//
//				TributiBD tributiBD = new TributiBD(bd);
//
//				Tributo tributo = new Tributo();
//				tributo.setCodTributo(it.govpay.model.Tributo.BOLLOT);
//				tributo.setAbilitato(false);
//				tributo.setDescrizione(bolloT.getDescrizione());
//
//				//TODO controllare il salvataggio
//				tributo.setIdTipoTributo(bolloT.getId());
//
//				UnitaOperativa uo = new UnitaOperativa();
//				uo.setAbilitato(true);
//				uo.setAnagrafica(putAvvisoDTO.getAvviso().getAnagrafica());
//				uo.setCodUo(it.govpay.model.Avviso.EC);
//				putAvvisoDTO.getAvviso().getAnagrafica().setCodUnivoco(uo.getCodUo());
//				bd.setAutoCommit(false);
//				dominiBD.insertAvviso(putAvvisoDTO.getAvviso());
//				uo.setIdAvviso(putAvvisoDTO.getAvviso().getId());
//				uoBd.insertUnitaOperativa(uo);
//				tributo.setIdAvviso(putAvvisoDTO.getAvviso().getId());
//				tributiBD.insertTributo(tributo);
//				bd.commit();
//
//				// ripristino l'autocommit.
//				bd.setAutoCommit(true); 
//			} else {
//				dominiBD.updateAvviso(putAvvisoDTO.getAvviso());
//			}
//		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
//			throw new AvvisoNonTrovatoException(e.getMessage());
//		} finally {
	//if(bd != null)
//			bd.closeConnection();
//		}
//		return dominioDTOResponse;
//	}


	public GetAvvisoDTOResponse getAvviso(GetAvvisoDTO getAvvisoDTO) throws NotAuthorizedException, AvvisoNonTrovatoException, ServiceException {
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			Set<String> domini = AclEngine.getAvvisiAutorizzati(getAvvisoDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA);
//			
//			if(domini != null && !domini.contains(getAvvisoDTO.getCodAvviso())) {
//				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.ANAGRAFICA_PAGOPA + " per il dominio " + getAvvisoDTO.getCodAvviso());
//			}

			AvvisiPagamentoBD avvisiPagamentoBD = new AvvisiPagamentoBD(bd);
			
			AvvisoPagamento avviso = avvisiPagamentoBD.getAvviso(getAvvisoDTO.getCodDominio(), getAvvisoDTO.getIuv());
			GetAvvisoDTOResponse response = new GetAvvisoDTOResponse(avviso);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new AvvisoNonTrovatoException("Avviso codDominio[" + getAvvisoDTO.getCodDominio() + "] iuv ["+getAvvisoDTO.getIuv()+"] non censito in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


}