package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.core.business.Wisp;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.WISPUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Portale;
import it.govpay.orm.IdVersamento;
import it.govpay.servizi.commons.EsitoOperazione;

public class PagamentiPortaleDAO extends BasicBD{
	
	private static final String ACTION_BACK = "back";
	private static final String ACTION_RETURN = "return";
	
	private static Logger log = LogManager.getLogger();
	
	public PagamentiPortaleDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public PagamentiPortaleDTOResponse inserisciPagamenti(PagamentiPortaleDTO pagamentiPortaleDTO) throws GovPayException, NotAuthorizedException, ServiceException {
		PagamentiPortaleDTOResponse response  = new PagamentiPortaleDTOResponse();
		GpContext ctx = GpThreadLocal.get();
		List<Versamento> versamenti = new ArrayList<Versamento>();
		
		// Aggiungo il codSessionePortale al PaymentContext
		ctx.getPagamentoCtx().setCodSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
		ctx.getContext().getRequest().addGenericProperty(new Property("codSessionePortale", pagamentiPortaleDTO.getIdSessionePortale() != null ? pagamentiPortaleDTO.getIdSessionePortale() : "--Non fornito--"));
		
		it.govpay.core.business.Portale portaleBusiness = new it.govpay.core.business.Portale(this);
		Portale portaleAutenticato = portaleBusiness.getPortaleAutenticato(pagamentiPortaleDTO.getPrincipal());
		ctx.log("ws.ricevutaRichiesta");
		String codPortale = portaleAutenticato.getCodPortale();
		
		portaleBusiness.autorizzaPortale(codPortale, portaleAutenticato, this);
		ctx.log("ws.autorizzazione");
		
		String codDominio = null;
		String enteCreditore = null;
		String nome = null;
		List<IdVersamento> idVersamento = new ArrayList<IdVersamento>();
		it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(this);
		StringBuilder sbNomeVersamenti = new StringBuilder();
		// 1. Lista Id_versamento
		for(int i = 0; i < pagamentiPortaleDTO.getPendenzeOrPendenzeRef().size(); i++) {
			Object v = pagamentiPortaleDTO.getPendenzeOrPendenzeRef().get(i);
			Versamento versamentoModel = null;
			String codDominioVKey = null, codApplicazione = null, codVersamentoEnte = null, iuv = null, bundlekey = null, codUnivocoDebitore = null;
		
			if(v instanceof it.govpay.servizi.commons.Versamento) {
				it.govpay.servizi.commons.Versamento versamento = (it.govpay.servizi.commons.Versamento) v;
				ctx.log("rpt.acquisizioneVersamento", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
//				versamentoModel = VersamentoUtils.toVersamentoModel(versamento, this);
				codDominioVKey = versamento.getCodDominio();
				codApplicazione = versamento.getCodApplicazione();
				codVersamentoEnte = versamento.getCodVersamentoEnte();
				iuv= versamento.getIuv();
				bundlekey = versamento.getBundlekey();
				if(versamento.getDebitore() != null)
					codUnivocoDebitore = versamento.getDebitore().getCodUnivoco();
				
			} else {
				it.govpay.servizi.commons.VersamentoKey versamento = (it.govpay.servizi.commons.VersamentoKey) v;
				
				Iterator<JAXBElement<String>> iterator = versamento.getContent().iterator();
				while(iterator.hasNext()){
					JAXBElement<String> element = iterator.next();

					if(element.getName().equals(VersamentoUtils._VersamentoKeyBundlekey_QNAME)) {
						bundlekey = element.getValue();
					}
					if(element.getName().equals(VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME)) {
						codUnivocoDebitore = element.getValue();
					}
					if(element.getName().equals(VersamentoUtils._VersamentoKeyCodApplicazione_QNAME)) {
						codApplicazione = element.getValue();
					}
					if(element.getName().equals(VersamentoUtils._VersamentoKeyCodDominio_QNAME)) {
						codDominioVKey = element.getValue();
					}
					if(element.getName().equals(VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME)) {
						codVersamentoEnte = element.getValue();
					}
					if(element.getName().equals(VersamentoUtils._VersamentoKeyIuv_QNAME)) {
						iuv = element.getValue();
					}
				}
			}
			
			versamentoModel = versamentoBusiness.chiediVersamento(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominioVKey, iuv);
		
			if(!versamentoModel.getUo(this).isAbilitato()) {
				throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad una unita' operativa disabilitata [Uo:"+versamentoModel.getUo(this).getCodUo()+"].", EsitoOperazione.UOP_001, versamentoModel.getUo(this).getCodUo());
			}
			
			if(!versamentoModel.getUo(this).getDominio(this).isAbilitato()) {
				throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio disabilitato [Dominio:"+versamentoModel.getUo(this).getDominio(this).getCodDominio()+"].", EsitoOperazione.DOM_001, versamentoModel.getUo(this).getDominio(this).getCodDominio());
			}
			
			IdVersamento idV = new IdVersamento();
			idV.setCodVersamentoEnte(versamentoModel.getCodVersamentoEnte());
			idV.setId(versamentoModel.getId());
			idVersamento.add(idV);
			
			if(i == 0) {
				// 	2. Codice dominio della prima pendenza
				codDominio = versamentoModel.getUo(this).getDominio(this).getCodDominio();
				// 3. ente creditore
				enteCreditore = versamentoModel.getUo(this).getDominio(this).getRagioneSociale();
			}
			
			if(sbNomeVersamenti.length() >0)
				sbNomeVersamenti.append("#");
			
			sbNomeVersamenti.append(versamentoModel.getCodVersamentoEnte());
			
			versamenti.add(versamentoModel);
		}
		
		//3. numero dei pagamenti = somma numero singoli versamenti
		int numeroPagamenti = 0;
		// 4. controllo se c'e' almeno un bollo
		boolean hasBollo = false;
		// 5. somma degli importi delle pendenze
		double sommaImporti = 0;
		// 6. IbanAccredito se num_pagamenti = 1 e l'iban indicato nel sv non e' postale
		IbanAccredito ibanAccredito = null;
		// 7. conto postale se tutti gli iban sono postali
		boolean contoPostale = true;
		
		boolean pagamentiModello2 = pagamentiPortaleDTO.getIbanAddebito() != null;
		
		for (Versamento vTmp : versamenti) {
			List<SingoloVersamento> singoliVersamenti = vTmp.getSingoliVersamenti(this);
			numeroPagamenti += singoliVersamenti.size();
			
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				if(singoloVersamento.getTipoBollo()!= null) {
					hasBollo = true;
				}
				
				IbanAccredito ibanAccreditoTmp = singoloVersamento.getIbanAccredito(this);
				
				if(ibanAccreditoTmp != null)
					contoPostale = contoPostale && ibanAccreditoTmp.isPostale();
			}
			
			sommaImporti += vTmp.getImportoTotale().doubleValue();
		}
		
		if(numeroPagamenti == 1) {
			Versamento vTmp = versamenti.get(0);
			List<SingoloVersamento> singoliVersamenti = vTmp.getSingoliVersamenti(this);
			SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
			IbanAccredito ibanAccreditoTmp = singoloVersamento.getIbanAccredito(this);
			if(!ibanAccreditoTmp.isPostale())
				ibanAccredito = ibanAccreditoTmp;
		}
		
		nome = sbNomeVersamenti.length() > 255 ? (sbNomeVersamenti.substring(0, 252) + "...") : sbNomeVersamenti.toString();
		
		STATO stato = null;
		String redirectUrl = null;
		String idSessionePsp = null;
		String pspRedirect = null;
		String idPsp = null;
		String tipoVersamento = null;
		String codCanale = null;		
		PagamentoPortale pagamentoPortale = null;
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		
		// gestione dello stato, url_redirect e id_session_psp
		if(pagamentiPortaleDTO.getKeyPA() != null && pagamentiPortaleDTO.getKeyWISP() != null && pagamentiPortaleDTO.getIdDominio() != null) {
			// procedo al pagamento
			Anagrafica versanteModel = VersamentoUtils.toAnagraficaModel(pagamentiPortaleDTO.getVersante());
			
			Wisp wisp = new Wisp(this);
			Dominio dominio =null;
			try {
				dominio = AnagraficaManager.getDominio(this, pagamentiPortaleDTO.getIdDominio());
			} catch (NotFoundException e) {
				throw new GovPayException("Il pagamento non puo' essere avviato poiche' il dominio utilizzato non esiste [Dominio:"+pagamentiPortaleDTO.getIdDominio()+"].", EsitoOperazione.DOM_000, pagamentiPortaleDTO.getIdDominio());
			}
			
			SceltaWISP scelta = null;
			Canale canale= null; 
			try {
				scelta = wisp.chiediScelta(portaleAutenticato, dominio, pagamentiPortaleDTO.getKeyPA(), pagamentiPortaleDTO.getKeyWISP());
			} catch (Exception e) {
				ctx.log("pagamento.risoluzioneWispKo", e.getMessage());
				throw new ServiceException(e); 
			}
			
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
			
			// decodifica di tipo versamento e id psp dal canale
			codCanale = canale.getCodCanale();
			tipoVersamento = canale.getTipoVersamento().getCodifica();
			idPsp = canale.getPsp(this).getCodPsp();
			it.govpay.core.business.Rpt rptBD = new it.govpay.core.business.Rpt(this);
			List<Rpt> rpts = rptBD.avviaTransazione(versamenti, portaleAutenticato, canale, pagamentiPortaleDTO.getIbanAddebito(), versanteModel, pagamentiPortaleDTO.getAutenticazioneSoggetto(), pagamentiPortaleDTO.getUrlRitorno(), false);
			
			Rpt rpt = rpts.get(0);
			
			// se ho un redirect 			
			if(rpt.getPspRedirectURL() != null) {
				stato = STATO.PAGAMENTO_IN_CORSO_AL_PSP;
				idSessionePsp = rpt.getCodSessione();
				redirectUrl = rpt.getPspRedirectURL();
			} else {
				stato = STATO.PAGAMENTO_IN_ATTESA_DI_ESITO;
				redirectUrl = pagamentiPortaleDTO.getUrlRitorno();
			}
			
		} else {
			// sessione di pagamento non in corso
			stato = STATO.DA_REDIRIGERE_AL_WISP;
			redirectUrl = GovpayConfig.getInstance().getUrlGovpayWC() + "/" + pagamentiPortaleDTO.getIdSessione();
		}
		
		pagamentoPortale = new PagamentoPortale();
		pagamentoPortale.setCodPortale(codPortale);
		pagamentoPortale.setDataRichiesta(new Date());
		pagamentoPortale.setIdSessione(pagamentiPortaleDTO.getIdSessione());
		pagamentoPortale.setIdSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
		pagamentoPortale.setIdSessionePsp(idSessionePsp);
		pagamentoPortale.setJsonRequest(pagamentiPortaleDTO.getJsonRichiesta());
		pagamentoPortale.setIdVersamento(idVersamento); 
		pagamentoPortale.setPspRedirectUrl(pspRedirect);
		pagamentoPortale.setStato(stato);
		pagamentoPortale.setWispIdDominio(codDominio);
		pagamentoPortale.setCodPsp(idPsp);
		pagamentoPortale.setTipoVersamento(tipoVersamento);
		pagamentoPortale.setCodCanale(codCanale); 
		pagamentoPortale.setUrlRitorno(pagamentiPortaleDTO.getUrlRitorno());
		pagamentoPortale.setNome(nome);
		
		// costruire html
		String template = WISPUtils.readTemplate();
		
		String urlReturn = UrlUtils.addParameter(GovpayConfig.getInstance().getUrlGovpayWC() + "/" + pagamentoPortale.getIdSessione() , "action" , ACTION_RETURN);
		String urlBack = UrlUtils.addParameter(GovpayConfig.getInstance().getUrlGovpayWC() + "/" + pagamentoPortale.getIdSessione() ,"action" , ACTION_BACK);
		
		String wispHtml = WISPUtils.getWispHtml(GovpayConfig.getInstance().getUrlWISP(), template, pagamentoPortale, urlReturn, urlBack, enteCreditore, numeroPagamenti, ibanAccredito, contoPostale, hasBollo, sommaImporti,pagamentiModello2,
				pagamentiPortaleDTO.getLingua()); 
		
		pagamentoPortale.setWispHtml(wispHtml);
		pagamentiPortaleBD.insertPagamento(pagamentoPortale);

		response.setRedirectUrl(redirectUrl);
		response.setId(pagamentoPortale.getId());
		
		return response;
	}
	
