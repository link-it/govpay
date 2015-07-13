/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.core.controller;

import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.ejb.DistintaEJB;
import it.govpay.ejb.core.ejb.ScadenzarioEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.EsitoPagamentoDistinta;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModelloVersamento;
import it.govpay.ejb.core.utils.rs.EjbUtils;
import it.govpay.ejb.ndp.controller.RptController;
import it.govpay.ejb.ndp.controller.RrController;
import it.govpay.ejb.ndp.controller.RtController;
import it.govpay.ejb.ndp.controller.RptController.DatiStatoRPT;
import it.govpay.ejb.ndp.ejb.DocumentiEJB;
import it.govpay.ejb.ndp.model.impl.RTModel;
import it.govpay.ejb.ndp.util.exception.GovPayNdpException;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.web.utils.ValidazioneUtil;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Stateless
public class PagamentiController {
	
	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	ScadenzarioEJB scadenzarioEjb;
	
	@Inject
	DocumentiEJB documentiEjb;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	RptController rptCtrl;

	@Inject
	RtController rtCtrl;
	
	@Inject
	RrController rrCtrl;

	@Inject
	Logger log;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public URL richiediPagamento(RichiestaPagamento richiestaPagamento, String ccp) throws GovPayException, URISyntaxException {

		log.debug("Identificazione del Gateway di Pagamento");
		
		GatewayPagamentoModel gw = null;
		try{
			gw = anagraficaEjb.getValidGateway(richiestaPagamento.getIdentificativoPsp());
		} catch (Exception e) {
			log.error("Impossibile recuperare le informazioni del psp indicato [idPsp:" + richiestaPagamento.getIdentificativoPsp() + "]", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
		}

		if(gw == null) {
			log.error("Il psp indicato non e' censito [idPsp:" + richiestaPagamento.getIdentificativoPsp() + "]");
			throw new GovPayException(GovPayExceptionEnum.PSP_NON_TROVATO);
		}
		
		log.debug("Validazione della richiesta");
		
		try {
			ValidazioneUtil.valida(richiestaPagamento, gw);
		} catch (GovPayException e) {
			log.error("La richiesta di pagamento non ha superato la validazione: " + e.getDescrizione());
			throw e;
		}
		
		log.debug("Creazione della distinta in corso");
		Map<DistintaModel, List<PendenzaModel>> distintePendenzeMap = null;
		try {
			distintePendenzeMap = distintaEjb.creaDistinta(richiestaPagamento, ccp);
		} catch (GovPayException e) {
			log.error("Creazione della distinta fallita", e);
			throw e;
		}

		log.info("Spedizione della RPT al Nodo dei Pagamenti");
		URL pspUrl;

		EnumStatoDistinta nuovo_stato = EnumStatoDistinta.IN_CORSO;
		GovPayExceptionEnum faultCode = null;
		String de_operazione = null;

		try {
			pspUrl = rptCtrl.inviaRPT(distintePendenzeMap);	
			if (pspUrl==null) {
				nuovo_stato = EnumStatoDistinta.ESEGUITO_SBF;
			} else {
				nuovo_stato = EnumStatoDistinta.IN_CORSO; 
			}
			de_operazione = "Mod Vers="+gw.getModelloVersamento()+": RPT inviata correttamente";

		} catch (GovPayNdpException e) {
			// Ricevuto un fault da parte del nodo. Non ha accettato l'RPT
			log.error("Spedizione fallita: " + e.getFaultCode() + " - " + e.getDescrizione());
			nuovo_stato = EnumStatoDistinta.IN_ERRORE;

			String de_errore  = e.getDescrizione();
			faultCode=e.getTipoException();
			if (de_errore==null) {
				de_errore=e.getMessage();
			}	
			de_operazione="Mod Vers="+gw.getModelloVersamento()+": "+de_errore;
			throw e;
		} catch (GovPayException e) {
			// Errore di trasmissione
			// Non so se la spedizione e' stata comunque acquisita.
			// Lascio in corso e aggiornero alla prima verifica.
			log.error("Spedizione fallita: " + e, e);

			if(gw.getModelloVersamento().equals(EnumModelloVersamento.DIFFERITO))
				nuovo_stato = EnumStatoDistinta.ESEGUITO_SBF;
			else 
				nuovo_stato = EnumStatoDistinta.IN_CORSO; 

			String de_errore  = e.getDescrizione();
			faultCode=e.getTipoException();
			if (de_errore==null) {
				de_errore=e.getMessage();
			}	
			de_operazione="Mod Vers="+gw.getModelloVersamento()+": RPT inviata con errore "+de_errore;

			throw e;
		} 
		finally {
			// Aggiorno le distinte e pagamenti on line.
			for(DistintaModel distinta : distintePendenzeMap.keySet()) {
				distintaEjb.updateStatoDistinta(distinta.getIdDistinta(), nuovo_stato, faultCode, de_operazione);
			}			
		}
		return pspUrl;
	}





	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VerificaPagamento verificaPagamento(EnteCreditoreModel enteCreditore, String iuv) throws GovPayException {
		log.info("Ricevuta richiesta di verifica per il pagamento");
		DistintaModel distinta = distintaEjb.getDistinta(enteCreditore.getIdFiscale(), iuv);
		if(distinta == null) {
			log.error("Il pagamento non risulta associato a nessuna distinta.");
			throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
		}

		ThreadContext.put("ccp", distinta.getCodTransazionePsp());
		log.info("Trovata distinta in stato " + distinta.getStato() + " associata al pagamento.");
		
		// Catturo il caso di pagamento attivato e non ancora spedito
		if(!distinta.getCodTransazionePsp().equals("n/a") && distinta.getStato().equals(EnumStatoDistinta.IN_CORSO)) {
			log.info("Distinta attivata ma ancora non inviata al Nodo dei Pagamenti.");
			EsitoPagamentoDistinta esitoPagamentoDistinta = distintaEjb.getEsitoPagamentoDistinta(distinta.getIdDistinta());
			VerificaPagamento verificaPagamento = EjbUtils.toWebVerificaPagamento(esitoPagamentoDistinta);
			return verificaPagamento;
		}
		
		String paymentUrl = null;
		// Se la distinta e' in uno stato non finale, tento un aggiornamento.
		switch (distinta.getStato()) {
		case ESEGUITO_SBF:
		case IN_CORSO:
			log.info("Distinta in stato non terminale [" + distinta.getStato()+ "]. Richiesto aggiornamento.");
			try {
				ScadenzarioModel scadenzario = anagraficaEjb.getScadenzario(distinta);
				DatiStatoRPT datiStatoRPT = rptCtrl.chiediStatoRPT(enteCreditore.getIdEnteCreditore(), scadenzario, iuv, distinta.getCodTransazionePsp());
				paymentUrl = datiStatoRPT.paymentUrl;
				log.info("Stato presso il Nodo di Pagamento: " + datiStatoRPT.statoRPT);
				switch (datiStatoRPT.statoRPT) {
				case RPT_RIFIUTATA_NODO:
				case RPT_ERRORE_INVIO_A_PSP:
				case RPT_RIFIUTATA_PSP:
				case PPT_RPT_SCONOSCIUTA:
					log.info("Aggiornamento dello stato della distinta " + EnumStatoDistinta.IN_ERRORE);
					distintaEjb.updateStatoDistinta(distinta.getIdDistinta(), EnumStatoDistinta.IN_ERRORE, "Aggiornamento a fronte dello stato presso il NdP " + datiStatoRPT.statoRPT);
					break;
				case RT_ACCETTATA_PA:
				case RT_RIFIUTATA_PA:
					// Non sarei dovuto arrivarci. Mi sono perso l'RT?
					// Chiedo una copia, aggiorno lo stato della distinta e ritorno l'esito aggiornato.
					log.info("Procedo al recupero della RT e aggiornamento della distinta");
					return EjbUtils.toWebVerificaPagamento(rtCtrl.chiediCopiaRT(enteCreditore.getIdEnteCreditore(), scadenzario, iuv, distinta.getCodTransazionePsp()));
				case RPT_ACCETTATA_PSP:
				case RPT_DECORSI_TERMINI:
				case RT_ESITO_SCONOSCIUTO_PA:
				case RPT_RICEVUTA_NODO:
				case RPT_ACCETTATA_NODO:
				case RPT_INVIATA_A_PSP:
				case RT_ACCETTATA_NODO:
				case RT_RICEVUTA_NODO:
				case RT_RIFIUTATA_NODO:
					// Stato non terminale. 
					// Lascio lo stato IN_CORSO o ESEGUITO_SBF. Arrivera' la RT.
					log.info("Stato del pagamento non terminale. Niente da aggiornare.");
					break;
				}
				break;
			} catch (GovPayException e) {
				log.error("Errore durante l'aggiornamento dello stato presso il nodo: " + e.getTipoException() + " " + (e.getDescrizione() != null ? e.getDescrizione() : ""));
			}
		default:
			// Sono in uno stato terminale.
			// Procedo oltre al recuperto dell'esitoPagamentoDistinta
			break;
		}
		EsitoPagamentoDistinta esitoPagamentoDistinta = distintaEjb.getEsitoPagamentoDistinta(distinta.getIdDistinta());
		VerificaPagamento verificaPagamento = EjbUtils.toWebVerificaPagamento(esitoPagamentoDistinta);
		RTModel rtModel = documentiEjb.recuperaRT(distinta.getIdentificativoFiscaleCreditore(), distinta.getIuv(), distinta.getCodTransazionePsp());
		if(rtModel != null) verificaPagamento.setRt(rtModel.getBytes());
		verificaPagamento.setUrlPagamento(paymentUrl);
		//TODO: [SR] Chiarire
		return verificaPagamento;

	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void inviaNotifiche(String idEnteCreditore, ScadenzarioModel scadenzario) throws GovPayException {
		log.info("Gestione notifiche");
		
		List<DistintaModel> distinte = distintaEjb.getDistinteNonNotificate(idEnteCreditore);
		log.info("Individuate " + distinte.size() + " distinte da notificare.");
		
		for(DistintaModel distinta : distinte) {
			scadenzarioEjb.notificaVerificaPagamento(idEnteCreditore, scadenzario, distinta);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void inviaNotifica(String idEnteCreditore, ScadenzarioModel scadenzario, DistintaModel distinta) throws GovPayException {
		log.info("Gestione notifica distinta [IdDistinta: " + distinta.getIdDistinta() + "]");
		scadenzarioEjb.notificaVerificaPagamento(idEnteCreditore, scadenzario, distinta);
	}

	public void stornaPagamento(EnteCreditoreModel enteCreditore, String iuv, String causale) throws GovPayException {
		log.info("Ricevuta richiesta di verifica per il pagamento");
		
		DistintaModel distinta = distintaEjb.getDistinta(enteCreditore.getIdFiscale(), iuv);
		if(distinta == null) {
			log.error("Il pagamento non risulta associato a nessuna distinta.");
			throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
		}
		ThreadContext.put("ccp", distinta.getCodTransazionePsp());
		log.info("Trovata distinta in stato " + distinta.getStato() + " associata al pagamento.");
		
		// Controllo se il pagamento ha una ricevuta
		switch (distinta.getStato()) {
			case ESEGUITO_SBF:
			case IN_CORSO:
				throw new GovPayException(GovPayExceptionEnum.STORNO_NON_CONSENTITO, "Il pagamento risulta essere ancora in corso.");
			case IN_ERRORE:
			case ANNULLATO:
			case ANNULLATO_OPE:
			case NON_ESEGUITO:
				throw new GovPayException(GovPayExceptionEnum.STORNO_NON_CONSENTITO, "Il pagamento risulta terminato con importo nullo.");
			case STORNATO:
				throw new GovPayException(GovPayExceptionEnum.STORNO_NON_CONSENTITO, "Il pagamento risulta gia stornato.");
			case ESEGUITO:
			case PARZIALMENTE_ESEGUITO:
				rrCtrl.richiediStorno(enteCreditore, distinta, causale);
		}
	}
}
