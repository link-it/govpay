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
import java.util.Date;
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
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
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
import it.govpay.model.Versamento.StatoPagamento;
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
			case IN_CORSO:
			case RIFIUTATO:
				//Stati interni. Non possono essere stati dell'RPT
				break;
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
		validaCausaleVersamento(singoloVersamento.getCausaleVersamento(), singoloPagamento.getCausaleVersamento(), esito, "CausaleVersamento non corrisponde", true);
		valida(singoloVersamento.getDatiSpecificiRiscossione(), singoloPagamento.getDatiSpecificiRiscossione(), esito, "DatiSpecificiRiscossione non corrisponde", false);

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
	
	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, byte[] rtByte, boolean recupero) throws ServiceException, NdpException, UtilsException, GovPayException {
		return acquisisciRT(codDominio, iuv, ccp, rtByte, recupero, false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, byte[] rtByte, boolean recupero, boolean acquisizioneDaCruscotto) throws ServiceException, NdpException, UtilsException, GovPayException {
		
		RptBD rptBD = null; 
		try {
			IContext ctx = ContextThreadLocal.get();
			BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
			GpContext appContext = (GpContext) ctx.getApplicationContext();
			
			rptBD = new RptBD(configWrapper);
			
			rptBD.setupConnection(configWrapper.getTransactionID());
			
			rptBD.setAtomica(false);
			
			rptBD.setAutoCommit(false);
			
			Rpt rpt = null;
			try {
				rpt = rptBD.getRpt(codDominio, iuv, ccp, true);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio);
			}
			
			// Faccio adesso la select for update, altrimenti in caso di 
			// ricezione di due RT afferenti allo stesso carrello di pagamento
			// vado in deadlock tra la getRpt precedente e la findAll seguente
			
			rptBD.enableSelectForUpdate();
			
			Long idPagamentoPortale = rpt.getIdPagamentoPortale();
			
			@SuppressWarnings("unused")
			List<Rpt> rptsCarrello = null; 
			if(idPagamentoPortale != null) {
				RptFilter filter = rptBD.newFilter();
				filter.setIdPagamentoPortale(idPagamentoPortale);
				rptsCarrello = rptBD.findAll(filter);
			}
			
			// Rifaccio la getRpt adesso che ho il lock per avere lo stato aggiornato
			// infatti in caso di RT concorrente, non viene gestito bene l'errore.
			
			try {
				rpt = rptBD.getRpt(codDominio, iuv, ccp, true);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio);
			}
			
			if(!acquisizioneDaCruscotto) {
				if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
					throw new NdpException(FaultPa.PAA_RT_DUPLICATA, "RT già acquisita in data " + rpt.getDataMsgRicevuta(), rpt.getCodDominio());
				}
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
				if(!acquisizioneDaCruscotto) {
					rpt.setStato(StatoRpt.RT_RIFIUTATA_PA);
					rpt.setDescrizioneStato(e.getDescrizione());
					rpt.setXmlRt(rtByte);
					try {
						rptBD.updateRpt(rpt.getId(), rpt);
						rptBD.commit();
					}catch (ServiceException e1) {
						rptBD.rollback();
					} finally {
						rptBD.disableSelectForUpdate();
					}
				}
				throw e;
			}
			
			// Validazione Semantica
			RtUtils.EsitoValidazione esito = null;
			try {
				ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				esito = RtUtils.validaSemantica(ctRpt, ctRt);
			} catch (JAXBException e) {
				throw e;
			} catch (SAXException e) {
				throw e;
			}
			
			// Caso anomalo. RT gia' acquisita, ma non registrata correttamente:
			if(rpt.getXmlRt() != null && (rpt.getStato().equals(StatoRpt.RPT_ACCETTATA_NODO) || rpt.getStato().equals(StatoRpt.RPT_ACCETTATA_PSP))) {
				try {
					CtRicevutaTelematica oldRT = JaxbUtils.toRT(rpt.getXmlRt(), true);
					if(oldRT.getIdentificativoMessaggioRicevuta().equals(ctRt.getIdentificativoMessaggioRicevuta()) 
							&& rpt.getEsitoPagamento() != null 
							&& rpt.getImportoTotalePagato() != null
							&& rpt.getDenominazioneAttestante() != null ) {
						rpt.setImportoTotalePagato(ctRt.getDatiPagamento().getImportoTotalePagato());
						rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
						rpt.setDescrizioneStato(null);
						try {
							rptBD.updateRpt(rpt.getId(), rpt);
							rptBD.commit();
						}catch (ServiceException e1) {
							rptBD.rollback();
							throw e1;
						} finally {
							rptBD.disableSelectForUpdate();
						}
						throw new NdpException(FaultPa.PAA_RT_DUPLICATA, "RT già acquisita in data " + rpt.getDataMsgRicevuta(), rpt.getCodDominio());
					}
				} catch (ServiceException e) {
					log.warn("Errore nella gestione di una RT gia' acquisita", e);
				} catch (JAXBException e) {
					log.warn("Errore nella gestione di una RT gia' acquisita", e);
				} catch (SAXException e) {
					log.warn("Errore nella gestione di una RT gia' acquisita", e);
				}
			}
			
			if(acquisizioneDaCruscotto) {
				// controllo esito validazione semantica
				
				
				// controllo stato pagamento attuale se e' gia' stato eseguito allora non devo acquisire l'rt
				//EsitoPagamento nuovoEsitoPagamento = Rpt.EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento());
				
				switch (rpt.getEsitoPagamento()) {
				case IN_CORSO:
				case PAGAMENTO_NON_ESEGUITO:
				case DECORRENZA_TERMINI:
				case RIFIUTATO:
					break;
				case DECORRENZA_TERMINI_PARZIALE:
				case PAGAMENTO_ESEGUITO:
				case PAGAMENTO_PARZIALMENTE_ESEGUITO:
					throw new NdpException(FaultPa.PAA_RT_DUPLICATA, "Aggiornamento di RT in pagamenti con esito "+rpt.getEsitoPagamento()+" non supportata.", rpt.getCodDominio());
				}
			}
			
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
				
				try {
					rptBD.updateRpt(rpt.getId(), rpt);
					rptBD.commit();
				}catch (ServiceException e1) {
					rptBD.rollback();
				} finally {
					rptBD.disableSelectForUpdate();
				}
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
			rpt.setDataMsgRicevuta(new Date());
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
			
			Versamento versamento = rpt.getVersamento();
			
			VersamentiBD versamentiBD = new VersamentiBD(rptBD);
			versamentiBD.setAtomica(false); // condivisione della connessione
			
	
			List<CtDatiSingoloPagamentoRT> datiSingoliPagamenti = ctRt.getDatiPagamento().getDatiSingoloPagamento();
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
			
			PagamentiBD pagamentiBD = new PagamentiBD(rptBD);
			pagamentiBD.setAtomica(false); // condivisione della connessione
			
			boolean irregolare = false;
			String irregolarita = null; 
			//String pagamentiNote = "";
			
			String iuvPagamento = rpt.getIuv();
			BigDecimal totalePagato = BigDecimal.ZERO;
			Date dataPagamento = new Date();
			
			List<Pagamento> pagamenti = new ArrayList<Pagamento>();
			
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
				
					// Pagamento rendicontato precedentemente senza RPT
					// Probabilmente sono stati scambiati i tracciati per sanare la situazione
					// Aggiorno il pagamento associando la RT appena arrivata
					
					if(pagamento.getIdRpt() != null) {
						//!! Pagamento gia' notificato da un'altra RPT !!
						throw new ServiceException("ERRORE: RT con pagamento gia' presente in sistema ["+codDominio+"/"+iuv+"/"+ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione()+"]");
					}
					
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
					if(singoloVersamento.getIdTributo() != null && singoloVersamento.getTributo(configWrapper).getCodTributo().equals(it.govpay.model.Tributo.BOLLOT)) {
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
				
				dataPagamento = pagamento.getDataPagamento();
				totalePagato = totalePagato.add(pagamento.getImportoPagato());
				
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
				else {
					ctx.getApplicationLogger().log("rt.aggiornamentoPagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte());
					pagamentiBD.updatePagamento(pagamento);
				}
				
				pagamenti.add(pagamento);
			}
			
			rpt.setPagamenti(pagamenti);
			
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
			case IN_CORSO:
			case RIFIUTATO:
				// Stati interni. Non possono essere stati RPT forniti dal Nodo
				break;
			default:
				break;
			}	
			
			switch (versamento.getStatoVersamento()) {
			case PARZIALMENTE_ESEGUITO:
			case ESEGUITO:
				// Aggiornamento stato promemoria
				versamentiBD.updateStatoPromemoriaAvvisoVersamento(versamento.getId(), true, null);
				versamentiBD.updateStatoPromemoriaScadenzaAppIOVersamento(versamento.getId(), true, null);
				versamentiBD.updateStatoPromemoriaScadenzaMailVersamento(versamento.getId(), true, null);
				
				// aggiornamento informazioni pagamento
				versamentiBD.updateVersamentoInformazioniPagamento(versamento.getId(), dataPagamento, totalePagato, BigDecimal.ZERO, iuvPagamento, StatoPagamento.PAGATO);
				break;
			default:
				// do nothing
				break;
			}
			
			// schedulo l'invio del promemoria ricevuta
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);
			Promemoria promemoria = null;
			if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaAbilitato()) {
				log.debug("Schedulazione invio ricevuta di pagamento in corso...");
				it.govpay.core.business.Promemoria promemoriaBD = new it.govpay.core.business.Promemoria();
				try {
					promemoria = promemoriaBD.creaPromemoriaRicevuta(rpt, versamento, versamento.getTipoVersamentoDominio(configWrapper));
					String msg = "non e' stato trovato un destinatario valido, l'invio non verra' schedulato.";
					if(promemoria.getDestinatarioTo() != null) {
						msg = "e' stato trovato un destinatario valido, l'invio e' stato schedulato con successo.";
						PromemoriaBD promemoriaBD2 = new PromemoriaBD(rptBD);
						promemoriaBD2.setAtomica(false); // condivisione della connessione;
						promemoriaBD2.insertPromemoria(promemoria);
						log.debug("Inserimento promemoria Pendenza["+ versamento.getCodVersamentoEnte() +"] effettuato.");
					}
					log.debug("Creazione promemoria completata: "+msg);
				} catch (JAXBException | SAXException e) {
					log.error("Errore durante la lettura dei dati della RT: ", e.getMessage(),e);
				}
			}
			
			// Aggiornamento dello stato del pagamento portale associato all'RPT
		//	Long idPagamentoPortale = rpt.getIdPagamentoPortale();
			if(idPagamentoPortale != null) {
				PagamentoPortaleUtils.aggiornaPagamentoPortale(idPagamentoPortale, rptBD); 
			}
			
			Notifica notifica = new Notifica(rpt, TipoNotifica.RICEVUTA, configWrapper);
			it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
			boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica,rptBD);
			
			rptBD.commit();
			rptBD.disableSelectForUpdate();
			
			if(schedulaThreadInvio) {
				ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
			}
			
			ctx.getApplicationLogger().log("rt.acquisizioneOk", versamento.getCodVersamentoEnte(), versamento.getStatoVersamento().toString());
			log.info("RT acquisita con successo.");
			
			return rpt;
		}  catch (JAXBException e) {
			throw new ServiceException(e);
		} catch (SAXException e) {
			throw new ServiceException(e);
		} catch (ServiceException e) {
			log.error("Errore acquisizione RT: " + e.getMessage(),e);
			
			if(rptBD != null)
				rptBD.rollback();
			
			throw e;
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
	}
	
	public static void validaCausaleVersamento(String causaleAttesa, String causaleRicevuta, EsitoValidazione esito, String errore, boolean fatal) {
		if(causaleAttesa==null && causaleRicevuta==null) 
			return;
		
		if(causaleAttesa==null || causaleRicevuta==null) {
			esito.addErrore(errore + " [Atteso:\"" + (causaleAttesa != null ? causaleAttesa : "<null>") + "\" Ricevuto:\"" + (causaleRicevuta != null ? causaleRicevuta : "<null>") + "\"]", fatal);
			return;
		}
		
		// cerco il separatore /TXT/
		int s1IndexOf = causaleAttesa.indexOf("/TXT/");
		if(s1IndexOf != -1) {
			causaleAttesa = causaleAttesa.substring(0,s1IndexOf);
		}
		
		int s2IndexOf = causaleRicevuta.indexOf("/TXT/");
		if(s2IndexOf != -1) {
			causaleRicevuta = causaleRicevuta.substring(0,s2IndexOf);
		}
		
		if(!causaleAttesa.equals(causaleRicevuta)) esito.addErrore(errore + " [Atteso:\"" + (causaleAttesa != null ? causaleAttesa : "<null>") + "\" Ricevuto:\"" + (causaleRicevuta != null ? causaleRicevuta : "<null>") + "\"]", fatal);
	}
}
