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

package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.exceptions.GovPayException;
//import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.AvvisaturaOperazione;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Versamento.StatoVersamento;

public class RtUtils extends NdpValidationUtils {
	
	public class ErroreValidazione {
		public ErroreValidazione(String errore, boolean fatal) {
			this.errore = errore;
			this.fatal = fatal;
		}
		public String errore;
		public boolean fatal;
	}

	public class EsitoValidazione {
		public boolean validato;
		public List<ErroreValidazione> errori;
		
		public EsitoValidazione() {
			this.validato = true;
			this.errori = new ArrayList<>();
		}
		
		public void addErrore(String errore, boolean fatal) {
			this.errori.add(new ErroreValidazione(errore, fatal));
			if(fatal) this.validato = false;
		}
		
		public String getFatal() {
			for(ErroreValidazione errore : this.errori) {
				if(errore.fatal) return errore.errore;
			}
			return "-";
		}
		
		public String getDiagnostico() {
			StringBuffer sb = new StringBuffer();
			for(ErroreValidazione errore : this.errori) {
				sb.append("\n");
				sb.append(errore.fatal ? "[Fatal] " : "[Warning] ");
				sb.append(errore.errore);
			}
			return sb.toString();
		}
	}

	private static Logger log = LoggerWrapperFactory.getLogger(RtUtils.class);

	public static EsitoValidazione validaSemantica(CtRichiestaPagamentoTelematico rpt, CtRicevutaTelematica rt) {
		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		valida(rpt.getIdentificativoMessaggioRichiesta(), rt.getRiferimentoMessaggioRichiesta(), esito, "RiferimentoMessaggioRichiesta non corrisponde", true);
		validaSemantica(rpt.getDominio(), rt.getDominio(), esito);
		validaSemantica(rpt.getEnteBeneficiario(), rt.getEnteBeneficiario(), esito);
		validaSemantica(rpt.getSoggettoPagatore(), rt.getSoggettoPagatore(), esito);
		validaSemantica(rpt.getSoggettoVersante(), rt.getSoggettoVersante(), esito);
		validaSemantica(rpt.getDatiVersamento(), rt.getDatiPagamento(), esito);
		return esito;
	}

