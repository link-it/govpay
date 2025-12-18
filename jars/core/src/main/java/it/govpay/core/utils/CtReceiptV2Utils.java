/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.NotificaException;
import it.govpay.core.utils.RtUtils.EsitoValidazione;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class CtReceiptV2Utils  extends NdpValidationUtils {

	private static final String ERRORE_IMPORTO_DEL_PAGAMENTO_IN_POSIZIONE_0_1_DIVERSO_DA_QUANTO_RICHIESTO_2 = "Importo del pagamento in posizione {0} [{1}] diverso da quanto richiesto [{2}]";
	private static final String ERRORE_ID_TRANSFER_NON_CORRISPONDENTE_PER_IL_PAGAMENTO_IN_POSIZIONE_0 = "IdTransfer non corrispondente per il pagamento in posizione [{0}]";
	private static final String ERRORE_IMPORTO_TOTALE_DEL_PAGAMENTO_0_DIVERSO_DA_QUANTO_RICHIESTO_1 = "Importo totale del pagamento [{0}] diverso da quanto richiesto [{1}]";
	private static final String ERRORE_IMPORTO_TOTALE_PAGATO_0_DIVERSO_DA_0_PER_UN_PAGAMENTO_CON_ESITO_KO = "ImportoTotalePagato [{0}] diverso da 0 per un pagamento con esito ''KO''.";
	private static final String ERRORE_IMPORTO_TOTALE_PAGATO_0_NON_CORRISPONDE_ALLA_SOMMA_DEI_SINGOLI_IMPORTI_PAGATI_1 = "ImportoTotalePagato [{0}] non corrisponde alla somma dei SingoliImportiPagati [{1}]";
	private static final String ERRORE_CREDITOR_REFERENCE_ID_NON_CORRISPONDE = "CreditorReferenceId non corrisponde";
	private static final String ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0 = "Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo {0}";
	private static Logger log = LoggerWrapperFactory.getLogger(CtReceiptV2Utils.class);

	public static EsitoValidazione validaSemantica(PaGetPaymentRes ctRpt, PaSendRTV2Request ctRt) {
		CtPaymentPA ctPaymentPA = ctRpt.getData();
		CtReceiptV2 ctReceipt = ctRt.getReceipt();

		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		valida(ctPaymentPA.getCreditorReferenceId(), ctReceipt.getCreditorReferenceId(), esito, ERRORE_CREDITOR_REFERENCE_ID_NON_CORRISPONDE, true); // Identificativo di correlazione dei due messaggi lo IUV???

		StOutcome ctRecepitOutcome = ctReceipt.getOutcome(); // esito pagamento ha solo due valori OK/KO
		String name = ctRecepitOutcome.name();
		switch (ctRecepitOutcome) {
		case OK:
			if(ctReceipt.getTransferList() != null && ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		case KO:
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty() && ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < ctPaymentPA.getTransferList().getTransfer().size(); i++) {

			CtTransferPA singoloVersamento = ctPaymentPA.getTransferList().getTransfer().get(i);
			CtTransferPAReceiptV2 singoloPagamento = null;
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty()) {
				singoloPagamento = ctReceipt.getTransferList().getTransfer().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento, (i+1), esito);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getTransferAmount());
			}
		}

		BigDecimal paymentAmount = ctReceipt.getPaymentAmount();
		if (importoTotaleCalcolato.compareTo(paymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_NON_CORRISPONDE_ALLA_SOMMA_DEI_SINGOLI_IMPORTI_PAGATI_1, paymentAmount.doubleValue(), importoTotaleCalcolato.doubleValue()), true);
		if (ctRecepitOutcome.equals(StOutcome.KO) && paymentAmount.compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_DIVERSO_DA_0_PER_UN_PAGAMENTO_CON_ESITO_KO, paymentAmount.doubleValue()), true);
		BigDecimal ctPaymentPAPaymentAmount = ctPaymentPA.getPaymentAmount();
		if (ctRecepitOutcome.equals(StOutcome.OK) && paymentAmount.compareTo(ctPaymentPAPaymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_DEL_PAGAMENTO_0_DIVERSO_DA_QUANTO_RICHIESTO_1, paymentAmount.doubleValue(), ctPaymentPAPaymentAmount.doubleValue()), false);

		return esito;
	}

	private static void validaSemanticaSingoloVersamento(CtTransferPA singoloVersamento, CtTransferPAReceiptV2 singoloPagamento, int pos, EsitoValidazione esito) {

		if(singoloPagamento.getIdTransfer() != singoloVersamento.getIdTransfer()) {
			esito.addErrore(MessageFormat.format(ERRORE_ID_TRANSFER_NON_CORRISPONDENTE_PER_IL_PAGAMENTO_IN_POSIZIONE_0, pos), false);
		}
		valida(singoloVersamento.getTransferCategory(), singoloPagamento.getTransferCategory(), esito, "TransferCategory non corrisponde", false);

		if(singoloPagamento.getTransferAmount().compareTo(BigDecimal.ZERO) == 0) {

		} else if(singoloPagamento.getTransferAmount().compareTo(singoloVersamento.getTransferAmount()) != 0) {
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_DEL_PAGAMENTO_IN_POSIZIONE_0_1_DIVERSO_DA_QUANTO_RICHIESTO_2, pos, singoloPagamento.getTransferAmount().doubleValue(), singoloVersamento.getTransferAmount().doubleValue()), false);
		}
	}

	public static void validaSemantica(CtSubject rpt, CtSubject rt, EsitoValidazione esito) {
		valida(rpt.getFullName(),rt.getFullName(), esito, "FullNameDebtor non corrisponde", false);
		valida(rpt.getPostalCode(),rt.getPostalCode(), esito, "PostalCodeDebtor non corrisponde", false);
		valida(rpt.getCivicNumber(),rt.getCivicNumber(), esito, "CivicNumberDebtor non corrisponde", false);
		valida(rpt.getEMail(),rt.getEMail(), esito, "EMailDebtor non corrisponde", false);
		if(rpt.getUniqueIdentifier() != null && rt.getUniqueIdentifier() != null) {
			valida(rpt.getUniqueIdentifier().getEntityUniqueIdentifierValue(),rt.getUniqueIdentifier().getEntityUniqueIdentifierValue(), esito, "UniqueIdentifierDebtor non corrisponde", true);
		}
		valida(rpt.getStreetName(),rt.getStreetName(), esito, "StreetNameDebtor non corrisponde", false);
		valida(rpt.getCity(),rt.getCity(), esito, "CityDebtor non corrisponde", false);
		valida(rpt.getCountry(),rt.getCountry(), esito, "CountryDebtor non corrisponde", false);
		valida(rpt.getStateProvinceRegion(),rt.getStateProvinceRegion(), esito, "StateProvinceDebtor non corrisponde", false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, PaSendRTV2Request ctRt, boolean recupero) throws ServiceException, NdpException, UtilsException, GovPayException {
		return acquisisciRT(codDominio, iuv, ctRt, recupero, false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, PaSendRTV2Request ctRt, boolean recupero, boolean acquisizioneDaCruscotto) throws ServiceException, NdpException, UtilsException, GovPayException {

		if(ctRt == null || ctRt.getReceipt() == null) throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Ricevuta vuota", codDominio);

		CtReceiptV2 ctReceipt = ctRt.getReceipt();
		String receiptId = ctReceipt.getReceiptId();

		log.info("Acquisizione RT Dominio[{}], IUV[{}], ReceiptID [{}] in corso", codDominio, iuv, receiptId);
		RptBD rptBD = null; 
		try {
			IContext ctx = ContextThreadLocal.get();
			BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
			GpContext appContext = (GpContext) ctx.getApplicationContext();

			// lettura dati significativi dalla ricevuta
			BigDecimal paymentAmount = ctReceipt.getPaymentAmount();
			Date dataPagamento = ctReceipt.getPaymentDateTime() != null ? DateUtils.toJavaDate(ctReceipt.getPaymentDateTime()) : new Date();
			StOutcome ctReceiptOutcome = ctReceipt.getOutcome();
			it.govpay.model.Rpt.EsitoPagamento rptEsito = ctReceiptOutcome.equals(StOutcome.OK) ? it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO : it.govpay.model.Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO; 
			String pspFiscalCode = ctReceipt.getPspFiscalCode();
			String pspCompanyName = ctReceipt.getPSPCompanyName();
			String idPSP = ctReceipt.getIdPSP();
			String idChannel = ctReceipt.getIdChannel();

			rptBD = new RptBD(configWrapper);

			rptBD.setupConnection(configWrapper.getTransactionID());

			rptBD.setAtomica(false);

			rptBD.setAutoCommit(false);

			VersamentiBD versamentiBD = new VersamentiBD(rptBD);

			versamentiBD.setAtomica(false);

			boolean update = true;
			Rpt rpt = null;
			Long idPagamentoPortale = null;
			VersioneRPT versioneRPTAttesa = it.govpay.model.Rpt.VersioneRPT.SANP_321_V2;
			try { 
				List<Rpt> listaTransazioniPerDominioIuv = rptBD.getRpt(codDominio, iuv, null, null);
				
				if(listaTransazioniPerDominioIuv.isEmpty()) { // replico il comportamento della vecchia getRpt, se non trovo transazioni devo acquisire la ricevuta ricostruendo la transazione
					throw new NotFoundException("Nessuna RPT Dominio: ["+codDominio+"], Iuv: ["+iuv+"] corrisponde ai parametri indicati.");
				}
				
				// verifico che la ricevuta receitId non sia gia' stata acquisita.
				for (Rpt rpt2 : listaTransazioniPerDominioIuv) {
					if(rpt2.getStato().equals(StatoRpt.RT_ACCETTATA_PA) && rpt2.getCcp().equals(receiptId)) {
						
						String dataMsgRicevutaString = rpt2.getDataMsgRicevuta() != null ? SimpleDateFormatUtils.newSimpleDateFormatDataOra().format(rpt2.getDataMsgRicevuta()) : "N.D.";
						throw new NdpException(FaultPa.PAA_RECEIPT_DUPLICATA, MessageFormat.format("La ricevuta [Dominio: {0}, IUV: {1}, ReceiptID: {2}] è già acquisita in data {3}.", codDominio, iuv, receiptId, 
								dataMsgRicevutaString), rpt2.getCodDominio());
					}
				}

				// Faccio adesso la select for update, altrimenti in caso di 
				// ricezione di due RT afferenti allo stesso carrello di pagamento
				// vado in deadlock tra la getRpt precedente e la findAll seguente

				rptBD.enableSelectForUpdate();

				// Rifaccio la getRpt adesso che ho il lock per avere lo stato aggiornato
				// infatti in caso di RT concorrente, non viene gestito bene l'errore.

				try {
					rpt = rptBD.getRpt(codDominio, iuv, null, null, false); // ricerca della RPT senza caricare il dettaglio versamenti, sv, pagamenti e pagamenti_portale
				} catch (NotFoundException e) {
					throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, e.getMessage(), codDominio);
				}

				PaGetPaymentRes ctRpt = null; 
				PaGetPaymentV2Response ctRptV2 = null;
				CtRichiestaPagamentoTelematico ctRptSanp230 = null;

				// Validazione Semantica
				RtUtils.EsitoValidazione esito = new RtUtils().new EsitoValidazione();
				if(rpt.getXmlRpt() != null) {
					// controllo versione RPT, PagoPA puo' inviare una paSendRT anche se l'attivazione e' stata con una paGetPaymentV2
					// 2024-11-04 recupero RT: il servizio rest di recupero RT restituisce sempre una paSendRT2, e' necessario controlare se la RPT e' una SANP_230
					if(!rpt.getVersione().equals(versioneRPTAttesa)) {
						
						if(rpt.getVersione().equals(VersioneRPT.SANP_240) || rpt.getVersione().equals(VersioneRPT.RPTV1_RTV2)) {
							// indico che questa transazione e' ibrida
							rpt.setVersione(VersioneRPT.RPTV1_RTV2);
							

							try {
								ctRpt = JaxbUtils.toPaGetPaymentResRPT(rpt.getXmlRpt(), false);
								esito = CtReceiptV2Utils.validaSemantica(ctRpt, ctRt);
							} catch (JAXBException | SAXException e) {
								throw e;
							}
						} else { // sanp 230
							// indico che questa transazione e' ibrida
							rpt.setVersione(VersioneRPT.RPTSANP230_RTV2);
							

							try {
								ctRptSanp230 = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
								esito = CtReceiptV2Utils.validaSemantica(ctRptSanp230, ctRt);
							} catch (JAXBException | SAXException e) {
								throw e;
							}
						}
					} else {
						try {
							ctRptV2 = JaxbUtils.toPaGetPaymentV2ResponseRPT(rpt.getXmlRpt(), false);
							esito = CtReceiptV2Utils.validaSemantica(ctRptV2, ctRt);
						} catch (JAXBException | SAXException e) {
							throw e;
						}
					}
				}

				// se e' gia' presente una ricevuta devo inserire una nuova transazione
				if(rpt.getXmlRt() != null) {
					// devo fare un insert e non un'update
					update = false;
				}
				
				if(esito.validato && !esito.errori.isEmpty()) {
					if(recupero)
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_VALIDAZIONE_RT_WARN, esito.getDiagnostico());
					else 
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_VALIDAZIONE_RT_WARN, esito.getDiagnostico());
				} 

				if (!esito.validato) {
					if(recupero)
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_VALIDAZIONE_RT_FAIL, esito.getDiagnostico());
					else 
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_VALIDAZIONE_RT_FAIL, esito.getDiagnostico());

					rpt.setStato(StatoRpt.RT_RIFIUTATA_PA);
					rpt.setDescrizioneStato(esito.getFatal());
					rpt.setXmlRt(JaxbUtils.toByte(ctRt));
					
					try {
						if(update) {
							rptBD.updateRpt(rpt);
						} else {
							// inserisco il ccp per le ricerche
							rpt.setCcp(receiptId);
							rpt.setCodMsgRicevuta(receiptId);
							rpt.setDataMsgRicevuta(new Date());
							rptBD.insertRpt(rpt);
						}
						rptBD.commit();
					}catch (ServiceException e1) {
						rptBD.rollback();
					} finally {
						rptBD.disableSelectForUpdate();
					}
					throw new NdpException(FaultPa.PAA_SEMANTICA, esito.getFatal(), codDominio);
				}

				log.info("Acquisizione RT per un importo di {}", paymentAmount);

				if(recupero) {
					appContext.getTransaction().getLastServer().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_MESSAGGIO_RICEVUTA, receiptId));
					appContext.getTransaction().getLastServer().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IMPORTO, paymentAmount.toString()));
					appContext.getTransaction().getLastServer().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_ESITO_PAGAMENTO, rptEsito.toString()));
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_RT_RECUPERO_ACQUISIZIONE);
				} else {
					appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_MESSAGGIO_RICEVUTA, receiptId));
					appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IMPORTO, paymentAmount.toString()));
					appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_ESITO_PAGAMENTO, rptEsito.toString()));
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_ACQUISIZIONE);
				}
			} catch (NotFoundException e) {
				// devo fare un insert e non un'update
				update = false;
				// Per la funzionalita' standin, se non trovo una RPT salvata allora creo una entry a partire dai dati delle RT e procedo con l'acquisizione della ricevuta. 
				try {
					rpt = ricostruisciRPT(codDominio, iuv, ctReceipt, TipoVersamento.ATTIVATO_PRESSO_PSP, ModelloPagamento.ATTIVATO_PRESSO_PSP, configWrapper, versamentiBD);
				} catch (NotFoundException e1) {
					// Pendenza non trovata
					throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, e1.getMessage(), codDominio);
				}
			}

			// aggiornamento del campo CCP con il valore RecepitID
			rpt.setCcp(receiptId);
			rpt.setCodMsgRicevuta(receiptId);
			rpt.setDataMsgRicevuta(new Date());
			rpt.setEsitoPagamento(rptEsito);
			rpt.setImportoTotalePagato(paymentAmount);
			rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
			rpt.setDescrizioneStato(null);
			rpt.setXmlRt(JaxbUtils.toByte(ctRt));
			rpt.setIdTransazioneRt(ContextThreadLocal.get().getTransactionId());
			rpt.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.G);
			rpt.setIdentificativoAttestante(pspFiscalCode);
			rpt.setDenominazioneAttestante(pspCompanyName);
			rpt.setCodPsp(idPSP);
			rpt.setCodCanale(idChannel);
			// aggiorno rpt con il tipoversamento ricevuto in risposta
			rpt.setTipoVersamento(ctReceipt.getPaymentMethod());

			if(update) {
				// Aggiorno l'RPT con i dati dell'RT
				rptBD.updateRpt(rpt);
			} else {
				rptBD.insertRpt(rpt);
			}

			Versamento versamento = rpt.getVersamento(rptBD);

			List<CtTransferPAReceiptV2> datiSingoliPagamenti = (ctReceipt.getTransferList() != null) ? ctReceipt.getTransferList().getTransfer() : new ArrayList<>();
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(rptBD);

			PagamentiBD pagamentiBD = new PagamentiBD(rptBD);
			pagamentiBD.setAtomica(false); // condivisione della connessione

			boolean irregolare = false;
			String irregolarita = null; 

			String iuvPagamento = rpt.getIuv();
			BigDecimal totalePagato = BigDecimal.ZERO;


			List<Pagamento> pagamenti = new ArrayList<>();

			for(int indice = 0; indice < datiSingoliPagamenti.size(); indice++) {
				CtTransferPAReceiptV2 ctDatiSingoloPagamentoRT = datiSingoliPagamenti.get(indice);
				BigDecimal transferAmount = ctDatiSingoloPagamentoRT.getTransferAmount();
				String codDominioSingoloPagamento = ctDatiSingoloPagamentoRT.getFiscalCodePA();
				int idTransfer = ctDatiSingoloPagamentoRT.getIdTransfer();
				BigDecimal commissioniApplicatePSP = ctReceipt.getFee();

				// Se non e' stato completato un pagamento, non faccio niente.
				if(transferAmount.compareTo(BigDecimal.ZERO) == 0)
					continue;

				SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);
				Dominio dominioSingoloVersamento = null;
				try {
					dominioSingoloVersamento = AnagraficaManager.getDominio(configWrapper, codDominioSingoloPagamento);
				} catch (NotFoundException e1) {
					dominioSingoloVersamento = versamento.getDominio(configWrapper);
				}

				Pagamento pagamento = null;
				boolean insert = true;
				try {
					pagamento = pagamentiBD.getPagamento(dominioSingoloVersamento.getCodDominio(), iuv, receiptId, BigInteger.valueOf(idTransfer));

					// Pagamento rendicontato precedentemente senza RPT
					// Probabilmente sono stati scambiati i tracciati per sanare la situazione
					// Aggiorno il pagamento associando la RT appena arrivata

					if(pagamento.getIdRpt() != null) {
						//!! Pagamento gia' notificato da un'altra RPT !!
						throw new ServiceException(MessageFormat.format("ERRORE: RT con pagamento gia'' presente in sistema [{0}/{1}/{2}]",	dominioSingoloVersamento.getCodDominio(), iuv, receiptId));
					}

					pagamento.setDataPagamento(dataPagamento); // <!--data esecuzione pagamento da parte dell'utente-->
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
					pagamento = RtUtils.creaNuovoPagamento(iuv, receiptId, ctx, configWrapper, dataPagamento, rpt, transferAmount, idTransfer, singoloVersamento, commissioniApplicatePSP, dominioSingoloVersamento);
				} catch (MultipleResultException e) {
					throw new ServiceException(MessageFormat.format("Identificativo pagamento non univoco: [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}]", dominioSingoloVersamento.getCodDominio(),	iuv, receiptId, idTransfer));
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
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_ACQUISIZIONE_PAGAMENTO_ANOMALO, receiptId, StringUtils.join(anomalie,"\n"));
						else 
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_ACQUISIZIONE_PAGAMENTO_ANOMALO, receiptId, StringUtils.join(anomalie,"\n"));

						irregolare = true;

					}
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_ACQUISIZIONE_PAGAMENTO, pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte(), singoloVersamento.getStatoSingoloVersamento().toString());
					versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), singoloVersamento.getStatoSingoloVersamento());
					pagamentiBD.insertPagamento(pagamento);

					if(!irregolare) {
						RtUtils.checkEsistenzaRendicontazioneAnomalaPerIlPagamento(pagamentiBD, pagamento);
					}
				}
				else {
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_AGGIORNAMENTO_PAGAMENTO, pagamento.getIur(), pagamento.getImportoPagato().toString(), singoloVersamento.getCodSingoloVersamentoEnte());
					pagamentiBD.updatePagamento(pagamento);
				}

				pagamenti.add(pagamento);
			}

			rpt.setPagamenti(pagamenti);

			boolean updateAnomalo = RtUtils.impostaNuovoStatoVersamento(rpt, versamento, irregolare, irregolarita);	

			RtUtils.schedulazionePromemoriaENotificaAppIO(rptBD, configWrapper, rpt, idPagamentoPortale, versamento, versamentiBD, iuvPagamento,
					totalePagato, dataPagamento, updateAnomalo);

			Notifica notifica = new Notifica(rpt, TipoNotifica.RICEVUTA, configWrapper);
			it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
			boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica,rptBD);

			rptBD.commit();
			rptBD.disableSelectForUpdate();

			if(schedulaThreadInvio) {
				ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
			}

			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RT_ACQUISIZIONE_OK, versamento.getCodVersamentoEnte(), versamento.getStatoVersamento().toString());
			log.info("RT Dominio[{}], IUV[{}], ReceiptID [{}] acquisita con successo.", codDominio, iuv, receiptId);

			return rpt;
		}  catch (JAXBException | SAXException e) {
			throw new ServiceException(e);
		} catch (NotificaException | IOException e) {
			LogUtils.logError(log, MessageFormat.format("Errore acquisizione RT: {0}", e.getMessage()),e);

			if(rptBD != null) 
				rptBD.rollback();

			throw new ServiceException(e);
		} catch (ServiceException e) {
			LogUtils.logError(log, MessageFormat.format("Errore acquisizione RT: {0}", e.getMessage()),e);

			if(rptBD != null)
				rptBD.rollback();

			throw e;
		} finally {
			if(rptBD != null) {
				// ripristino autocommit
				if(!rptBD.isAutoCommit() ) {
					rptBD.setAutoCommit(true);
				}
				
				rptBD.closeConnection();
			}
		}
	}

	public static Rpt ricostruisciRPT(String codDominio, String iuv, CtReceiptV2 ctReceipt, TipoVersamento tipoVersamento,
			ModelloPagamento modelloPagamento, BDConfigWrapper configWrapper, BasicBD basicBd) throws ServiceException, NotFoundException {
		String receiptId = ctReceipt.getReceiptId();
		Date paymentDateTime = DateUtils.toJavaDate(ctReceipt.getPaymentDateTime());
		VersioneRPT versioneRPT = VersioneRPT.SANP_321_V2;
		return CtReceiptUtils.ricostruisciRPT(codDominio, iuv, receiptId, paymentDateTime, versioneRPT, tipoVersamento, modelloPagamento, configWrapper, basicBd);
	}

	public static EsitoValidazione validaSemantica(PaGetPaymentV2Response ctRpt, PaSendRTV2Request ctRt) {
		CtPaymentPAV2 ctPaymentPA = ctRpt.getData();
		CtReceiptV2 ctReceipt = ctRt.getReceipt();

		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		valida(ctPaymentPA.getCreditorReferenceId(), ctReceipt.getCreditorReferenceId(), esito, ERRORE_CREDITOR_REFERENCE_ID_NON_CORRISPONDE, true); // Identificativo di correlazione dei due messaggi lo IUV???
		validaSemantica(ctPaymentPA.getDebtor(), ctReceipt.getDebtor(), esito);

		StOutcome ctRecepitOutcome = ctReceipt.getOutcome(); // esito pagamento ha solo due valori OK/KO
		String name = ctRecepitOutcome.name();
		switch (ctRecepitOutcome) {
		case OK:
			if(ctReceipt.getTransferList() != null && ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		case KO:
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty() && ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < ctPaymentPA.getTransferList().getTransfer().size(); i++) {

			CtTransferPAV2 singoloVersamento = ctPaymentPA.getTransferList().getTransfer().get(i);
			CtTransferPAReceiptV2 singoloPagamento = null;
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty()) {
				singoloPagamento = ctReceipt.getTransferList().getTransfer().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento, (i+1), esito);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getTransferAmount());
			}
		}

		BigDecimal paymentAmount = ctReceipt.getPaymentAmount();
		if (importoTotaleCalcolato.compareTo(paymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_NON_CORRISPONDE_ALLA_SOMMA_DEI_SINGOLI_IMPORTI_PAGATI_1,	paymentAmount.doubleValue(), importoTotaleCalcolato.doubleValue()), true);
		if (ctRecepitOutcome.equals(StOutcome.KO) && paymentAmount.compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_DIVERSO_DA_0_PER_UN_PAGAMENTO_CON_ESITO_KO, paymentAmount.doubleValue()), true);
		BigDecimal ctPaymentPAPaymentAmount = ctPaymentPA.getPaymentAmount();
		if (ctRecepitOutcome.equals(StOutcome.OK) && paymentAmount.compareTo(ctPaymentPAPaymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_DEL_PAGAMENTO_0_DIVERSO_DA_QUANTO_RICHIESTO_1, paymentAmount.doubleValue(), ctPaymentPAPaymentAmount.doubleValue()), false);

		return esito;
	}

	private static void validaSemanticaSingoloVersamento(CtTransferPAV2 singoloVersamento, CtTransferPAReceiptV2 singoloPagamento, int pos, EsitoValidazione esito) {

		if(singoloPagamento.getIdTransfer() != singoloVersamento.getIdTransfer()) {
			esito.addErrore(MessageFormat.format(ERRORE_ID_TRANSFER_NON_CORRISPONDENTE_PER_IL_PAGAMENTO_IN_POSIZIONE_0, pos), false);
		}
		valida(singoloVersamento.getTransferCategory(), singoloPagamento.getTransferCategory(), esito, "TransferCategory non corrisponde", false);

		if(singoloPagamento.getTransferAmount().compareTo(BigDecimal.ZERO) == 0) {
		} else if(singoloPagamento.getTransferAmount().compareTo(singoloVersamento.getTransferAmount()) != 0) {
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_DEL_PAGAMENTO_IN_POSIZIONE_0_1_DIVERSO_DA_QUANTO_RICHIESTO_2, pos, singoloPagamento.getTransferAmount().doubleValue(), singoloVersamento.getTransferAmount().doubleValue()), false);
		}
	}
	
	public static EsitoValidazione validaSemantica(CtRichiestaPagamentoTelematico ctRpt, PaSendRTV2Request ctRt) {
		CtDatiVersamentoRPT datiVersamento = ctRpt.getDatiVersamento();
		CtReceiptV2 ctReceipt = ctRt.getReceipt();

		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		valida(datiVersamento.getIdentificativoUnivocoVersamento(), ctReceipt.getCreditorReferenceId(), esito, ERRORE_CREDITOR_REFERENCE_ID_NON_CORRISPONDE, true); // Identificativo di correlazione dei due messaggi lo IUV???

		validaSemantica(ctRpt.getSoggettoPagatore(), ctReceipt.getDebtor(), esito);

		StOutcome ctRecepitOutcome = ctReceipt.getOutcome(); // esito pagamento ha solo due valori OK/KO
		String name = ctRecepitOutcome.name();
		switch (ctRecepitOutcome) {
		case OK:
			if(ctReceipt.getTransferList() != null && ctReceipt.getTransferList().getTransfer().size() != datiVersamento.getDatiSingoloVersamento().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		case KO:
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty() && ctReceipt.getTransferList().getTransfer().size() != datiVersamento.getDatiSingoloVersamento().size()) {
				esito.addErrore(MessageFormat.format(ERRORE_NUMERO_DI_PAGAMENTI_DIVERSO_DAL_NUMERO_DI_VERSAMENTI_PER_UNA_RICEVUTA_DI_TIPO_0, name), true);
				return esito;
			}
			break;
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < datiVersamento.getDatiSingoloVersamento().size(); i++) {

			CtDatiSingoloVersamentoRPT singoloVersamento = datiVersamento.getDatiSingoloVersamento().get(i);
			CtTransferPAReceiptV2 singoloPagamento = null;
			if(ctReceipt.getTransferList() != null && !ctReceipt.getTransferList().getTransfer().isEmpty()) {
				singoloPagamento = ctReceipt.getTransferList().getTransfer().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento, (i+1), esito);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getTransferAmount());
			}
		}

		BigDecimal paymentAmount = ctReceipt.getPaymentAmount();
		if (importoTotaleCalcolato.compareTo(paymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_NON_CORRISPONDE_ALLA_SOMMA_DEI_SINGOLI_IMPORTI_PAGATI_1, paymentAmount.doubleValue(), importoTotaleCalcolato.doubleValue()), true);
		if (ctRecepitOutcome.equals(StOutcome.KO) && paymentAmount.compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_PAGATO_0_DIVERSO_DA_0_PER_UN_PAGAMENTO_CON_ESITO_KO, paymentAmount.doubleValue()), true);
		BigDecimal ctPaymentPAPaymentAmount = datiVersamento.getImportoTotaleDaVersare();
		if (ctRecepitOutcome.equals(StOutcome.OK) && paymentAmount.compareTo(ctPaymentPAPaymentAmount) != 0)
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_TOTALE_DEL_PAGAMENTO_0_DIVERSO_DA_QUANTO_RICHIESTO_1, paymentAmount.doubleValue(), ctPaymentPAPaymentAmount.doubleValue()), false);

		return esito;
	}
	
	public static void validaSemantica(CtSoggettoPagatore rpt, CtSubject rt, EsitoValidazione esito) {
		valida(rpt.getAnagraficaPagatore(),rt.getFullName(), esito, "FullNameDebtor non corrisponde", false);
		valida(rpt.getCapPagatore(),rt.getPostalCode(), esito, "PostalCodeDebtor non corrisponde", false);
		valida(rpt.getCivicoPagatore(),rt.getCivicNumber(), esito, "CivicNumberDebtor non corrisponde", false);
		valida(rpt.getEMailPagatore(),rt.getEMail(), esito, "EMailDebtor non corrisponde", false);
		if(rpt.getIdentificativoUnivocoPagatore() != null && rt.getUniqueIdentifier() != null) {
			valida(rpt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco(),rt.getUniqueIdentifier().getEntityUniqueIdentifierValue(), esito, "UniqueIdentifierDebtor non corrisponde", true);
		}
		valida(rpt.getIndirizzoPagatore(),rt.getStreetName(), esito, "StreetNameDebtor non corrisponde", false);
		valida(rpt.getLocalitaPagatore(),rt.getCity(), esito, "CityDebtor non corrisponde", false);
		valida(rpt.getNazionePagatore(),rt.getCountry(), esito, "CountryDebtor non corrisponde", false);
		valida(rpt.getProvinciaPagatore(),rt.getStateProvinceRegion(), esito, "StateProvinceDebtor non corrisponde", false);
	}
	
	private static void validaSemanticaSingoloVersamento(CtDatiSingoloVersamentoRPT singoloVersamento, CtTransferPAReceiptV2 singoloPagamento, int pos, EsitoValidazione esito) {

		if(singoloPagamento.getIdTransfer() != pos) {
			esito.addErrore(MessageFormat.format(ERRORE_ID_TRANSFER_NON_CORRISPONDENTE_PER_IL_PAGAMENTO_IN_POSIZIONE_0, pos), false);
		}

		if(singoloPagamento.getTransferAmount().compareTo(BigDecimal.ZERO) == 0) {

		} else if(singoloPagamento.getTransferAmount().compareTo(singoloVersamento.getImportoSingoloVersamento()) != 0) {
			esito.addErrore(MessageFormat.format(ERRORE_IMPORTO_DEL_PAGAMENTO_IN_POSIZIONE_0_1_DIVERSO_DA_QUANTO_RICHIESTO_2, pos, singoloPagamento.getTransferAmount().doubleValue(), singoloVersamento.getImportoSingoloVersamento().doubleValue()), false);
		}
	}
}
