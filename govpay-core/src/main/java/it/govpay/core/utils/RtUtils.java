/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Notifica;
import it.govpay.model.Pagamento;
import it.govpay.model.Rpt;
import it.govpay.model.SingoloVersamento;
import it.govpay.model.Versamento;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;

public class RtUtils extends NdpValidationUtils {
	
	private static Logger log = LogManager.getLogger();

	public enum StatoRicevuta {
		PagamentoEseguito,
		PagamentoNonEseguito,
		PagamentoParzialmenteEseguito,
		DecorrenzaTermini,
		DecorrenzaTerminiParziale;
	}

	public static byte[] validaFirma(String tipoFirma,  byte[] rt, String idDominio) throws NdpException {
		try {
			switch (FirmaRichiesta.toEnum(tipoFirma)) {
			case NESSUNA:
				return rt;
			case CA_DES:
				return validaFirmaCades(rt, idDominio);
			case XA_DES:
				return validaFirmaXades(rt, idDominio);
			default:
				throw new NdpException(FaultPa.PAA_FIRMA_ERRATA, idDominio, "La firma non e' quella richiesta nella RPT (Tipo Firma " + tipoFirma + ")");
			}
		} catch (ServiceException e) {
			throw new NdpException(FaultPa.PAA_FIRMA_ERRATA, idDominio, "La firma non e' quella richiesta nella RPT (Tipo Firma " + tipoFirma + ")");
		}
	}

	private static byte[] validaFirmaXades(byte[] rt, String idDominio) throws NdpException {
		try {
			return SignUtils.cleanXadesSignedFile(rt);
		} catch (Exception e) {
			throw new NdpException(FaultPa.PAA_FIRMA_ERRATA, idDominio);
		}
	}

	private static byte[] validaFirmaCades(byte[] rt, String idDominio) throws NdpException {		
		try {
			return SignUtils.cleanCadesSignedFile(rt);
		} catch (Exception e) {
			throw new NdpException(FaultPa.PAA_FIRMA_ERRATA, idDominio);
		}
	}