	private static void validaSemantica(CtDatiVersamentoRPT rpt, CtDatiVersamentoRT rt, EsitoValidazione esito) {
		
		valida(rpt.getCodiceContestoPagamento(), rt.getCodiceContestoPagamento(), esito, "CodiceContestoPagamento non corrisponde", true);
		valida(rpt.getIdentificativoUnivocoVersamento(), rt.getIdentificativoUnivocoVersamento(), esito, "IdentificativoUnivocoVersamento non corrisponde", true);
		
		Rpt.EsitoPagamento esitoPagamento = validaSemanticaCodiceEsitoPagamento(rt.getCodiceEsitoPagamento(), esito);

		// Se siamo in pagamento eseguito, parzialmente eseguito o parzialmente decorso, devono esserci tanti versamenti quanti pagamenti.
		if(esitoPagamento != null) {
			switch (esitoPagamento) {
			case DECORRENZA_TERMINI_PARZIALE:
			case PAGAMENTO_ESEGUITO:
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				if(rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size()) {
					esito.addErrore("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name(), true);
					return;
				}
			case DECORRENZA_TERMINI:
			case PAGAMENTO_NON_ESEGUITO:
				if(rt.getDatiSingoloPagamento().size() != 0 && rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size()) {
					esito.addErrore("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + esitoPagamento.name(), true);
					return;
				}
			}
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < rpt.getDatiSingoloVersamento().size(); i++) {

			CtDatiSingoloVersamentoRPT singoloVersamento = rpt.getDatiSingoloVersamento().get(i);
			CtDatiSingoloPagamentoRT singoloPagamento = null;
			if(rt.getDatiSingoloPagamento().size() != 0) {
				singoloPagamento = rt.getDatiSingoloPagamento().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento, esito);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getSingoloImportoPagato());
			}
		}

		if (importoTotaleCalcolato.compareTo(rt.getImportoTotalePagato()) != 0)
			esito.addErrore("ImportoTotalePagato [" + rt.getImportoTotalePagato().doubleValue() + "] non corrisponde alla somma dei SingoliImportiPagati [" + importoTotaleCalcolato.doubleValue() + "]", true);
		if (esitoPagamento == Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore("ImportoTotalePagato [" + rt.getImportoTotalePagato().doubleValue() + "] diverso da 0 per un pagamento con esito 'Non Eseguito'.", true);
		if (esitoPagamento == Rpt.EsitoPagamento.DECORRENZA_TERMINI && rt.getImportoTotalePagato().compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore("ImportoTotalePagato [" + rt.getImportoTotalePagato().doubleValue() + "] diverso da 0 per un pagamento con esito 'Decorrenza temini'.", true);
		if (esitoPagamento == Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO && rt.getImportoTotalePagato().compareTo(rpt.getImportoTotaleDaVersare()) != 0)
			esito.addErrore("Importo totale del pagamento [" + rt.getImportoTotalePagato().doubleValue() + "] diverso da quanto richiesto [" + rpt.getImportoTotaleDaVersare().doubleValue() + "]", false);
	}

	private static void validaSemanticaSingoloVersamento(CtDatiSingoloVersamentoRPT singoloVersamento, CtDatiSingoloPagamentoRT singoloPagamento, EsitoValidazione esito) {
		valida(singoloPagamento.getCausaleVersamento(), singoloVersamento.getCausaleVersamento(), esito, "CausaleVersamento non corrisponde", true);
		valida(singoloPagamento.getDatiSpecificiRiscossione(), singoloVersamento.getDatiSpecificiRiscossione(), esito, "DatiSpecificiRiscossione non corrisponde", false);

		if(singoloPagamento.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0) {
			if(singoloPagamento.getEsitoSingoloPagamento() == null || singoloPagamento.getEsitoSingoloPagamento().isEmpty()) {
				esito.addErrore("EsitoSingoloPagamento obbligatorio per pagamenti non eseguiti", false);
			}
			if(!singoloPagamento.getIdentificativoUnivocoRiscossione().equals("n/a")) {
				esito.addErrore("IdentificativoUnivocoRiscossione deve essere n/a per pagamenti non eseguiti.", false);
			}
		} else if(singoloPagamento.getSingoloImportoPagato().compareTo(singoloVersamento.getImportoSingoloVersamento()) != 0) {
			esito.addErrore("Importo di un pagamento [" + singoloPagamento.getSingoloImportoPagato().doubleValue() + "] diverso da quanto richiesto [" + singoloVersamento.getImportoSingoloVersamento().doubleValue() + "]", false);
		}
	}

	private static Rpt.EsitoPagamento validaSemanticaCodiceEsitoPagamento(String codiceEsitoPagamento, EsitoValidazione esito) {
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
		esito.addErrore("CodiceEsitoPagamento [" + codiceEsitoPagamento + "] sconosciuto", true);
		return null;
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, String tipoFirma, byte[] rtByte, boolean recupero, BasicBD bd) throws ServiceException, NdpException, UtilsException, GovPayException {
		bd.setAutoCommit(false);
		bd.enableSelectForUpdate();
		
		RptBD rptBD = new RptBD(bd);
		Rpt rpt = null;
		try {
			rpt = rptBD.getRpt(codDominio, iuv, ccp);
		} catch (NotFoundException e) {
			throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio);
		}
		
		if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
			throw new NdpException(FaultPa.PAA_RT_DUPLICATA, "RT già acquisita in data " + rpt.getDataMsgRicevuta(), rpt.getCodDominio());
		}
		
		CtRicevutaTelematica ctRt = null;
		CtRichiestaPagamentoTelematico ctRpt = null;
		// Validazione RT
		try {
			// Validazione Sintattica
			try {
				ctRt = JaxbUtils.toRT(rtByte, true);
			} catch (Exception e) {
				log.warn("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
				if(e.getCause() != null)
					throw new NdpException(FaultPa.PAA_SINTASSI_XSD, e.getCause().getMessage(), codDominio);
				else
					throw new NdpException(FaultPa.PAA_SINTASSI_XSD, e.getMessage(), codDominio);
			}
		} catch (NdpException e) {
			log.warn("Rt rifiutata: " + e.getDescrizione());
			rpt.setStato(StatoRpt.RT_RIFIUTATA_PA);
			rpt.setDescrizioneStato(e.getDescrizione());
			rpt.setXmlRt(rtByte);
			rptBD.updateRpt(rpt.getId(), rpt);
			bd.commit();
			bd.disableSelectForUpdate();
			throw e;
		}
		
		// Validazione Semantica
		RtUtils.EsitoValidazione esito = null;
		try {
			ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			esito = RtUtils.validaSemantica(ctRpt, ctRt);
		} catch (JAXBException e) {
			throw new ServiceException(e);
		} catch (SAXException e) {
			throw new ServiceException(e);
		}
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		if(esito.validato && esito.errori.size() > 0) {
			if(recupero)
				ctx.getApplicationLogger().log("pagamento.recuperoRtValidazioneRtWarn", esito.getDiagnostico());
			else 
				ctx.getApplicationLogger().log("pagamento.validazioneRtWarn", esito.getDiagnostico());
		} 
		
		if (!esito.validato) {
			if(recupero)
				ctx.getApplicationLogger().log("pagamento.recuperoRtValidazioneRtFail", esito.getDiagnostico());
			else 
				ctx.getApplicationLogger().log("pagamento.validazioneRtFail", esito.getDiagnostico());
			
			rpt.setStato(StatoRpt.RT_RIFIUTATA_PA);
			rpt.setDescrizioneStato(esito.getFatal());
			rpt.setXmlRt(rtByte);
			rptBD.updateRpt(rpt.getId(), rpt);
			bd.commit();
			bd.disableSelectForUpdate();
			throw new NdpException(FaultPa.PAA_SEMANTICA, esito.getFatal(), codDominio);
		}
		
		log.info("Acquisizione RT per un importo di " + ctRt.getDatiPagamento().getImportoTotalePagato());
	
		if(recupero) {
			appContext.getTransaction().getLastServer().addGenericProperty(new Property("codMessaggioRicevuta", ctRt.getIdentificativoMessaggioRicevuta()));
			appContext.getTransaction().getLastServer().addGenericProperty(new Property("importo", ctRt.getDatiPagamento().getImportoTotalePagato().toString()));
			appContext.getTransaction().getLastServer().addGenericProperty(new Property("codEsitoPagamento", Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()).toString()));
			ctx.getApplicationLogger().log("rt.rtRecuperoAcquisizione");
		} else {
			appContext.getRequest().addGenericProperty(new Property("codMessaggioRicevuta", ctRt.getIdentificativoMessaggioRicevuta()));
			appContext.getRequest().addGenericProperty(new Property("importo", ctRt.getDatiPagamento().getImportoTotalePagato().toString()));
			appContext.getRequest().addGenericProperty(new Property("codEsitoPagamento", Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()).toString()));
			ctx.getApplicationLogger().log("rt.acquisizione");
		}
		
