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

import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.RtUtils.EsitoValidazione;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;

public class CtReceiptUtils  extends NdpValidationUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(RtUtils.class);

	public static EsitoValidazione validaSemantica(PaGetPaymentRes ctRpt, PaSendRTReq ctRt) {
		CtPaymentPA ctPaymentPA = ctRpt.getData();
		CtReceipt ctReceipt = ctRt.getReceipt();

		EsitoValidazione esito = new RtUtils().new EsitoValidazione();
		valida(ctPaymentPA.getCreditorReferenceId(), ctReceipt.getCreditorReferenceId(), esito, "CreditorReferenceId non corrisponde", true); // Identificativo di correlazione dei due messaggi lo IUV???

		// validaSemantica(ctRpt.getDominio(), ctRt.getDominio(), esito); sostituito con le due istruzioni seguenti ma non ci sono i dati di dominio e stazione nell'oggetto rpt

		//		valida(ctPaymentPA.getIdentificativoDominio(),ctRt.getIdPA(), esito, "IdentificativoDominio non corrisponde", true);
		//		valida(ctPaymentPA.getIdentificativoStazioneRichiedente(),ctRt.getIdStation(), esito, "IdentificativoStazioneRichiedente non corrisponde", false);

		//		validaSemantica(ctRpt.getEnteBeneficiario(), ctRt.getEnteBeneficiario(), esito);
		validaSemantica(ctPaymentPA.getDebtor(), ctReceipt.getDebtor(), esito);
		//		validaSemantica(ctRpt.getSoggettoVersante(), ctRt.getSoggettoVersante(), esito);


		StOutcome ctRecepitOutcome = ctReceipt.getOutcome(); // esito pagamento ha solo due valori OK/KO
		switch (ctRecepitOutcome) {
		case OK:
			if(ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + ctRecepitOutcome.name(), true);
				return esito;
			}
			break;
		case KO:
			if(ctReceipt.getTransferList().getTransfer().size() != 0 && ctReceipt.getTransferList().getTransfer().size() != ctPaymentPA.getTransferList().getTransfer().size()) {
				esito.addErrore("Numero di pagamenti diverso dal numero di versamenti per una ricevuta di tipo " + ctRecepitOutcome.name(), true);
				return esito;
			}
			break;
		}

		BigDecimal importoTotaleCalcolato = BigDecimal.ZERO;

		for (int i = 0; i < ctPaymentPA.getTransferList().getTransfer().size(); i++) {

			CtTransferPA singoloVersamento = ctPaymentPA.getTransferList().getTransfer().get(i);
			CtTransferPA singoloPagamento = null; 
			if(ctReceipt.getTransferList().getTransfer().size() != 0) {
				singoloPagamento = ctReceipt.getTransferList().getTransfer().get(i);
				validaSemanticaSingoloVersamento(singoloVersamento, singoloPagamento, (i+1), esito);
				importoTotaleCalcolato = importoTotaleCalcolato.add(singoloPagamento.getTransferAmount());
			}
		}

		if (importoTotaleCalcolato.compareTo(ctReceipt.getPaymentAmount()) != 0)
			esito.addErrore("ImportoTotalePagato [" + ctReceipt.getPaymentAmount().doubleValue() + "] non corrisponde alla somma dei SingoliImportiPagati [" + importoTotaleCalcolato.doubleValue() + "]", true);
		if (ctRecepitOutcome.equals(StOutcome.KO) && ctReceipt.getPaymentAmount().compareTo(BigDecimal.ZERO) != 0)
			esito.addErrore("ImportoTotalePagato [" + ctReceipt.getPaymentAmount().doubleValue() + "] diverso da 0 per un pagamento con esito 'KO'.", true);
		if (ctRecepitOutcome.equals(StOutcome.OK) && ctReceipt.getPaymentAmount().compareTo(ctPaymentPA.getPaymentAmount()) != 0)
			esito.addErrore("Importo totale del pagamento [" + ctReceipt.getPaymentAmount().doubleValue() + "] diverso da quanto richiesto [" + ctPaymentPA.getPaymentAmount().doubleValue() + "]", false);

		return esito;
	}

	private static void validaSemanticaSingoloVersamento(CtTransferPA singoloVersamento, CtTransferPA singoloPagamento, int pos, EsitoValidazione esito) {

		if(singoloPagamento.getIdTransfer() != singoloVersamento.getIdTransfer()) {
			esito.addErrore("IdTransfer non corrispondente per il pagamento in posizione ["+pos+"]", false);
		}
		//		validaCausaleVersamento(singoloVersamento.getCausaleVersamento(), singoloPagamento.getCausaleVersamento(), esito, "CausaleVersamento non corrisponde", true); ??? causale
		valida(singoloVersamento.getTransferCategory(), singoloPagamento.getTransferCategory(), esito, "TransferCategory non corrisponde", false);

		if(singoloPagamento.getTransferAmount().compareTo(BigDecimal.ZERO) == 0) {
			//			if(singoloPagamento.getEsitoSingoloPagamento() == null || singoloPagamento.getEsitoSingoloPagamento().isEmpty()) {
			//				esito.addErrore("EsitoSingoloPagamento obbligatorio per pagamenti non eseguiti", false);
			//			}
			//			if(!singoloPagamento.getIdentificativoUnivocoRiscossione().equals("n/a")) {
			//				esito.addErrore("IdentificativoUnivocoRiscossione deve essere n/a per pagamenti non eseguiti.", false);
			//			}
		} else if(singoloPagamento.getTransferAmount().compareTo(singoloVersamento.getTransferAmount()) != 0) {
			esito.addErrore("Importo del pagamento in posizione "+pos+" [" + singoloPagamento.getTransferAmount().doubleValue() + "] diverso da quanto richiesto [" + singoloVersamento.getTransferAmount().doubleValue() + "]", false);
		}
	}

	public static void validaSemantica(CtSubject rpt, CtSubject rt, EsitoValidazione esito) {
		valida(rpt.getFullName(),rt.getFullName(), esito, "FullNameDebtor non corrisponde", false);
		valida(rpt.getPostalCode(),rt.getPostalCode(), esito, "PostalCodeDebtor non corrisponde", false);
		valida(rpt.getCivicNumber(),rt.getCivicNumber(), esito, "CivicNumberDebtor non corrisponde", false);
		valida(rpt.getEMail(),rt.getEMail(), esito, "EMailDebtor non corrisponde", false);
		valida(rpt.getUniqueIdentifier().getEntityUniqueIdentifierValue(),rt.getUniqueIdentifier().getEntityUniqueIdentifierValue(), esito, "UniqueIdentifierDebtor non corrisponde", true);
		valida(rpt.getStreetName(),rt.getStreetName(), esito, "StreetNameDebtor non corrisponde", false);
		valida(rpt.getCity(),rt.getCity(), esito, "CityDebtor non corrisponde", false);
		valida(rpt.getCountry(),rt.getCountry(), esito, "CountryDebtor non corrisponde", false);
		valida(rpt.getStateProvinceRegion(),rt.getStateProvinceRegion(), esito, "StateProvinceDebtor non corrisponde", false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, PaSendRTReq ctRt, boolean recupero) throws ServiceException, NdpException, UtilsException, GovPayException {
		return acquisisciRT(codDominio, iuv, ctRt, recupero, false);
	}

	public static Rpt acquisisciRT(String codDominio, String iuv, PaSendRTReq ctRt, boolean recupero, boolean acquisizioneDaCruscotto) throws ServiceException, NdpException, UtilsException, GovPayException {

		RptBD rptBD = null; 
		try {
			IContext ctx = ContextThreadLocal.get();
			BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
			GpContext appContext = (GpContext) ctx.getApplicationContext();

			rptBD = new RptBD(configWrapper);

			rptBD.setupConnection(configWrapper.getTransactionID());

			rptBD.setAtomica(false);

			rptBD.setAutoCommit(false);

			VersamentiBD versamentiBD = new VersamentiBD(rptBD);

			versamentiBD.setAtomica(false);

			Rpt rpt = null;
			try { 
				rpt = rptBD.getRpt(codDominio, iuv, ModelloPagamento.ATTIVATO_PRESSO_PSP, it.govpay.model.Rpt.Versione.SANP_240, true);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, e.getMessage(), codDominio);
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
				rpt = rptBD.getRpt(codDominio, iuv, ModelloPagamento.ATTIVATO_PRESSO_PSP, it.govpay.model.Rpt.Versione.SANP_240, true);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, e.getMessage(), codDominio);
			}

			if(!acquisizioneDaCruscotto) {
				if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
					throw new NdpException(FaultPa.PAA_RECEIPT_DUPLICATA, "CtReceipt già acquisita in data " + rpt.getDataMsgRicevuta(), rpt.getCodDominio());
				}
			}

			PaGetPaymentRes ctRpt = null; 

			// Validazione Semantica
			RtUtils.EsitoValidazione esito = null;
			try {
				ctRpt = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				esito = CtReceiptUtils.validaSemantica(ctRpt, ctRt);
			} catch (JAXBException e) {
				throw e;
			} catch (SAXException e) {
				throw e;
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
					throw new NdpException(FaultPa.PAA_RECEIPT_DUPLICATA, "Aggiornamento di CtReceipt in pagamenti con esito "+rpt.getEsitoPagamento()+" non supportata.", rpt.getCodDominio());
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
				rpt.setXmlRt(JaxbUtils.toByte(ctRt));

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

			CtReceipt ctReceipt = ctRt.getReceipt();


			log.info("Acquisizione RT per un importo di " + ctReceipt.getPaymentAmount());

			StOutcome ctReceiptOutcome = ctReceipt.getOutcome();
			Rpt.EsitoPagamento rptEsito = ctReceiptOutcome.equals(StOutcome.OK) ? Rpt.EsitoPagamento.PAGAMENTO_ESEGUITO : Rpt.EsitoPagamento.PAGAMENTO_NON_ESEGUITO; 

			if(recupero) {
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("codMessaggioRicevuta", ctReceipt.getReceiptId()));
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("importo", ctReceipt.getPaymentAmount().toString()));
				appContext.getTransaction().getLastServer().addGenericProperty(new Property("codEsitoPagamento", rptEsito.toString()));
				ctx.getApplicationLogger().log("rt.rtRecuperoAcquisizione");
			} else {
				appContext.getRequest().addGenericProperty(new Property("codMessaggioRicevuta", ctReceipt.getReceiptId()));
				appContext.getRequest().addGenericProperty(new Property("importo", ctReceipt.getPaymentAmount().toString()));
				appContext.getRequest().addGenericProperty(new Property("codEsitoPagamento", rptEsito.toString()));
				ctx.getApplicationLogger().log("rt.acquisizione");
			}

			// aggiornamento del campo CCP con il valore RecepitID
			rpt.setCcp(ctReceipt.getReceiptId());
			rpt.setCodMsgRicevuta(ctReceipt.getReceiptId());
			rpt.setDataMsgRicevuta(new Date());
			rpt.setEsitoPagamento(rptEsito);
			rpt.setImportoTotalePagato(ctReceipt.getPaymentAmount());
			rpt.setStato(StatoRpt.RT_ACCETTATA_PA);
			rpt.setDescrizioneStato(null);
			rpt.setXmlRt(JaxbUtils.toByte(ctRt));
			rpt.setIdTransazioneRt(ContextThreadLocal.get().getTransactionId());


			rpt.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.G);
			rpt.setIdentificativoAttestante(ctReceipt.getPspFiscalCode()); // oppure ctReceipt.getIDPsp(); ??
			rpt.setDenominazioneAttestante(ctReceipt.getPSPCompanyName());

			rpt.setCodPsp(ctReceipt.getIdPSP());
