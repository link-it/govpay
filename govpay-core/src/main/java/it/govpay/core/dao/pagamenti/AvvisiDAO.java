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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.exception.AvvisoNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.model.avvisi.AvvisoPagamentoInput;

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


	public GetAvvisoDTOResponse getAvviso(GetAvvisoDTO getAvvisoDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException {
		BasicBD bd = null;
		Versamento versamento = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che l'utenza sia autorizzata per il dominio scelto
			this.autorizzaRichiesta(getAvvisoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, getAvvisoDTO.getCodDominio(), null, bd);
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamento = versamentiBD.getVersamento(getAvvisoDTO.getCodDominio(), getAvvisoDTO.getIuv());
			
			Dominio dominio = versamento.getDominio(versamentiBD);
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(getAvvisoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, dominio.getCodDominio(), null, bd);
			
			
			GetAvvisoDTOResponse response = new GetAvvisoDTOResponse();
			switch(getAvvisoDTO.getFormato()) {
			case PDF:
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
				avvisoPagamento.setCodDominio(versamento.getDominio(bd).getCodDominio());
				avvisoPagamento.setIuv(versamento.getIuvVersamento());
				PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
				printAvvisoDTO.setAvviso(avvisoPagamento);
				AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, versamento);
				printAvvisoDTO.setInput(input); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
				response.setAvvisoPdf(printAvvisoDTOResponse.getAvviso().getPdf());
				String pdfFileName = versamento.getDominio(bd).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";
				response.setFilenameAvviso(pdfFileName);
				break;
			case JSON:
			default:
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(bd), dominio);
				
				response.setVersamento(versamento);
				response.setDominio(dominio);
				response.setBarCode(new String(iuvGenerato.getBarCode()));
				response.setQrCode(new String(iuvGenerato.getQrCode())); 
				break;
			
			}
			
//			AvvisiPagamentoBD avvisiPagamentoBD = new AvvisiPagamentoBD(bd);
//			AvvisoPagamento avviso = avvisiPagamentoBD.getAvviso(getAvvisoDTO.getCodDominio(), getAvvisoDTO.getIuv());
			
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


}