	public static void validaSemantica(CtRichiestaPagamentoTelematico rpt, CtRicevutaTelematica rt) throws NdpException {

		if(!equals(rpt.getIdentificativoMessaggioRichiesta(), rt.getRiferimentoMessaggioRichiesta())) {
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), "RiferimentoMessaggioRichiesta non corrisponde all'RPT");
		}
		String errore = null;
		if( (errore = validaSemantica(rpt.getDominio(), rt.getDominio())) != null){
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), errore);
		}
		if( (errore = validaSemantica(rpt.getEnteBeneficiario(), rt.getEnteBeneficiario()) )!= null){
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoPagatore(), rt.getSoggettoPagatore())) != null){
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), errore);
		}
		if( (errore = validaSemantica(rpt.getSoggettoVersante(), rt.getSoggettoVersante())) != null){
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), errore);
		}

		CtDatiVersamentoRT datiVersamentoRT = rt.getDatiPagamento();

		if ((errore = validaSemantica(rpt.getDatiVersamento(), datiVersamentoRT)) != null) {
			throw new NdpException(FaultPa.PAA_SEMANTICA, rpt.getDominio().getIdentificativoDominio(), errore);
		}
	}

	private static String validaSemantica(CtDatiVersamentoRPT rpt, CtDatiVersamentoRT rt) throws NdpException {

		if (!equals(rpt.getCodiceContestoPagamento(), rt.getCodiceContestoPagamento())) 
			return "CodiceContestoPagamento non corrisponde all'RPT";


		if (!equals(rpt.getIdentificativoUnivocoVersamento(), rt.getIdentificativoUnivocoVersamento())) 
			return "IdentificativoUnivocoVersamento non corrisponde all'RPT";

		Rpt.EsitoPagamento esitoPagamento = validaSemanticaCodiceEsitoPagamento(rt.getCodiceEsitoPagamento());

		// Se siamo in pagamento eseguito, parzialmente eseguito o parzialmente decorso, devono esserci tanti versamenti quanti pagamenti.
		switch (esitoPagamento) {
		case DECORRENZA_TERMINI_PARZIALE:
		case PAGAMENTO_ESEGUITO:
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			if(rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name();
			break;
		case DECORRENZA_TERMINI:
		case PAGAMENTO_NON_ESEGUITO:
			if(rt.getDatiSingoloPagamento().size() != 0 && rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size())
				return "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name();
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < rpt.getDatiSingoloVersamento().size(); i++) {

			CtDatiSingoloVersamentoRPT singoloVersamento = rpt.getDatiSingoloVersamento().get(i);
			CtDatiSingoloPagamentoRT singoloPagamento = null;
			if(rt.getDatiSingoloPagamento().size() != 0) {
				singoloPagamento = rt.getDatiSingoloPagamento().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getSingoloImportoPagato());
			}
		}

		if (importoTotaleCalcolato.compareTo(rt.getImportoTotalePagato()) != 0)
			return "ImportoTotalePagato non corrisponde alla somma dei SingoliImportiPagati";
		if (esitoPagamento == Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Non Eseguito'.";
		if (esitoPagamento == Rpt.EsitoPagamento.DECORRENZA_TERMINI && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			return "ImportoTotalePagato diverso da 0 per un pagamento con esito 'Decorrenza temini'.";
		if (esitoPagamento == Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO && rt.getImportoTotalePagato().compareTo(rpt.getImportoTotaleDaVersare()) != 0)
			return "ImportoTotalePagato diverso dall'ImportoTotaleDaVersare per un pagamento con esito 'Eseguito'.";
		return null;
	}

	private static String validaSemanticaSingoloVersamento(CtDatiSingoloVersamentoRPT singoloVersamento, CtDatiSingoloPagamentoRT singoloPagamento) {
		if(!equals(singoloPagamento.getCausaleVersamento(), singoloVersamento.getCausaleVersamento())) return "CausaleVersamento del pagamento non corrisponde a quella nell'RPT.";
		if(!equals(singoloPagamento.getDatiSpecificiRiscossione(), singoloVersamento.getDatiSpecificiRiscossione())) return "DatiSpecificiRiscossione del pagamento non corrisponde a quella nell'RPT.";

		if(singoloPagamento.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0) {
			if(singoloPagamento.getEsitoSingoloPagamento() == null || singoloPagamento.getEsitoSingoloPagamento().isEmpty()) return "EsitoSingoloPagamento obbligatorio per pagamenti non eseguiti.";
			if(!singoloPagamento.getIdentificativoUnivocoRiscossione().equals("n/a")) return "IdentificativoUnivocoRiscossione deve essere n/a per pagamenti non eseguiti.";
		} else if(singoloPagamento.getSingoloImportoPagato().compareTo(singoloVersamento.getImportoSingoloVersamento()) != 0) {
			return "SingoloImportoPagato di un pagamento eseguito non corrisponde all'RPT";
		}

		return null;
	}

	private static Rpt.EsitoPagamento validaSemanticaCodiceEsitoPagamento(String codiceEsitoPagamento) throws NdpException {
		try{
			switch (Integer.parseInt(codiceEsitoPagamento)) {
			case 0:
				return Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO;
			case 1:
				return Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
			case 2:
				return Rpt.EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
			case 3:
				return Rpt.EsitoPagamento.DECORRENZA_TERMINI;
			case 4:
				return Rpt.EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
			default:
				break;
			} 
		} catch (Throwable e) {
		}
		throw new NdpException(FaultPa.PAA_SEMANTICA, "CodiceEsitoPagamento sconosciuto");
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, String tipoFirma, byte[] rtByte, BasicBD bd) throws ServiceException, NdpException {
		bd.setAutoCommit(false);
		bd.enableSelectForUpdate();
		
		RptBD rptBD = new RptBD(bd);
		Rpt rpt = null;
		try {
			rpt = rptBD.getRpt(codDominio, iuv, ccp);
		} catch (NotFoundException e) {
			throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio, null);
		}
		
		if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
			throw new NdpException(FaultPa.PAA_RT_DUPLICATA, rpt.getCodDominio());
		}
		
		if(!rpt.getFirmaRichiesta().equals(FirmaRichiesta.toEnum(tipoFirma)))
			throw new NdpException(FaultPa.PAA_FIRMA_ERRATA, codDominio, "Richiesta RT con firma [" + rpt.getFirmaRichiesta().getCodifica() + "], ricevuta RT con firma [" + tipoFirma + "]");
		
		
		CtRicevutaTelematica ctRt = null;
		CtRichiestaPagamentoTelematico ctRpt = null;
		// Validazione RT
		try {
			// Validazione Firma
			byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rtByte, codDominio);
			
			// Validazione Sintattica
			try {
				ctRt = JaxbUtils.toRT(rtByteValidato);
			} catch (Exception e) {
				log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
				throw new NdpException(FaultPa.PAA_SINTASSI_XSD, codDominio, e.getCause().getMessage());
			}
			
			// Validazione Semantica
			try {
				ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt());
				RtUtils.validaSemantica(ctRpt, ctRt);
			} catch (JAXBException e) {
				throw new ServiceException(e);
			} catch (SAXException e) {
				throw new ServiceException(e);
			}
		} catch (NdpException e) {
			log.error("Rt rifiutata: " + e.getDescrizione());
			rpt.setStato(StatoRpt.RT_RIFIUTATA_PA);
			rpt.setDescrizioneStato(e.getDescrizione());
			rpt.setXmlRt(rtByte);
			rptBD.updateRpt(rpt.getId(), rpt);
			bd.commit();
			bd.disableSelectForUpdate();
			throw e;
		}
		
		log.info("Acquisizione RT per un importo di " + ctRt.getDatiPagamento().getImportoTotalePagato());
		
		GpContext ctx = GpThreadLocal.get();
		ctx.getContext().getRequest().addGenericProperty(new Property("codMessaggioRicevuta", ctRt.getIdentificativoMessaggioRicevuta()));
		ctx.getContext().getRequest().addGenericProperty(new Property("importo", ctRt.getDatiPagamento().getImportoTotalePagato().toString()));
		ctx.getContext().getRequest().addGenericProperty(new Property("codEsitoPagamento", Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()).toString()));
		ctx.log("rt.acquisizione");
		
		// Rileggo per avere la lettura dello stato rpt in transazione
		rpt.setCodMsgRicevuta(ctRt.getIdentificativoMessaggioRicevuta());
		rpt.setDataMsgRicevuta(ctRt.getDataOraMessaggioRicevuta());
		rpt.setEsitoPagamento(Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()));
		rpt.setImportoTotalePagato(ctRt.getDatiPagamento().getImportoTotalePagato());
		rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
		rpt.setXmlRt(rtByte);
		rpt.setIdTransazioneRt(GpThreadLocal.get().getTransactionId());
		// Aggiorno l'RPT con i dati dell'RT
		rptBD.updateRpt(rpt.getId(), rpt);
		
		Versamento versamento = rpt.getVersamento(bd);
		VersamentiBD versamentiBD = new VersamentiBD(bd);

		List<CtDatiSingoloPagamentoRT> datiSingoliPagamenti = ctRt.getDatiPagamento().getDatiSingoloPagamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
		
		PagamentiBD pagamentiBD = new PagamentiBD(bd);
		
		boolean irregolare = false;
		for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoliPagamenti.get(indice);
			CtDatiSingoloVersamentoRPT ctDatiSingoloVersamentoRPT = ctRpt.getDatiVersamento().getDatiSingoloVersamento().get(indice);
			// Se non e' stato completato un pagamento, non faccio niente.
			if(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0)
				continue;
			
			SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);
			
			Pagamento pagamento = new Pagamento();
			pagamento.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
			pagamento.setRpt(rpt);
			pagamento.setSingoloVersamento(singoloVersamento);
			pagamento.setImportoPagato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato());
			pagamento.setIur(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			pagamento.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
			pagamento.setIbanAccredito(ctDatiSingoloVersamentoRPT.getIbanAccredito());

			if(ctDatiSingoloPagamentoRT.getAllegatoRicevuta() != null) {
				pagamento.setTipoAllegato(Pagamento.TipoAllegato.valueOf(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTipoAllegatoRicevuta().toString()));
				pagamento.setAllegato(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTestoAllegato());
			}
			
			// Se gli importi corrispondono e lo stato era da pagare, il singoloVersamento e' eseguito. Altrimenti irregolare.
			if(singoloVersamento.getStatoSingoloVersamento().equals(StatoSingoloVersamento.NON_ESEGUITO) && singoloVersamento.getImportoSingoloVersamento().compareTo(pagamento.getImportoPagato()) == 0)
				singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.ESEGUITO);
			else {
				singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.ANOMALO);
				irregolare = true;
			}
			
			ctx.log("rt.acquisizionePagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte(), singoloVersamento.getStatoSingoloVersamento().toString());
			
			versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), singoloVersamento.getStatoSingoloVersamento());
			pagamentiBD.insertPagamento(pagamento);
		}
		
		
		
		switch (rpt.getEsitoPagamento()) {
		case PAGAMENTO_ESEGUITO:
			switch (versamento.getStatoVersamento()) {
			case ANNULLATO:
			case NON_ESEGUITO:
				if(!irregolare) {
					versamento.setStatoVersamento(StatoVersamento.ESEGUITO);
					versamentiBD.updateStatoVersamento(versamento.getId(), versamento.getStatoVersamento(), null);
					break;
				}
			default:
				versamento.setStatoVersamento(StatoVersamento.ANOMALO);
				versamentiBD.updateStatoVersamento(versamento.getId(), versamento.getStatoVersamento(), null);
				break;
			}
			break;
			
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
		case DECORRENZA_TERMINI_PARZIALE:
			switch (versamento.getStatoVersamento()) {
			case ANNULLATO:
			case NON_ESEGUITO:
				if(!irregolare) {
					versamento.setStatoVersamento(StatoVersamento.PARZIALMENTE_ESEGUITO);
					versamentiBD.updateStatoVersamento(versamento.getId(), versamento.getStatoVersamento(), null);
					break;
				}
			default:
				versamento.setStatoVersamento(StatoVersamento.ANOMALO);
				versamentiBD.updateStatoVersamento(versamento.getId(), versamento.getStatoVersamento(), null);
				break;
			}
		case DECORRENZA_TERMINI:
		case PAGAMENTO_NON_ESEGUITO:
			break;
		}	
		
		Notifica notifica = new Notifica(rpt, TipoNotifica.RICEVUTA, bd);
		NotificheBD notificheBD = new NotificheBD(bd);
		notificheBD.insertNotifica(notifica);
		
		bd.commit();
		bd.disableSelectForUpdate();
		
		ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, bd));
		
		ctx.log("rt.acquisizioneOk", versamento.getCodVersamentoEnte(), versamento.getStatoVersamento().toString());
		log.info("RT acquisita con successo.");
		
		return rpt;
	}
}
