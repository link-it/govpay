/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
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
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
//import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.NotificaException;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.pagopa.beans.utils.JaxbUtils;

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
		
		it.govpay.model.Rpt.EsitoPagamento esitoPagamento = validaSemanticaCodiceEsitoPagamento(rt.getCodiceEsitoPagamento(), esito);

		// Se siamo in pagamento eseguito, parzialmente eseguito o parzialmente decorso, devono esserci tanti versamenti quanti pagamenti.
		if(esitoPagamento != null) {
			String name = esitoPagamento.name();
			switch (esitoPagamento) {
			case DECORRENZA_TERMINI_PARZIALE:
			case PAGAMENTO_ESEGUITO:
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				if(rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size()) {
					esito.addErrore(MessageFormat.format("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo {0}", name), true);
					return;
				}
				break;
			case DECORRENZA_TERMINI:
			case PAGAMENTO_NON_ESEGUITO:
				if(rt.getDatiSingoloPagamento().size() != 0 && rt.getDatiSingoloPagamento().size() != rpt.getDatiSingoloVersamento().size()) {
					esito.addErrore(MessageFormat.format("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo {0}", name), true);
					return;
				}
				break;
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

		BigDecimal importoTotalePagato = rt.getImportoTotalePagato();
		if (importoTotaleCalcolato.compareTo(importoTotalePagato) != 0)
			esito.addErrore(MessageFormat.format("ImportoTotalePagato [{0}] non corrisponde alla somma dei SingoliImportiPagati [{1}]",	importoTotalePagato.doubleValue(), importoTotaleCalcolato.doubleValue()), true);
		if (esitoPagamento == it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO && importoTotalePagato.compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore(MessageFormat.format("ImportoTotalePagato [{0}] diverso da 0 per un pagamento con esito ''Non Eseguito''.",	importoTotalePagato.doubleValue()), true);
		if (esitoPagamento == it.govpay.model.Rpt.EsitoPagamento.DECORRENZA_TERMINI && importoTotalePagato.compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore(MessageFormat.format("ImportoTotalePagato [{0}] diverso da 0 per un pagamento con esito ''Decorrenza temini''.",	importoTotalePagato.doubleValue()), true);
		if (esitoPagamento == it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO && importoTotalePagato.compareTo(rpt.getImportoTotaleDaVersare()) != 0)
			esito.addErrore(MessageFormat.format("Importo totale del pagamento [{0}] diverso da quanto richiesto [{1}]", importoTotalePagato.doubleValue(),	rpt.getImportoTotaleDaVersare().doubleValue()), false);
	}

	private static void validaSemanticaSingoloVersamento(CtDatiSingoloVersamentoRPT singoloVersamento, CtDatiSingoloPagamentoRT singoloPagamento, EsitoValidazione esito) {
		validaCausaleVersamento(singoloVersamento.getCausaleVersamento(), singoloPagamento.getCausaleVersamento(), esito, "CausaleVersamento non corrisponde", true);
		valida(singoloVersamento.getDatiSpecificiRiscossione(), singoloPagamento.getDatiSpecificiRiscossione(), esito, "DatiSpecificiRiscossione non corrisponde", false);

		BigDecimal singoloImportoPagato = singoloPagamento.getSingoloImportoPagato();
		if(singoloImportoPagato.compareTo(BigDecimal.ZERO) == 0) {
			if(singoloPagamento.getEsitoSingoloPagamento() == null || singoloPagamento.getEsitoSingoloPagamento().isEmpty()) {
				esito.addErrore("EsitoSingoloPagamento obbligatorio per pagamenti non eseguiti", false);
			}
			if(!singoloPagamento.getIdentificativoUnivocoRiscossione().equals("n/a")) {
				esito.addErrore("IdentificativoUnivocoRiscossione deve essere n/a per pagamenti non eseguiti.", false);
			}
		} else if(singoloImportoPagato.compareTo(singoloVersamento.getImportoSingoloVersamento()) != 0) {
			esito.addErrore(MessageFormat.format("Importo di un pagamento [{0}] diverso da quanto richiesto [{1}]", singoloImportoPagato.doubleValue(),	singoloVersamento.getImportoSingoloVersamento().doubleValue()), false);
		}
	}

	private static it.govpay.model.Rpt.EsitoPagamento validaSemanticaCodiceEsitoPagamento(String codiceEsitoPagamento, EsitoValidazione esito) {
		try{
			switch (Integer.parseInt(codiceEsitoPagamento)) {
			case 0:
				return it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO;
			case 1:
				return it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
			case 2:
				return it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
			case 3:
				return it.govpay.model.Rpt.EsitoPagamento.DECORRENZA_TERMINI;
			case 4:
				return it.govpay.model.Rpt.EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
			default:
				break;
			} 
		} catch (Throwable e) {
			
		}
		esito.addErrore(MessageFormat.format("CodiceEsitoPagamento [{0}] sconosciuto", codiceEsitoPagamento), true);
		return null;
	}
	
	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, byte[] rtByte, boolean recupero) throws ServiceException, NdpException, UtilsException, GovPayException {
		return acquisisciRT(codDominio, iuv, ccp, rtByte, recupero, false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, String ccp, byte[] rtByte, boolean recupero, boolean acquisizioneDaCruscotto) throws ServiceException, NdpException, UtilsException, GovPayException {
		
		log.info(MessageFormat.format("Acquisizione RT Dominio[{0}], IUV[{1}], CCP [{2}] in corso", codDominio, iuv, ccp));
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
				rpt = rptBD.getRpt(codDominio, iuv, ccp, false); // ricerca della RPT senza caricare il dettaglio versamenti, sv, pagamenti e pagamenti_portale
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio);
			}
			
			// se provo ad acquisire un RT da cruscotto deve essere solo ti vecchio tipo
			if(acquisizioneDaCruscotto) {
				if(!rpt.getVersione().equals(VersioneRPT.SANP_230)) {
					throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, MessageFormat.format("Aggiornamento di RT versione {0} non supportata tramite cruscotto.", rpt.getVersione()), rpt.getCodDominio());
				}
			}
			
			boolean isCarrello = RtUtils.isCarrelloRpt(rpt);
			
			// Faccio adesso la select for update, altrimenti in caso di 
			// ricezione di due RT afferenti allo stesso carrello di pagamento
			// vado in deadlock tra la getRpt precedente e la findAll seguente
			
			rptBD.enableSelectForUpdate();
			
			Long idPagamentoPortale = rpt.getIdPagamentoPortale();
			
			if(isCarrello) {
				@SuppressWarnings("unused")
				List<Rpt> rptsCarrello = null; 
				if(idPagamentoPortale != null) {
					RptFilter filter = rptBD.newFilter();
					filter.setIdPagamentoPortale(idPagamentoPortale);
					rptsCarrello = rptBD.findAll(filter);
				}
			}
			
			// Rifaccio la getRpt adesso che ho il lock per avere lo stato aggiornato
			// infatti in caso di RT concorrente, non viene gestito bene l'errore.
			
			try {
				rpt = rptBD.getRpt(codDominio, iuv, ccp, false); // ricerca della RPT senza caricare il dettaglio versamenti, sv, pagamenti e pagamenti_portale
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, codDominio);
			}
			
			if(!acquisizioneDaCruscotto) {
				if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
					throw new NdpException(FaultPa.PAA_RT_DUPLICATA, MessageFormat.format("RT già acquisita in data {0}", rpt.getDataMsgRicevuta()), rpt.getCodDominio());
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
				log.warn(MessageFormat.format("Rt rifiutata: {0}", e.getDescrizione()));
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
			
			String receiptId = ctRt.getIdentificativoMessaggioRicevuta();
			
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
			
			// lettura dati significativi dalla ricevuta
			CtDatiVersamentoRT datiPagamento = ctRt.getDatiPagamento();
			BigDecimal paymentAmount = datiPagamento.getImportoTotalePagato();
			it.govpay.model.Rpt.EsitoPagamento rptEsito = it.govpay.model.Rpt.EsitoPagamento.toEnum(datiPagamento.getCodiceEsitoPagamento());
			CtIstitutoAttestante istitutoAttestante = ctRt.getIstitutoAttestante();
			CtIdentificativoUnivoco identificativoUnivocoAttestante = istitutoAttestante.getIdentificativoUnivocoAttestante();
			String pspFiscalCode = identificativoUnivocoAttestante.getCodiceIdentificativoUnivoco();
			String pspCompanyName = istitutoAttestante.getDenominazioneAttestante();

			// Caso anomalo. RT gia' acquisita, ma non registrata correttamente:
			if(rpt.getXmlRt() != null && (rpt.getStato().equals(StatoRpt.RPT_ACCETTATA_NODO) || rpt.getStato().equals(StatoRpt.RPT_ACCETTATA_PSP))) {
				try {
					CtRicevutaTelematica oldRT = JaxbUtils.toRT(rpt.getXmlRt(), true);
					if(oldRT.getIdentificativoMessaggioRicevuta().equals(receiptId) 
							&& rpt.getEsitoPagamento() != null 
							&& rpt.getImportoTotalePagato() != null
							&& rpt.getDenominazioneAttestante() != null ) {
						rpt.setImportoTotalePagato(paymentAmount);
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
						throw new NdpException(FaultPa.PAA_RT_DUPLICATA, MessageFormat.format("RT già acquisita in data {0}", rpt.getDataMsgRicevuta()), rpt.getCodDominio());
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
					throw new NdpException(FaultPa.PAA_RT_DUPLICATA, MessageFormat.format("Aggiornamento di RT in pagamenti con esito {0} non supportata.", rpt.getEsitoPagamento()), rpt.getCodDominio());
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
			
			log.info(MessageFormat.format("Acquisizione RT per un importo di {0}", paymentAmount));
			
			if(recupero) {
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("codMessaggioRicevuta", receiptId));
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("importo", paymentAmount.toString()));
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("codEsitoPagamento", rptEsito.toString()));
				ctx.getApplicationLogger().log("rt.rtRecuperoAcquisizione");
			} else {
				appContext.getRequest().addGenericProperty(new Property("codMessaggioRicevuta", receiptId));
				appContext.getRequest().addGenericProperty(new Property("importo", paymentAmount.toString()));
				appContext.getRequest().addGenericProperty(new Property("codEsitoPagamento", rptEsito.toString()));
				ctx.getApplicationLogger().log("rt.acquisizione");
			}
			
			rpt.setCodMsgRicevuta(receiptId);
			rpt.setDataMsgRicevuta(new Date());
			rpt.setEsitoPagamento(rptEsito);
			rpt.setImportoTotalePagato(paymentAmount);
			rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
			rpt.setDescrizioneStato(null);
			rpt.setXmlRt(rtByte);
			rpt.setIdTransazioneRt(ContextThreadLocal.get().getTransactionId());
			rpt.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.valueOf(identificativoUnivocoAttestante.getTipoIdentificativoUnivoco().value()));
			rpt.setIdentificativoAttestante(pspFiscalCode);
			rpt.setDenominazioneAttestante(pspCompanyName);
		
			// Aggiorno l'RPT con i dati dell'RT
			rptBD.updateRpt(rpt.getId(), rpt);
			
			Versamento versamento = rpt.getVersamento(rptBD);
			
			VersamentiBD versamentiBD = new VersamentiBD(rptBD);
			versamentiBD.setAtomica(false); // condivisione della connessione
	
			List<CtDatiSingoloPagamentoRT> datiSingoliPagamenti = datiPagamento.getDatiSingoloPagamento();
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(rptBD);
			
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
				BigDecimal transferAmount = ctDatiSingoloPagamentoRT.getSingoloImportoPagato();
				String iur = ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione();
				Date dataEsitoSingoloPagamento = ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento();
				BigDecimal commissioniApplicatePSP = ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP(); 
				
				// Se non e' stato completato un pagamento, non faccio niente.
				if(transferAmount.compareTo(BigDecimal.ZERO) == 0)
					continue;
				
				//pagamentiNote += "[" +(indice+1) + "/" + ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione() + "/" + ctDatiSingoloPagamentoRT.getSingoloImportoPagato() + "]";
				
				SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);

				Pagamento pagamento = null;
				boolean insert = true;
				
				try {
					pagamento = pagamentiBD.getPagamento(codDominio, iuv, iur,  BigInteger.valueOf(indice+1));

					// Pagamento rendicontato precedentemente senza RPT
					// Probabilmente sono stati scambiati i tracciati per sanare la situazione
					// Aggiorno il pagamento associando la RT appena arrivata
					
					if(pagamento.getIdRpt() != null) {
						//!! Pagamento gia' notificato da un'altra RPT !!
						throw new ServiceException(MessageFormat.format("ERRORE: RT con pagamento gia'' presente in sistema [{0}/{1}/{2}]", codDominio, iuv, iur));
					}
					
					pagamento.setDataPagamento(dataEsitoSingoloPagamento);
					pagamento.setRpt(rpt);
					// Se non e' gia' stato incassato, aggiorno lo stato in pagato
					if(!pagamento.getStato().equals(Stato.INCASSATO)) {
						pagamento.setStato(Stato.PAGATO);
						pagamento.setImportoPagato(transferAmount);
					} else {
						// Era stato gia incassato.
						// non faccio niente.
						continue;
					}
					insert = false;
				} catch (NotFoundException nfe){
					pagamento = creaNuovoPagamento(iuv, iur, ctx, configWrapper, dataEsitoSingoloPagamento, rpt, transferAmount, (indice + 1), singoloVersamento, commissioniApplicatePSP); 
				} catch (MultipleResultException e) {
					throw new ServiceException(MessageFormat.format("Identificativo pagamento non univoco: [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}]", codDominio, iuv, iur, (indice + 1)));
				}
	
				if(ctDatiSingoloPagamentoRT.getAllegatoRicevuta() != null) {
					pagamento.setTipoAllegato(it.govpay.model.Pagamento.TipoAllegato.valueOf(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTipoAllegatoRicevuta().toString()));
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
							ctx.getApplicationLogger().log("pagamento.recuperoRtAcquisizionePagamentoAnomalo", iur, StringUtils.join(anomalie,"\n"));
						else 
							ctx.getApplicationLogger().log("pagamento.acquisizionePagamentoAnomalo", iur, StringUtils.join(anomalie,"\n"));
						
						irregolare = true;
						
					}
					ctx.getApplicationLogger().log("rt.acquisizionePagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte(), singoloVersamento.getStatoSingoloVersamento().toString());
					versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), singoloVersamento.getStatoSingoloVersamento());
					pagamentiBD.insertPagamento(pagamento);
					
					if(!irregolare) {
						checkEsistenzaRendicontazioneAnomalaPerIlPagamento(pagamentiBD, pagamento);
					}
				}
				else {
					ctx.getApplicationLogger().log("rt.aggiornamentoPagamento", pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte());
					pagamentiBD.updatePagamento(pagamento);
				}
				
				pagamenti.add(pagamento);
			}
			
			rpt.setPagamenti(pagamenti);
			
			boolean updateAnomalo = impostaNuovoStatoVersamento(rpt, versamento, irregolare, irregolarita);	
			
			schedulazionePromemoriaENotificaAppIO(rptBD, configWrapper, rpt, idPagamentoPortale, versamento, versamentiBD, iuvPagamento,
					totalePagato, dataPagamento, updateAnomalo);
			
			Notifica notifica = new Notifica(rpt, TipoNotifica.RICEVUTA, configWrapper);
			it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
			boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica,rptBD);
			
			rptBD.commit();
			rptBD.disableSelectForUpdate();
			
			if(schedulaThreadInvio) {
				ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
			}
			
			ctx.getApplicationLogger().log("rt.acquisizioneOk", versamento.getCodVersamentoEnte(), versamento.getStatoVersamento().toString());
			log.info(MessageFormat.format("RT Dominio[{0}], IUV[{1}], CCP [{2}] acquisita con successo.", codDominio, iuv, ccp));
			
			return rpt;
		}  catch (JAXBException e) {
			throw new ServiceException(e);
		} catch (SAXException e) {
			throw new ServiceException(e);
		} catch (NotificaException | CodificaInesistenteException | IOException e) {
			log.error(MessageFormat.format("Errore acquisizione RT: {0}", e.getMessage()),e);
			
			if(rptBD != null) 
				rptBD.rollback();
			
			throw new ServiceException(e);
		} catch (ServiceException e) {
			log.error(MessageFormat.format("Errore acquisizione RT: {0}", e.getMessage()),e);
			
			if(rptBD != null)
				rptBD.rollback();
			
			throw e;
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
	}

	public static boolean isCarrelloRpt(Rpt rpt) {
		boolean isCarrello = false;
		// e' un pagamento modello 1 con carrello se la versione e' SANP_230
		if(rpt != null && (rpt.getVersione().equals(VersioneRPT.SANP_230) && rpt.getModelloPagamento().equals(it.govpay.model.Rpt.modelloPagamentoWISP20))){
			isCarrello = true;
		}
		return isCarrello;
	}
	
	public static Pagamento creaNuovoPagamento(String iuv, String receiptId, IContext ctx, BDConfigWrapper configWrapper,
			Date dataPagamento, Rpt rpt, BigDecimal transferAmount, int idTransfer, SingoloVersamento singoloVersamento, BigDecimal commissioniApplicatePSP) throws ServiceException, UtilsException {
		return creaNuovoPagamento(iuv, receiptId, ctx, configWrapper, dataPagamento, rpt, transferAmount, idTransfer, singoloVersamento, commissioniApplicatePSP, null);
	}

	public static Pagamento creaNuovoPagamento(String iuv, String receiptId, IContext ctx, BDConfigWrapper configWrapper,
			Date dataPagamento, Rpt rpt, BigDecimal transferAmount, int idTransfer, SingoloVersamento singoloVersamento, BigDecimal commissioniApplicatePSP,
			Dominio dominioSingoloVersamento) throws ServiceException, UtilsException {
		Pagamento pagamento = new Pagamento();
		if(singoloVersamento.getIdTributo() != null && singoloVersamento.getTributo(configWrapper).getCodTributo().equals(it.govpay.model.Tributo.BOLLOT)) {
			pagamento.setTipo(TipoPagamento.MBT);
		} else {
			if(dominioSingoloVersamento != null) {
				if(dominioSingoloVersamento.isIntermediato()) {
					pagamento.setTipo(TipoPagamento.ENTRATA);
				} else {
					pagamento.setTipo(TipoPagamento.ENTRATA_PA_NON_INTERMEDIATA);
					ctx.getApplicationLogger().log("pagamento.entrataPaNonIntermediata", dominioSingoloVersamento.getCodDominio(), iuv, receiptId, transferAmount.toString());
				}
			} else {
				pagamento.setTipo(TipoPagamento.ENTRATA);
			}
		}
		
		String codDominio = dominioSingoloVersamento != null ? dominioSingoloVersamento.getCodDominio() : rpt.getCodDominio();
		
		pagamento.setDataPagamento(dataPagamento); // <!--data esecuzione pagamento da parte dell'utente-->
		pagamento.setRpt(rpt);
		pagamento.setSingoloVersamento(singoloVersamento);
		pagamento.setImportoPagato(transferAmount);
		pagamento.setIur(receiptId);
		pagamento.setCodDominio(codDominio);
		pagamento.setIuv(rpt.getIuv());
		pagamento.setIndiceDati(idTransfer);
		pagamento.setCommissioniPsp(commissioniApplicatePSP);
		return pagamento;
	}
	
	public static void schedulazionePromemoriaENotificaAppIO(RptBD rptBD, BDConfigWrapper configWrapper, Rpt rpt, Long idPagamentoPortale,
			Versamento versamento, VersamentiBD versamentiBD, String iuvPagamento, BigDecimal totalePagato,
			Date dataPagamento, boolean updateAnomalo) throws ServiceException, GovPayException, NotificaException {
		switch (versamento.getStatoVersamento()) {
		case PARZIALMENTE_ESEGUITO:
		case ESEGUITO:
			// aggiornamento informazioni pagamento, stato promemoria e avvisatura
			versamentiBD.updateVersamentoInformazioniPagamento(versamento.getId(), dataPagamento, totalePagato, BigDecimal.ZERO, iuvPagamento, StatoPagamento.PAGATO
					, true, null, true, null, true, null, versamento.getStatoVersamento(), versamento.getDescrizioneStato(), updateAnomalo, versamento.isAnomalo());
			
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
						log.debug(MessageFormat.format("Inserimento promemoria Pendenza[{0}] effettuato.", versamento.getCodVersamentoEnte()));
					}
					log.debug("Creazione promemoria completata: "+msg);
				} catch (JAXBException | SAXException e) {
					log.error("Errore durante la lettura dei dati della RT: ", e.getMessage(),e);
				}
			}
			
			//schedulo l'invio della notifica APPIO
			if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaAbilitato()) {
				log.debug("Creo notifica avvisatura ricevuta tramite App IO..."); 
				NotificaAppIo notificaAppIo = new NotificaAppIo(rpt, versamento, it.govpay.model.NotificaAppIo.TipoNotifica.RICEVUTA, configWrapper);
				log.debug("Creazione notifica avvisatura ricevuta tramite App IO completata.");
				NotificheAppIoBD notificheAppIoBD = new NotificheAppIoBD(rptBD);
				notificheAppIoBD.setAtomica(false); // riuso connessione
				notificheAppIoBD.insertNotifica(notificaAppIo);
				log.debug("Inserimento su DB notifica avvisatura ricevuta tramite App IO completata.");
			}
			break;
		default:
			// do nothing
			break;
		}
		
		// Aggiornamento dello stato del pagamento portale associato all'RPT
