package it.govpay.core.dao.pagamenti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTOResponse;
import it.govpay.core.dao.pagamenti.exception.ActionNonValidaException;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RedirectException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.WISPUtils;
import it.govpay.servizi.commons.EsitoOperazione;

public class WebControllerDAO extends BasicBD{
	
	private static final String ACTION_BACK = "back";
	private static final String ACTION_RETURN = "return";
	private static Logger log = LogManager.getLogger();
	
	public WebControllerDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public RichiestaWebControllerDTOResponse gestisciRichiestaWebController(RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO) throws RedirectException, GovPayException, NotAuthorizedException, ServiceException{
		RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = new RichiestaWebControllerDTOResponse();
		
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		PagamentoPortale pagamentoPortale = null;
		
		try {
			pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(aggiornaPagamentiPortaleDTO.getIdSessione());
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException(GovpayConfig.getInstance().getUrlErrorGovpayWC(), "Non esiste un pagamento associato all'ID sessione ["+aggiornaPagamentiPortaleDTO.getIdSessione()+"]");
		}
			
		switch (pagamentoPortale.getStato()) {
		case DA_REDIRIGERE_AL_WISP:
			// costruire html
			String template = WISPUtils.readTemplate();
			
			String urlReturn = GovpayConfig.getInstance().getUrlGovpayWC() + "/" + pagamentoPortale.getIdSessione() + "?action=" + ACTION_RETURN;
			String urlBack = GovpayConfig.getInstance().getUrlGovpayWC() + "/" + pagamentoPortale.getIdSessione() + "?action=" + ACTION_BACK;
			
			String wispHtml = WISPUtils.getWispHtml(template, pagamentoPortale, urlReturn, urlBack); 
			// aggiornare entry con html creato e stato = SELEZIONE_WISP_IN_CORSO
			
			pagamentoPortale.setStato(STATO.SELEZIONE_WISP_IN_CORSO);
			pagamentoPortale.setWispHtml(wispHtml);
			pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
			break;
		case PAGAMENTO_ESEGUITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case PAGAMENTO_IN_ATTESA_DI_ESITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case PAGAMENTO_IN_CORSO_AL_PSP:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getPspRedirect());
			break;
		case PAGAMENTO_NON_ESEGUITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case SELEZIONE_WISP_ANNULLATA:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=ANNULLO");
			break;
		case SELEZIONE_WISP_FALLITA:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
			break;
		case SELEZIONE_WISP_IN_CORSO:
			if(aggiornaPagamentiPortaleDTO.getAction() == null) {
				aggiornaPagamentiPortaleDTOResponse.setWispHtml(pagamentoPortale.getWispHtml());
			} else {
				if(aggiornaPagamentiPortaleDTO.getAction().equals(ACTION_RETURN)) {
					if(aggiornaPagamentiPortaleDTO.getWispDominio() != null 
							&& aggiornaPagamentiPortaleDTO.getWispKeyPA() != null
							&& aggiornaPagamentiPortaleDTO.getWispKeyWisp() != null)  {
						
						// risoluzione del token wisp
						String tokenWisp = "";
						
						if(tokenWisp.equals("WISP_SESSIONE_SCONOSCIUTA")) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_FALLITA);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
						} else if(tokenWisp.equals("WISP_TIMEOUT_RECUPERO_SCELTA")) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_TIMEOUT);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=TIMEOUT");
						} else if(tokenWisp.equals("OK")) {
							// leggere id psp , e tipo_versamento [TODO]
							pagamentoPortale.setIdPsp(null);
							pagamentoPortale.setTipoVersamento(null);
							pagamentoPortale.setWispIdDominio(aggiornaPagamentiPortaleDTO.getWispDominio());
							pagamentoPortale.setWispKeyPA(aggiornaPagamentiPortaleDTO.getWispKeyPA());
							pagamentoPortale.setWispKeyWisp(aggiornaPagamentiPortaleDTO.getWispKeyWisp());
							
							// invio RPT [TODO]
							Rpt rpt = new Rpt();
							
							// se ho un redirect 			
							if(rpt.getPspRedirectURL() != null) {
								pagamentoPortale.setStato(STATO.PAGAMENTO_IN_CORSO_AL_PSP);
								pagamentoPortale.setIdSessionePsp(rpt.getCodSessione());
								pagamentoPortale.setPspRedirect(rpt.getPspRedirectURL()); 
								aggiornaPagamentiPortaleDTOResponse.setLocation(rpt.getPspRedirectURL());
							} else {							
								pagamentoPortale.setStato(STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
								aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno());
							}
						} else {
							// caso non valido 
							throw new GovPayException("Token WISP ["+tokenWisp+"] non valido", EsitoOperazione.INTERNAL, tokenWisp);
						}
						pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
					} else {
						aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
					}
				} else if(aggiornaPagamentiPortaleDTO.getAction().equals(ACTION_BACK)) {
					if(aggiornaPagamentiPortaleDTO.getType() == null) {
						aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
					} else {
						// controllo del valore di type
						if(aggiornaPagamentiPortaleDTO.getType().equals("ANNULLO")) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_ANNULLATA);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=ANNULLO");
						} else if(aggiornaPagamentiPortaleDTO.getType().equals("TIMEOUT")) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_TIMEOUT);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=TIMEOUT");
						} else if(aggiornaPagamentiPortaleDTO.getType().equals("IBAN")) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_FALLITA);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
						} else {
							// caso non valido 
							throw new GovPayException("Type ["+aggiornaPagamentiPortaleDTO.getType()+"] non valido", EsitoOperazione.INTERNAL, aggiornaPagamentiPortaleDTO.getType());
						}
						pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
					}
				} else {
					// caso non valido 
					throw new ActionNonValidaException(GovpayConfig.getInstance().getUrlErrorGovpayWC(), "L'action ["+aggiornaPagamentiPortaleDTO.getAction()+"] non e' valida.");
				}
			}
			break;
		case SELEZIONE_WISP_TIMEOUT:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=TIMEOUT");
			break;
		}
		
		return aggiornaPagamentiPortaleDTOResponse;
	}
	
	
}