//			rpt.setCodIntermediarioPsp(codIntermediarioPsp);
			rpt.setCodCanale(ctReceipt.getIdChannel());
			
			// Aggiorno l'RPT con i dati dell'RT
			rptBD.updateRpt(rpt.getId(), rpt);

			Versamento versamento = rpt.getVersamento();

			//			VersamentiBD versamentiBD = new VersamentiBD(rptBD);
			//			versamentiBD.setAtomica(false); // condivisione della connessione

			List<CtTransferPA> datiSingoliPagamenti = ctReceipt.getTransferList().getTransfer();
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
				CtTransferPA ctDatiSingoloPagamentoRT = datiSingoliPagamenti.get(indice);

				// Se non e' stato completato un pagamento, non faccio niente.
				if(ctDatiSingoloPagamentoRT.getTransferAmount().compareTo(BigDecimal.ZERO) == 0)
					continue;

				//pagamentiNote += "[" +(indice+1) + "/" + ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione() + "/" + ctDatiSingoloPagamentoRT.getSingoloImportoPagato() + "]";

				SingoloVersamento singoloVersamento = singoliVersamenti.get(indice);
//				Dominio dominioSingoloVersamento = VersamentoUtils.getDominioSingoloVersamento(singoloVersamento, versamento.getDominio(configWrapper), configWrapper);
				Dominio dominioSingoloVersamento = null;
				try {
					dominioSingoloVersamento = AnagraficaManager.getDominio(configWrapper, ctDatiSingoloPagamentoRT.getFiscalCodePA());
				} catch (NotFoundException e1) {
					dominioSingoloVersamento = versamento.getDominio(configWrapper);
				}

				Pagamento pagamento = null;
				boolean insert = true;
				try {
					pagamento = pagamentiBD.getPagamento(dominioSingoloVersamento.getCodDominio(), iuv, ctReceipt.getReceiptId(), ctDatiSingoloPagamentoRT.getIdTransfer());

					// Pagamento rendicontato precedentemente senza RPT
					// Probabilmente sono stati scambiati i tracciati per sanare la situazione
					// Aggiorno il pagamento associando la RT appena arrivata

					if(pagamento.getIdRpt() != null) {
						//!! Pagamento gia' notificato da un'altra RPT !!
						throw new ServiceException("ERRORE: RT con pagamento gia' presente in sistema ["+dominioSingoloVersamento.getCodDominio()+"/"+iuv+"/"+ctReceipt.getReceiptId()+"]");
					}

			 		Date dataSingoloPagamento = ctReceipt.getPaymentDateTime() != null ? ctReceipt.getPaymentDateTime() : new Date();
					pagamento.setDataPagamento(dataSingoloPagamento); // <!--data esecuzione pagamento da parte dell'utente-->
					pagamento.setRpt(rpt);
					// Se non e' gia' stato incassato, aggiorno lo stato in pagato
					if(!pagamento.getStato().equals(Stato.INCASSATO)) {
						pagamento.setStato(Stato.PAGATO);
						pagamento.setImportoPagato(ctDatiSingoloPagamentoRT.getTransferAmount());
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
						if(dominioSingoloVersamento.isIntermediato()) {
							pagamento.setTipo(TipoPagamento.ENTRATA);
						} else {
							pagamento.setTipo(TipoPagamento.ENTRATA_PA_NON_INTERMEDIATA);
							ctx.getApplicationLogger().log("pagamento.entrataPaNonIntermediata", dominioSingoloVersamento.getCodDominio(), iuv, ctReceipt.getReceiptId(), ctDatiSingoloPagamentoRT.getTransferAmount().toString());
						}
					}
					Date dataSingoloPagamento = ctReceipt.getPaymentDateTime() != null ? ctReceipt.getPaymentDateTime() : new Date();
					pagamento.setDataPagamento(dataSingoloPagamento); // <!--data esecuzione pagamento da parte dell'utente-->
					pagamento.setRpt(rpt);
					pagamento.setSingoloVersamento(singoloVersamento);
					pagamento.setImportoPagato(ctDatiSingoloPagamentoRT.getTransferAmount());
					pagamento.setIur(ctReceipt.getReceiptId());
					pagamento.setCodDominio(dominioSingoloVersamento.getCodDominio());
					pagamento.setIuv(rpt.getIuv());
					pagamento.setIndiceDati(ctDatiSingoloPagamentoRT.getIdTransfer());
					pagamento.setCommissioniPsp(pagamento.getCommissioniPsp());
				} catch (MultipleResultException e) {
					throw new ServiceException("Identificativo pagamento non univoco: [Dominio:"+dominioSingoloVersamento.getCodDominio()+" Iuv:"+iuv+" Iur:"+ctReceipt.getReceiptId()+" Indice:"+ctDatiSingoloPagamentoRT.getIdTransfer()+"]");
				}

				//				if(ctDatiSingoloPagamentoRT.getAllegatoRicevuta() != null) {
				//					pagamento.setTipoAllegato(Pagamento.TipoAllegato.valueOf(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTipoAllegatoRicevuta().toString()));
				//					pagamento.setAllegato(ctDatiSingoloPagamentoRT.getAllegatoRicevuta().getTestoAllegato());
				//				}

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
							ctx.getApplicationLogger().log("pagamento.recuperoRtAcquisizionePagamentoAnomalo", ctReceipt.getReceiptId(), StringUtils.join(anomalie,"\n"));
						else 
							ctx.getApplicationLogger().log("pagamento.acquisizionePagamentoAnomalo", ctReceipt.getReceiptId(), StringUtils.join(anomalie,"\n"));

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
			
			//schedulo l'invio della notifica APPIO
			if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaAbilitato()) {
				log.debug("Creo notifica avvisatura ricevuta tramite App IO..."); 
				NotificaAppIo notificaAppIo = new NotificaAppIo(rpt, versamento, it.govpay.model.NotificaAppIo.TipoNotifica.RICEVUTA, configWrapper);
				log.debug("Creazione notifica avvisatura ricevuta tramite App IO completata.");
				NotificheAppIoBD notificheAppIoBD = new NotificheAppIoBD(versamentiBD);
				notificheAppIoBD.setAtomica(false); // riuso connessione
				notificheAppIoBD.insertNotifica(notificaAppIo);
				log.debug("Inserimento su DB notifica avvisatura ricevuta tramite App IO completata.");
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