//	Long idPagamentoPortale = rpt.getIdPagamentoPortale();
		if(idPagamentoPortale != null) {
			PagamentoPortaleUtils.aggiornaPagamentoPortale(idPagamentoPortale, rpt, rptBD); 
		}
	}

	public static boolean impostaNuovoStatoVersamento(Rpt rpt, Versamento versamento, boolean irregolare, String irregolarita) {
		boolean updateAnomalo = false;
		switch (rpt.getEsitoPagamento()) {
		case PAGAMENTO_ESEGUITO:
			switch (versamento.getStatoVersamento()) {
				case ANNULLATO:
				case NON_ESEGUITO:
					versamento.setStatoVersamento(StatoVersamento.ESEGUITO);
					if(irregolare) {
						versamento.setAnomalo(true);
						versamento.setDescrizioneStato(irregolarita);
						updateAnomalo = true;
					}
					break;
				default:
					versamento.setStatoVersamento(StatoVersamento.ESEGUITO);
					if(irregolare) {
						versamento.setDescrizioneStato(irregolarita);
					}
					versamento.setAnomalo(true);
					updateAnomalo = true;
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
						updateAnomalo = true;
					}
					break;
				default:
					versamento.setStatoVersamento(StatoVersamento.PARZIALMENTE_ESEGUITO);
					if(irregolare) {
						versamento.setDescrizioneStato(irregolarita);
					}
					versamento.setAnomalo(true);
					updateAnomalo = true;						
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
		return updateAnomalo;
	}

	public static void checkEsistenzaRendicontazioneAnomalaPerIlPagamento(PagamentiBD pagamentiBD, Pagamento pagamento) throws ServiceException {
		RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(pagamentiBD);
		rendicontazioniBD.setAtomica(false);
		
		try {
			// cerco una eventuale rendicontazione per gli estremi del pagamento, in stato anomala ma non collegata a nessun pagamento
//			rendicontazioniBD.enableSelectForUpdate();

			Rendicontazione rendicontazioneAnomala = rendicontazioniBD.getRendicontazione(pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur(), pagamento.getIndiceDati(), StatoRendicontazione.ANOMALA, true);
			
			// aggiorno la rendicontazione e la metto in stato OK
			rendicontazioneAnomala.setStato(StatoRendicontazione.OK);
			rendicontazioneAnomala.setAnomalie(new ArrayList<>());
			rendicontazioneAnomala.setIdPagamento(pagamento.getId());
			// aggiungo anche il riferimento al singolo versamento se non e' valorizzato
			if(rendicontazioneAnomala.getIdSingoloVersamento() == null && pagamento.getSingoloVersamento(pagamentiBD) != null) {
				rendicontazioneAnomala.setIdSingoloVersamento(pagamento.getSingoloVersamento(pagamentiBD).getId());
			}
			
			rendicontazioniBD.updateRendicontazione(rendicontazioneAnomala);
			
		} catch (NotFoundException e) {
			// se non esiste una rendicontazione in stato anomala con i riferimenti del pagamento  non faccio niente.
		} finally {
//			rendicontazioniBD.disableSelectForUpdate();
		}
	}
	
	public static void validaCausaleVersamento(String causaleAttesa, String causaleRicevuta, EsitoValidazione esito, String errore, boolean fatal) {
		if(causaleAttesa==null && causaleRicevuta==null) 
			return;
		
		if(causaleAttesa==null || causaleRicevuta==null) {
			esito.addErrore(MessageFormat.format("{0} [Atteso:\"{1}\" Ricevuto:\"{2}\"]", errore, (causaleAttesa != null ? causaleAttesa : "<null>"), (causaleRicevuta != null ? causaleRicevuta : "<null>")), fatal);
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
		
		if(!causaleAttesa.equals(causaleRicevuta)) esito.addErrore(MessageFormat.format("{0} [Atteso:\"{1}\" Ricevuto:\"{2}\"]", errore, causaleAttesa, causaleRicevuta), fatal);
	}
}
