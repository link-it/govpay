/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.StampeBD;
import it.govpay.core.beans.tracciati.LinguaSecondaria;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.InvalidSwitchValueException;
import it.govpay.core.exceptions.PropertyNotFoundException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.core.utils.stampe.AvvisoPagamentoUtils;
import it.govpay.core.utils.stampe.AvvisoPagamentoV2Utils;
import it.govpay.model.Stampa;
import it.govpay.model.Stampa.TIPO;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.JRException;

public class AvvisoPagamento {


	private static final String ERROR_MSG_AGGIORNAMENTO_PDF_AVVISO_DOCUMENTO_FALLITO_0 = "Aggiornamento Pdf Avviso Documento fallito: {0}";
	private static final String ERROR_MSG_CREAZIONE_PDF_AVVISO_DOCUMENTO_FALLITO_0 = "Creazione Pdf Avviso Documento fallito: {0}";
	private static Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamento.class);

	public AvvisoPagamento() {
	}

	public void cancellaAvviso(Versamento versamento) throws GovPayException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			log.debug(MessageFormat.format("Delete Avviso Pagamento per la pendenza [IDA2A: {0} | Id: {1}]", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte()));

			StampeBD avvisiBD = new StampeBD(configWrapper);
			avvisiBD.cancellaAvviso(versamento.getId());
			
			// cancellazione del documento
			if(versamento.getIdDocumento() != null) {
				avvisiBD.cancellaAvvisoDocumento(versamento.getIdDocumento());
			}
			
		} catch (ServiceException e) {
			log.error("Delete Avviso Pagamento fallito", e);
			throw new GovPayException(e);
		} catch (NotFoundException e) {
		}
	}


	public PrintAvvisoDTOResponse printAvvisoVersamento(PrintAvvisoVersamentoDTO printAvviso) throws ServiceException{
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		StampeBD avvisiBD = null;
		try {
			avvisiBD = new StampeBD(configWrapper); 

			Stampa avviso = null;
			Applicazione applicazione = printAvviso.getVersamento().getApplicazione(configWrapper);
			if(printAvviso.isSalvaSuDB()) {
				try {
					log.debug(MessageFormat.format(
							"Lettura PDF Avviso Pagamento Pendenza [IDA2A: {0} | IdPendenza: {1}] Check Esistenza DB...",
							applicazione.getCodApplicazione(), printAvviso.getVersamento().getCodVersamentoEnte()));
					avviso = avvisiBD.getAvvisoVersamento(printAvviso.getVersamento().getId());
					log.debug(MessageFormat.format(
							"Lettura PDF Avviso Pagamento Pendenza [IDA2A: {0} | IdPendenza: {1}] trovato.", applicazione.getCodApplicazione(),
							printAvviso.getVersamento().getCodVersamentoEnte()));
				}catch (NotFoundException e) {
					log.debug(MessageFormat.format(
							"Lettura PDF Avviso Pagamento Pendenza [IDA2A: {0} | IdPendenza: {1}] non trovato.",
							applicazione.getCodApplicazione(), printAvviso.getVersamento().getCodVersamentoEnte()));
				}
			}
			
			// se non c'e' allora vien inserito
			if(avviso == null) {
				try {
					byte[] pdfBytes = getBytesAvvisoVersamento(printAvviso, false);

					avviso = new Stampa();
					avviso.setDataCreazione(new Date());
					avviso.setIdVersamento(printAvviso.getVersamento().getId());
					avviso.setTipo(TIPO.AVVISO);
					avviso.setPdf(pdfBytes);
					if(printAvviso.isSalvaSuDB()) {
						log.debug(MessageFormat.format("Creazione PDF Avviso Pagamento [Dominio: {0} | IUV: {1}] Salvataggio su DB...",	printAvviso.getCodDominio(), printAvviso.getIuv()));
						avvisiBD.insertStampa(avviso);
						log.debug(MessageFormat.format("Creazione PDF Avviso Pagamento [Dominio: {0} | IUV: {1}] Salvataggio su DB completato.", printAvviso.getCodDominio(), printAvviso.getIuv()));
					}
				} catch (UtilsException | JAXBException | IOException | JRException | PropertyNotFoundException | InvalidSwitchValueException e) {
					log.error(MessageFormat.format("Creazione Pdf Avviso Pagamento fallito: {0}", e.getMessage()) , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error(MessageFormat.format("Creazione Pdf Avviso Pagamento fallito: {0}", e.getMessage()) , e);
					throw e;
				}
			} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
				try {
					byte[] pdfBytes = getBytesAvvisoVersamento(printAvviso, true);

					avviso.setDataCreazione(new Date());
					avviso.setPdf(pdfBytes);
					
					if(printAvviso.isSalvaSuDB()) {
						log.debug(MessageFormat.format("Aggiornamento PDF Avviso Pagamento [Dominio: {0} | IUV: {1}] Salvataggio su DB...", printAvviso.getCodDominio(), printAvviso.getIuv()));
						avvisiBD.updatePdfStampa(avviso);
						log.debug(MessageFormat.format("Aggiornamento PDF Avviso Pagamento [Dominio: {0} | IUV: {1}] Salvato.", printAvviso.getCodDominio(), printAvviso.getIuv()));
					}
				} catch (UtilsException | JAXBException | IOException | JRException | PropertyNotFoundException | InvalidSwitchValueException e) {
					log.error(MessageFormat.format("Aggiornamento Pdf Avviso Pagamento fallito: {0}", e.getMessage()) , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error(MessageFormat.format("Aggiornamento Pdf Avviso Pagamento fallito: {0}", e.getMessage()) , e);
					throw e;
				}
			}

			log.debug(MessageFormat.format("Stampa PDF Avviso Pagamento [IDA2A: {0} | IdPendenza: {1}]  Creazione Stampa completata.",	applicazione.getCodApplicazione(), printAvviso.getVersamento().getCodVersamentoEnte()));
			response.setAvviso(avviso);
		}finally {
			if(avvisiBD != null)
				avvisiBD.closeConnection();
		}
		return response;
	}

	private byte[] getBytesAvvisoVersamento(PrintAvvisoVersamentoDTO printAvviso, boolean update) throws ServiceException, JAXBException, IOException, JRException, UtilsException, PropertyNotFoundException, InvalidSwitchValueException {
		String logPrefix = update ? "Aggiornamento" : "Creazione";
		
		log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Lettura properties...", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Lettura properties completata.",	logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
		
		LinguaSecondaria linguaSelezionata = this.getSecondaLingua(printAvviso.getLinguaSecondaria(), printAvviso.getVersamento());
		
		byte[] pdfBytes = null;
		if(linguaSelezionata != null) {
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento Multilingua [Dominio: {1} | IUV: {2}] Creazione input...", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
			it.govpay.stampe.model.v2.AvvisoPagamentoInput input = AvvisoPagamentoV2Utils.fromVersamento(printAvviso, linguaSelezionata);
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento Multilingua [Dominio: {1} | IUV: {2}] Creazione input completata.", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));

			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento Multilingua [Dominio: {1} | IUV: {2}] Generazione pdf...",	logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, input, printAvviso.getCodDominio(), avProperties);
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento Multilingua [Dominio: {1} | IUV: {2}] Generazione pdf completata.", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
		} else {
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Creazione input...", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
			AvvisoPagamentoInput input = AvvisoPagamentoUtils.fromVersamento(printAvviso);
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Creazione input completata.", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));

			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Generazione pdf...", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);
			log.debug(MessageFormat.format("{0} PDF Avviso Pagamento [Dominio: {1} | IUV: {2}] Generazione pdf completata.", logPrefix, printAvviso.getCodDominio(), printAvviso.getIuv()));
		}

		return pdfBytes;
	}

	private LinguaSecondaria getSecondaLingua(LinguaSecondaria linguaSecondaria, Versamento versamento)  {
		LinguaSecondaria linguaSelezionata = null;
		
		if(linguaSecondaria != null) {
			if(linguaSecondaria.equals(LinguaSecondaria.FALSE)) {
				log.debug(MessageFormat.format("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore [{0}], l''avviso verra'' stampato solo in italiano.", linguaSecondaria));
				linguaSelezionata = null;
			} else {
				log.debug(MessageFormat.format("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore [{0}], l''avviso verra'' stampato in formato bilingue.", linguaSecondaria));
				linguaSelezionata = linguaSecondaria;
			}
		} else {
			LinguaSecondaria linguaSecondariaDefault = versamento.getProprietaPendenza() != null ? versamento.getProprietaPendenza().getLinguaSecondaria() : null;
			if(linguaSecondariaDefault != null) {			
				if(linguaSecondariaDefault.equals(LinguaSecondaria.FALSE)) {
					log.debug(MessageFormat.format("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: [{0}], l''avviso verra'' stampato solo in italiano.", linguaSecondariaDefault));
					linguaSelezionata = null;
				} else {
					log.debug(MessageFormat.format("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: [{0}], l''avviso verra'' stampato in formato bilingue.", linguaSecondariaDefault));
					linguaSelezionata = linguaSecondariaDefault;
				}
			} else {
				log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: valore non impostato, l'avviso verra' stampato solo in italiano.");
				linguaSelezionata = null;
			}
		}
		
		if(linguaSelezionata != null) {
			// controllo che la lingua selezionata sia disponibile nel sistema:
			try {
				Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(linguaSelezionata.toString());
				if(labelsLingua == null || labelsLingua.isEmpty()) {
					log.debug(MessageFormat.format("Non sono state definite le label per la lingua secondaria [{0}], l''avviso verra'' stampato solo in italiano.", linguaSelezionata));
					linguaSelezionata = null;
				}
			} catch (UtilsException e) {
				log.warn(MessageFormat.format("Errore durante la lettura delle label per la lingua secondaria [{0}] :", linguaSelezionata), e.getMessage());
				linguaSelezionata = null;
			}
			
		}
		
		
		return linguaSelezionata;
	}

	public PrintAvvisoDTOResponse printAvvisoDocumento(PrintAvvisoDocumentoDTO printAvviso) throws ServiceException, UnprocessableEntityException{ 
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		StampeBD avvisiBD = null;
		try {
			avvisiBD = new StampeBD(configWrapper);

			Stampa avviso = null;

			Applicazione applicazione = printAvviso.getDocumento().getApplicazione(configWrapper); 
			if(printAvviso.isSalvaSuDB()) {
				try {
					log.debug(MessageFormat.format("Lettura PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] Check Esistenza DB...", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
					avviso = avvisiBD.getAvvisoDocumento(printAvviso.getDocumento().getId());
					log.debug(MessageFormat.format("Lettura PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] trovato].", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
				}catch (NotFoundException e) {
					log.debug(MessageFormat.format("Lettura PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] non trovato].", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
				}
			}
			// se non c'e' allora vien inserito
			Dominio dominio = printAvviso.getDocumento().getDominio(configWrapper);
			if(avviso == null) {
				try {
					byte[] pdfBytes = getBytesAvvisoDocumento(printAvviso, applicazione, dominio, false);

					avviso = new Stampa();
					avviso.setDataCreazione(new Date());
					avviso.setIdDocumento(printAvviso.getDocumento().getId());
					avviso.setTipo(TIPO.AVVISO);
					avviso.setPdf(pdfBytes);
					if(printAvviso.isSalvaSuDB()) {
						log.debug(MessageFormat.format("Creazione PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] Salvataggio su DB...", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
						avvisiBD.insertStampa(avviso);
						log.debug(MessageFormat.format("Creazione PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] Salvataggio su DB completato.", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
					}
				} catch (UtilsException | JAXBException | IOException | JRException| PropertyNotFoundException | InvalidSwitchValueException e) {
					log.error(MessageFormat.format(ERROR_MSG_CREAZIONE_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getMessage()), e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error(MessageFormat.format(ERROR_MSG_CREAZIONE_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getMessage()), e);
					throw e;
				} catch (UnprocessableEntityException e) {
					log.error(MessageFormat.format(ERROR_MSG_CREAZIONE_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getDetails()), e);
					throw e;
				}
			} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
				try {
					byte[] pdfBytes = getBytesAvvisoDocumento(printAvviso, applicazione, dominio, false);

					avviso.setDataCreazione(new Date());
					avviso.setPdf(pdfBytes);
					if(printAvviso.isSalvaSuDB()) {
						log.debug(MessageFormat.format("Aggiornamento PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] Salvataggio su DB...", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
						avvisiBD.updatePdfStampa(avviso);
						log.debug(MessageFormat.format("Aggiornamento PDF Avviso Documento [IDA2A: {0} | CodDocumento: {1}] Salvataggio su DB completato.", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
					}
				} catch (UtilsException | JAXBException | IOException | JRException| PropertyNotFoundException | InvalidSwitchValueException e) {
					log.error(MessageFormat.format(ERROR_MSG_AGGIORNAMENTO_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getMessage()) , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error(MessageFormat.format(ERROR_MSG_AGGIORNAMENTO_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getMessage()) , e);
					throw e;
				} catch (UnprocessableEntityException e) {
					log.error(MessageFormat.format(ERROR_MSG_AGGIORNAMENTO_PDF_AVVISO_DOCUMENTO_FALLITO_0, e.getDetails()) , e);
					throw e;
				}
			}

			log.debug(MessageFormat.format("Stampa PDF Avviso Pagamento Documento [IDA2A: {0} | CodDocumento: {1}] Creazione Stampa completata.", applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
			response.setAvviso(avviso);
		}finally {
			if(avvisiBD != null)
				avvisiBD.closeConnection();
		}
		return response;
	}

	private byte[] getBytesAvvisoDocumento(PrintAvvisoDocumentoDTO printAvviso, Applicazione applicazione, Dominio dominio, boolean update) throws ServiceException, UnprocessableEntityException, JAXBException, IOException, JRException,	UtilsException, PropertyNotFoundException, InvalidSwitchValueException {
		String logPrefix = update ? "Aggiornamento" : "Creazione";
		
		log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Lettura properties...", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Lettura properties completata.", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Documento documento = printAvviso.getDocumento();
		List<String> numeriAvviso = printAvviso.getNumeriAvviso();
		String codApplicazione = applicazione.getCodApplicazione();
		
		// Le pendenze che non sono rate (dovrebbe esserceni al piu' una, ma non si sa mai...) 
		// vanno su una sola pagina
		List<Versamento> versamenti = documento.getVersamentiPagabili(configWrapper, numeriAvviso);
		
		if(versamenti == null || versamenti.isEmpty()) {
			if(numeriAvviso == null || numeriAvviso.isEmpty()) {
				throw new UnprocessableEntityException(MessageFormat.format("Non sono state trovate pendenze da includere nella stampa del documento [IDA2A: {0} | CodDocumento: {1}].", codApplicazione, documento.getCodDocumento()));
			} else {
				throw new UnprocessableEntityException(MessageFormat.format("I numeri avviso indicati [{0}] non individuano alcuna pendenza valida da includere nella stampa del documento [IDA2A: {1} | CodDocumento: {2}].", StringUtils.join(numeriAvviso,","), codApplicazione, documento.getCodDocumento()));
			}
		}

		// Le rate vanno ordinate, per numero rata o per soglia
		Collections.sort(versamenti, new Comparator<Versamento>() {
			@Override
			public int compare(Versamento v1, Versamento v2) {
				if(v1.getGiorniSoglia() == null && (v1.getNumeroRata() == null || v1.getNumeroRata() == 0))
					return -1;
				if(v2.getGiorniSoglia() == null && (v2.getNumeroRata() == null || v2.getNumeroRata() == 0))
					return 1;
				if(v1.getNumeroRata() != null)
					return v1.getNumeroRata().compareTo(v2.getNumeroRata());
				if(v1.getGiorniSoglia() != null)
					if(v1.getGiorniSoglia().compareTo(v2.getGiorniSoglia())==0)
						if(v1.getTipoSoglia().equals(TipoSogliaVersamento.ENTRO))
							return -1;
						else
							return 1;
					else
						return v1.getGiorniSoglia().compareTo(v2.getGiorniSoglia());


				//Qua non ci arrivo mai
				log.warn("Compare di versamenti non corretto. Una casistica non valutata correttamente?");
				return 0;
			}
		});
		
		LinguaSecondaria linguaSelezionata = this.getSecondaLingua(printAvviso.getLinguaSecondaria(), versamenti.get(0));
		
		byte[] pdfBytes = null;
		if(linguaSelezionata != null) {
			log.debug(MessageFormat.format("{0} PDF Avviso Documento Multilingua [IDA2A: {1} | CodDocumento: {2}] Creazione input...", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
			it.govpay.stampe.model.v2.AvvisoPagamentoInput input = AvvisoPagamentoV2Utils.fromDocumento(printAvviso, versamenti, linguaSelezionata, log);
			log.debug(MessageFormat.format("{0} PDF Avviso Documento Multilingua [IDA2A: {1} | CodDocumento: {2}] Creazione input completata.", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));

			log.debug(MessageFormat.format("{0} PDF Avviso Documento Multilingua [IDA2A: {1} | CodDocumento: {2}] Generazione pdf...", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, input, dominio.getCodDominio(), avProperties);
			log.debug(MessageFormat.format("{0} PDF Avviso Documento Multilingua [IDA2A: {1} | CodDocumento: {2}] Generazione pdf completata.", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
		} else {
			log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Creazione input...", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
			AvvisoPagamentoInput input = AvvisoPagamentoUtils.fromDocumento(printAvviso, versamenti, log);
			log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Creazione input completata.", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));

			log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Generazione pdf...", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, dominio.getCodDominio(), avProperties);
			log.debug(MessageFormat.format("{0} PDF Avviso Documento [IDA2A: {1} | CodDocumento: {2}] Generazione pdf completata.", logPrefix, applicazione.getCodApplicazione(), printAvviso.getDocumento().getCodDocumento()));
		}
		
		return pdfBytes;
	}

	
	
	
}
