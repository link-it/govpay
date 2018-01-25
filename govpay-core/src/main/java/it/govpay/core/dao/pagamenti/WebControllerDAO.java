package it.govpay.core.dao.pagamenti;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTOResponse;
import it.govpay.core.dao.pagamenti.exception.ActionNonValidaException;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RedirectException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException.FaultNodo;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Portale;
import it.govpay.servizi.commons.EsitoOperazione;

public class WebControllerDAO extends BasicBD{

	public static final String OK = "OK";
	private static final String ACTION_BACK = "back";
	private static final String ACTION_RETURN = "return";

	public WebControllerDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public RichiestaWebControllerDTOResponse gestisciRichiestaWebController(RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO) throws RedirectException, GovPayException, NotAuthorizedException, ServiceException{
		RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = new RichiestaWebControllerDTOResponse();

		GpContext ctx = GpThreadLocal.get();
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		PagamentoPortale pagamentoPortale = null;

		try {
			pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(aggiornaPagamentiPortaleDTO.getIdSessione());
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException(GovpayConfig.getInstance().getUrlErrorGovpayWC(), "Non esiste un pagamento associato all'ID sessione ["+aggiornaPagamentiPortaleDTO.getIdSessione()+"]");
		}

		switch (pagamentoPortale.getStato()) {
		case DA_REDIRIGERE_AL_WISP:
			pagamentoPortale.setStato(STATO.SELEZIONE_WISP_IN_CORSO);
			pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
			aggiornaPagamentiPortaleDTOResponse.setWispHtml(pagamentoPortale.getWispHtml());
			break;
		case PAGAMENTO_ESEGUITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case PAGAMENTO_IN_ATTESA_DI_ESITO:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		case PAGAMENTO_IN_CORSO_AL_PSP:
			aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getPspRedirectUrl());
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

						pagamentoPortale.setWispIdDominio(aggiornaPagamentiPortaleDTO.getWispDominio());
						pagamentoPortale.setWispKeyPA(aggiornaPagamentiPortaleDTO.getWispKeyPA());
						pagamentoPortale.setWispKeyWisp(aggiornaPagamentiPortaleDTO.getWispKeyWisp());

						it.govpay.core.business.Portale portaleBusiness = new it.govpay.core.business.Portale(this);
						Portale portaleAutenticato = null;
						try {
							portaleAutenticato = AnagraficaManager.getPortale(this, pagamentoPortale.getCodPortale());
						} catch (NotFoundException e1) {
							throw new GovPayException("Portale ["+pagamentoPortale.getCodPortale()+"] non esistente", EsitoOperazione.PRT_000, pagamentoPortale.getCodPortale());
						}

						ctx.log("ws.ricevutaRichiesta");
						String codPortale = portaleAutenticato.getCodPortale();

						portaleBusiness.autorizzaPortale(codPortale, portaleAutenticato, this);
						ctx.log("ws.autorizzazione");

						it.govpay.core.business.Wisp wisp = new it.govpay.core.business.Wisp(this);
						Dominio dominio =null;
						try {
							dominio = AnagraficaManager.getDominio(this, pagamentoPortale.getWispIdDominio());
						} catch (NotFoundException e) {
							throw new GovPayException("Il pagamento non puo' essere avviato poiche' il dominio utilizzato non esiste [Dominio:"+pagamentoPortale.getWispIdDominio()+"].", EsitoOperazione.DOM_000, pagamentoPortale.getWispIdDominio());
						}

						SceltaWISP scelta = null;
						Canale canale= null; 
						try {
							scelta = wisp.chiediScelta(portaleAutenticato, dominio, pagamentoPortale.getWispKeyPA(), pagamentoPortale.getWispKeyWisp(),false);
						} catch (Exception e) {
							ctx.log("pagamento.risoluzioneWispKo", e.getMessage());
							throw new ServiceException(e); 
						}

						// risoluzione del token wisp
						String tokenWisp = "";

						if(scelta.getFault() !=null) {
							tokenWisp = scelta.getFault().toString();
						} else {
							tokenWisp = OK;
							if(scelta.isSceltaEffettuata() && !scelta.isPagaDopo()) {
								canale = scelta.getCanale();
							}
							if(!scelta.isSceltaEffettuata()) {
								ctx.log("pagamento.risoluzioneWispOkNoScelta");
								throw new GovPayException(EsitoOperazione.WISP_003);
							}
							if(scelta.isPagaDopo()) {
								ctx.log("pagamento.risoluzioneWispOkPagaDopo");
								throw new GovPayException(EsitoOperazione.WISP_004);
							}
						}
						
						if(tokenWisp.equals(FaultNodo.PPT_WISP_SESSIONE_SCONOSCIUTA.toString())) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_FALLITA);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=FAIL");
						} else if(tokenWisp.equals(FaultNodo.PPT_WISP_TIMEOUT_RECUPERO_SCELTA.toString())) {
							pagamentoPortale.setStato(STATO.SELEZIONE_WISP_TIMEOUT);
							aggiornaPagamentiPortaleDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito=TIMEOUT");
						} else if(tokenWisp.equals(OK)) {
							pagamentoPortale.setCodCanale(canale.getCodCanale());
							pagamentoPortale.setTipoVersamento(canale.getTipoVersamento().toString());
							pagamentoPortale.setCodCanale(canale.getPsp(this).getCodPsp());
							// procedo al pagamento
							
							List<Versamento> versamenti = pagamentoPortale.getVersamenti(this);
							//Anagrafica versanteModel = VersamentoUtils.toAnagraficaModel(pagamentoPortale.getVersante());
														
							it.govpay.core.business.Rpt rptBD = new it.govpay.core.business.Rpt(this);
														
							//List<Rpt> rpts = rptBD.avviaTransazione(versamenti, portaleAutenticato, canale, pagamentiPortaleDTO.getIbanAddebito(), versanteModel, pagamentoPortale.getAutenticazione(), pagamentoPortale.getUrlRitorno(), false);

							Rpt rpt = new Rpt(); // rpts.get(0);

							// se ho un redirect 			
							if(rpt.getPspRedirectURL() != null) {
								pagamentoPortale.setStato(STATO.PAGAMENTO_IN_CORSO_AL_PSP);
								pagamentoPortale.setIdSessionePsp(rpt.getCodSessione());
								pagamentoPortale.setPspRedirectUrl(rpt.getPspRedirectURL()); 
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


	public RedirectDaPspDTOResponse gestisciRedirectPsp(RedirectDaPspDTO redirectDaPspDTO) throws RedirectException, GovPayException, NotAuthorizedException, ServiceException{
		RedirectDaPspDTOResponse redirectDaPspDTOResponse = new RedirectDaPspDTOResponse();


		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		PagamentoPortale pagamentoPortale = null;

		try {
			pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessionePsp(redirectDaPspDTO.getIdSession());
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException(GovpayConfig.getInstance().getUrlErrorGovpayWC(), "Non esiste un pagamento associato all'ID sessione Psp ["+redirectDaPspDTO.getIdSession()+"]");
		}

		switch (pagamentoPortale.getStato()) {
		case PAGAMENTO_IN_CORSO_AL_PSP:
			pagamentoPortale.setStato(STATO.PAGAMENTO_IN_ATTESA_DI_ESITO);
			pagamentiPortaleBD.updatePagamento(pagamentoPortale); 
			redirectDaPspDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;
		default:
			redirectDaPspDTOResponse.setLocation(pagamentoPortale.getUrlRitorno() + "?esito="+pagamentoPortale.getPspEsito());
			break;

		}

		return redirectDaPspDTOResponse;
	}

}