	public LeggiPagamentoPortaleDTOResponse leggiPagamentoPortale(LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException{
		LeggiPagamentoPortaleDTOResponse leggiPagamentoPortaleDTOResponse = new LeggiPagamentoPortaleDTOResponse();
		
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		PagamentoPortale pagamentoPortale = null;
		
		try {
			pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(leggiPagamentoPortaleDTO.getIdSessione());
			leggiPagamentoPortaleDTOResponse.setPagamento(pagamentoPortale); 
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException(null, "Non esiste un pagamento associato all'ID ["+leggiPagamentoPortaleDTO.getIdSessione()+"]");
		}
		
		return leggiPagamentoPortaleDTOResponse;
	}
	
	public ListaPagamentiPortaleDTOResponse listaPagamentiPortale(ListaPagamentiPortaleDTO listaPagamentiPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException{
		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
		PagamentoPortaleFilter filter = pagamentiPortaleBD.newFilter();

		filter.setOffset(listaPagamentiPortaleDTO.getOffset());
		filter.setLimit(listaPagamentiPortaleDTO.getLimit());
		filter.setDataInizio(listaPagamentiPortaleDTO.getDataDa());
		filter.setDataFine(listaPagamentiPortaleDTO.getDataA());
		filter.setStato(listaPagamentiPortaleDTO.getStato());
		filter.setVersante(listaPagamentiPortaleDTO.getVersante());
		
		long count = pagamentiPortaleBD.count(filter);

		if(count > 0) {
			return new ListaPagamentiPortaleDTOResponse(count, pagamentiPortaleBD.findAll(filter));
		} else {
			return new ListaPagamentiPortaleDTOResponse(count, new ArrayList<PagamentoPortale>());
		}
	}
}
