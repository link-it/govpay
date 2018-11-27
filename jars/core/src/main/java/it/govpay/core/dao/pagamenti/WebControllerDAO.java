/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTOResponse;
import it.govpay.core.dao.pagamenti.exception.ActionNonValidaException;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.ParametriNonTrovatiException;
import it.govpay.core.dao.pagamenti.exception.TokenWISPNonValidoException;
import it.govpay.core.dao.pagamenti.exception.TransazioneRptException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.UrlUtils;

public class WebControllerDAO extends BaseDAO{

	public static final String OK = "OK";

	public WebControllerDAO() {

	}

	public RichiestaWebControllerDTOResponse gestisciRichiestaWebController(RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO) throws GovPayException, NotAuthorizedException, ServiceException, PagamentoPortaleNonTrovatoException, ActionNonValidaException,
	TokenWISPNonValidoException, TransazioneRptException{
		RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = new RichiestaWebControllerDTOResponse();

		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			PagamentoPortale pagamentoPortale = null;

			try {
				pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(aggiornaPagamentiPortaleDTO.getIdSessione());
			}catch(NotFoundException e) {
				throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID sessione ["+aggiornaPagamentiPortaleDTO.getIdSessione()+"]");
			}

			String urlRitorno = pagamentoPortale.getUrlRitorno();
			
			switch (pagamentoPortale.getCodiceStato()) {
			case PAGAMENTO_FALLITO:
				urlRitorno = UrlUtils.addParameter(urlRitorno, "esito","FAIL");
				aggiornaPagamentiPortaleDTOResponse.setLocation(urlRitorno);
				break;
			case PAGAMENTO_ESEGUITO:
			case PAGAMENTO_IN_ATTESA_DI_ESITO:
			case PAGAMENTO_NON_ESEGUITO:
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				urlRitorno = UrlUtils.addParameter(urlRitorno, "esito",pagamentoPortale.getPspEsito());
				aggiornaPagamentiPortaleDTOResponse.setLocation(urlRitorno);
				break;
			case PAGAMENTO_IN_CORSO_AL_PSP:
				aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getPspRedirectUrl());
				break;
			}
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

		return aggiornaPagamentiPortaleDTOResponse;
	}


	public RedirectDaPspDTOResponse gestisciRedirectPsp(RedirectDaPspDTO redirectDaPspDTO) throws GovPayException, NotAuthorizedException, ServiceException, PagamentoPortaleNonTrovatoException, ParametriNonTrovatiException{
		RedirectDaPspDTOResponse redirectDaPspDTOResponse = new RedirectDaPspDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			PagamentoPortale pagamentoPortale = null;

			try {
				pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessionePsp(redirectDaPspDTO.getIdSession());
			}catch(NotFoundException e) {
				throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID sessione Psp ["+redirectDaPspDTO.getIdSession()+"]");
			}

			if(redirectDaPspDTO.getEsito() == null || redirectDaPspDTO.getIdSession() == null)
				throw new ParametriNonTrovatiException(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() ,"esito","FAIL"), "Parametri 'idSession' ed 'esito' obbligatori");

			switch (pagamentoPortale.getCodiceStato()) {
			case PAGAMENTO_IN_CORSO_AL_PSP:
				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
				pagamentoPortale.setPspEsito(redirectDaPspDTO.getEsito()); 
				pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
				break;
			default:
				break;
			}
			String urlRitorno = pagamentoPortale.getUrlRitorno();
			
			if(urlRitorno != null) {
				urlRitorno = UrlUtils.addParameter(urlRitorno, "idPagamento", pagamentoPortale.getIdSessione());
				urlRitorno = UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito());
				redirectDaPspDTOResponse.setLocation(urlRitorno);
			} else {
				throw new GovPayException("Impossibile indirizzare il Portale di Pagamento: non e' stata fornita una URL di ritorno in fase di richiesta. IdCarrello " + pagamentoPortale.getIdSessione(), EsitoOperazione.PAG_013, pagamentoPortale.getIdSessione());
			}
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

		return redirectDaPspDTOResponse;
	}



}
