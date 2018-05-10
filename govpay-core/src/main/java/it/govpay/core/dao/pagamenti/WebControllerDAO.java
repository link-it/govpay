package it.govpay.core.dao.pagamenti;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
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

			switch (pagamentoPortale.getCodiceStato()) {
			case PAGAMENTO_FALLITO:
				aggiornaPagamentiPortaleDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito","FAIL"));
				break;
			case PAGAMENTO_ESEGUITO:
				aggiornaPagamentiPortaleDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
				break;
			case PAGAMENTO_IN_ATTESA_DI_ESITO:
				aggiornaPagamentiPortaleDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
				break;
			case PAGAMENTO_IN_CORSO_AL_PSP:
				aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getPspRedirectUrl());
				break;
			case PAGAMENTO_NON_ESEGUITO:
				aggiornaPagamentiPortaleDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				aggiornaPagamentiPortaleDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
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
				redirectDaPspDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
				break;
			default:
				redirectDaPspDTOResponse.setLocation(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "esito",pagamentoPortale.getPspEsito()));
				break;

			}
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

		return redirectDaPspDTOResponse;
	}



}