		rpt.setCodMsgRicevuta(ctRt.getIdentificativoMessaggioRicevuta());
		rpt.setDataMsgRicevuta(ctRt.getDataOraMessaggioRicevuta());
		rpt.setEsitoPagamento(Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()));
		rpt.setImportoTotalePagato(ctRt.getDatiPagamento().getImportoTotalePagato());
		rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
		rpt.setDescrizioneStato(null);
		rpt.setXmlRt(rtByte);
		rpt.setIdTransazioneRt(ContextThreadLocal.get().getTransactionId());
		rpt.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.valueOf(ctRt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco().value()));
		rpt.setIdentificativoAttestante(ctRt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
		rpt.setDenominazioneAttestante(ctRt.getIstitutoAttestante().getDenominazioneAttestante());
		// Aggiorno l'RPT con i dati dell'RT
		rptBD.updateRpt(rpt.getId(), rpt);
		
		Versamento versamento = rpt.getVersamento(bd);
		VersamentiBD versamentiBD = new VersamentiBD(bd);

		List<CtDatiSingoloPagamentoRT> datiSingoliPagamenti = ctRt.getDatiPagamento().getDatiSingoloPagamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
		
		PagamentiBD pagamentiBD = new PagamentiBD(bd);
		
		boolean irregolare = false;
		String irregolarita = null; 
		//String pagamentiNote = "";
		for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoliPagamenti.get(indice);

			// Se non e' stato completato un pagamento, non faccio niente.
			if(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0)
				continue;
			
			//pagamentiNote += "[" +(indice+1) + "/" + ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione() + "/" + ctDatiSingoloPagamentoRT.getSingoloImportoPagato() + "]";
			
			SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);
			
			Pagamento pagamento = null;
			boolean insert = true;
			try {
				pagamento = pagamentiBD.getPagamento(codDominio, iuv, ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione(), indice+1);
				pagamento.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
				pagamento.setRpt(rpt);
				// Se non e' gia' stato incassato, aggiorno lo stato in pagato
				if(!pagamento.getStato().equals(Stato.INCASSATO)) {
					pagamento.setStato(Stato.PAGATO);
					pagamento.setImportoPagato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato());
				} else {
					// Era stato gia incassato.
					// non faccio niente.
					continue;
				}
				insert = false;
			} catch (NotFoundException nfe){
				pagamento = new Pagamento();
				if(singoloVersamento.getIdTributo() != null && singoloVersamento.getTributo(bd).getCodTributo().equals(it.govpay.model.Tributo.BOLLOT)) {
					pagamento.setTipo(TipoPagamento.MBT);
				} else {
					pagamento.setTipo(TipoPagamento.ENTRATA);
				}
				pagamento.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
				pagamento.setRpt(rpt);
				pagamento.setSingoloVersamento(singoloVersamento);
				pagamento.setImportoPagato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato());
				pagamento.setIur(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
				pagamento.setCodDominio(rpt.getCodDominio());
				pagamento.setIuv(rpt.getIuv());
				pagamento.setIndiceDati(indice + 1);
				pagamento.setCommissioniPsp(pagamento.getCommissioniPsp());
			} catch (MultipleResultException e) {
				throw new ServiceException("Identificativo pagamento non univoco: [Dominio:"+codDominio+" Iuv:"+iuv+" Iur:"+ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione()+" Indice:"+(indice + 1)+"]");
			}

			if(ctDatiSingoloPagamentoRT.getAllegatoRicevuta() != null) {
				pagamento.setTipoAllegato(Pagamento.TipoAllegato.valueOf(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTipoAllegatoRicevuta().toString()));
				pagamento.setAllegato(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTestoAllegato());
			}
			
			// Se ho solo aggiornato un pagamento che gia' c'era, non devo fare altro.
			// Se gli importi corrispondono e lo stato era da pagare, il singoloVersamento e' eseguito. Altrimenti irregolare.
			
			if(insert) {
				if(singoloVersamento.getStatoSingoloVersamento().equals(StatoSingoloVersamento.NON_ESEGUITO) && singoloVersamento.getImportoSingoloVersamento().compareTo(pagamento.getImportoPagato()) == 0)
				    singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.ESEGUITO);
				else {
					List<String> anomalie = new ArrayList<>();
					
					if(singoloVersamento.getStatoSingoloVersamento().equals(StatoSingoloVersamento.ESEGUITO)) {
						irregolarita = "Acquisito pagamento duplicato";
						anomalie.add(irregolarita);
						log.warn(irregolarita);
					}
					
					if(singoloVersamento.getImportoSingoloVersamento().compareTo(pagamento.getImportoPagato()) != 0) {
						irregolarita = "L'importo pagato non corrisponde all'importo dovuto.";
						anomalie.add(irregolarita);
						log.warn(irregolarita);
					}
					if(recupero)
						ctx.getApplicationLogger().log("pagamento.recuperoRtAcquisizionePagamentoAnomalo", ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione(), StringUtils.join(anomalie,"\n"));
					else 
						ctx.getApplicationLogger().log("pagamento.acquisizionePagamentoAnomalo", ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione(), StringUtils.join(anomalie,"\n"));
					
					irregolare = true;
					
				}
				ctx.getApplicationLogger().log("rt.acquisizionePagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte(), singoloVersamento.getStatoSingoloVersamento().toString());
				versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), singoloVersamento.getStatoSingoloVersamento());
				pagamentiBD.insertPagamento(pagamento);
			}
			else 
				ctx.getApplicationLogger().log("rt.aggiornamentoPagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte());
				pagamentiBD.updatePagamento(pagamento);
		}
		switch (rpt.getEsitoPagamento()) {
		case PAGAMENTO_ESEGUITO:
			switch (versamento.getStatoVersamento()) {
				case ANNULLATO:
				case NON_ESEGUITO:
					versamento.setStatoVersamento(StatoVersamento.ESEGUITO);
					if(irregolare) {
						versamento.setAnomalo(true);
						versamento.setDescrizioneStato(irregolarita);
					}
					break;
				default:
					versamento.setStatoVersamento(StatoVersamento.ESEGUITO);
					if(irregolare) {
						versamento.setDescrizioneStato(irregolarita);
					}
					versamento.setAnomalo(true);
			}
			
			try { 
				versamentiBD.updateVersamento(versamento);
			} catch (NotFoundException nfe) {
				// Impossibile, l'ho trovato prima
			}
			break;
			
		case PAGAMENTO_PARZIALMENTE_ESEGUITO:
		case DECORRENZA_TERMINI_PARZIALE:
			switch (versamento.getStatoVersamento()) {
				case ANNULLATO:
				case NON_ESEGUITO:
					versamento.setStatoVersamento(StatoVersamento.PARZIALMENTE_ESEGUITO);
					if(irregolare) {
						versamento.setAnomalo(true);
						versamento.setDescrizioneStato(irregolarita);
					}
					break;
				default:
					versamento.setStatoVersamento(StatoVersamento.PARZIALMENTE_ESEGUITO);
					if(irregolare) {
						versamento.setDescrizioneStato(irregolarita);
					}
					versamento.setAnomalo(true);
			}
			
			try { 
				versamentiBD.updateVersamento(versamento);
			} catch (NotFoundException nfe) {
				// Impossibile, l'ho trovato prima
			}
			break;
			
		case DECORRENZA_TERMINI:
		case PAGAMENTO_NON_ESEGUITO:
			break;
		}	
		
		switch (versamento.getStatoVersamento()) {
		case PARZIALMENTE_ESEGUITO:
		case ESEGUITO:
			// Avvisatura
			versamentiBD.updateVersamentoOperazioneAvvisatura(versamento.getId(), AvvisaturaOperazione.DELETE.getValue()); 
			String avvisaturaDigitaleModalitaAnnullamentoAvviso = GovpayConfig.getInstance().getAvvisaturaDigitaleModalitaAnnullamentoAvviso();
			if(!avvisaturaDigitaleModalitaAnnullamentoAvviso.equals(AvvisaturaUtils.AVVISATURA_DIGITALE_MODALITA_USER_DEFINED)) {
				versamentiBD.updateVersamentoModalitaAvvisatura(versamento.getId(), avvisaturaDigitaleModalitaAnnullamentoAvviso.equals("asincrona") ? ModoAvvisatura.ASICNRONA.getValue() : ModoAvvisatura.SINCRONA.getValue());
			}
			// schedulo l'invio dell'avvisatura
			versamentiBD.updateVersamentoStatoAvvisatura(versamento.getId(), true);
			break;
		default:
			// do nothing
			break;
		}
		
		// schedulo l'invio del promemoria ricevuta
		TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(bd);
		Promemoria promemoria = null;
		if(tipoVersamentoDominio.isPromemoriaRicevutaAbilitato()) {
			log.debug("Schedulazione invio ricevuta di pagamento in corso...");
			it.govpay.core.business.Promemoria promemoriaBD = new it.govpay.core.business.Promemoria(bd);
			try {
				promemoria = promemoriaBD.creaPromemoriaRicevuta(rpt, versamento, versamento.getTipoVersamentoDominio(bd));
				String msg = "non e' stato trovato un destinatario valido, l'invio non verra' schedulato.";
				if(promemoria.getDestinatarioTo() != null) {
					msg = "e' stato trovato un destinatario valido, l'invio e' stato schedulato con successo.";
					promemoriaBD.inserisciPromemoria(promemoria);
				}
				log.debug("Creazione promemoria completata: "+msg);
			} catch (JAXBException | SAXException e) {
				log.error("Errore durante la lettura dei dati della RT: ", e.getMessage(),e);
			}
		}
		
		// Aggiornamento dello stato del pagamento portale associato all'RPT
		Long idPagamentoPortale = rpt.getIdPagamentoPortale();
		if(idPagamentoPortale != null) {
			PagamentoPortaleUtils.aggiornaPagamentoPortale(idPagamentoPortale, bd); 
		}
		
		Notifica notifica = new Notifica(rpt, TipoNotifica.RICEVUTA, bd);
		it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica(bd);
		boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica);
		
		bd.commit();
		bd.disableSelectForUpdate();
		
		if(schedulaThreadInvio)
			ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, bd,ctx));
		
		ctx.getApplicationLogger().log("rt.acquisizioneOk", versamento.getCodVersamentoEnte(), versamento.getStatoVersamento().toString());
		log.info("RT acquisita con successo.");
		
		return rpt;
	}
}
