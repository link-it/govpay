package it.govpay.core.business;

import java.io.IOException;
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


	private static Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamento.class);

	public AvvisoPagamento() {
	}

	public void cancellaAvviso(Versamento versamento) throws GovPayException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			log.debug("Delete Avviso Pagamento per la pendenza [IDA2A: " + versamento.getApplicazione(configWrapper).getCodApplicazione() 
					+" | Id: " + versamento.getCodVersamentoEnte() + "]");

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
					log.debug("Lettura PDF Avviso Pagamento Pendenza [IDA2A: " + applicazione.getCodApplicazione()	
					+" | IdPendenza: " + printAvviso.getVersamento().getCodVersamentoEnte() + "] Check Esistenza DB...");
					avviso = avvisiBD.getAvvisoVersamento(printAvviso.getVersamento().getId());
					log.debug("Lettura PDF Avviso Pagamento Pendenza [IDA2A: " + applicazione.getCodApplicazione()	
					+" | IdPendenza: " + printAvviso.getVersamento().getCodVersamentoEnte() + "] trovato.");
				}catch (NotFoundException e) {
					log.debug("Lettura PDF Avviso Pagamento Pendenza [IDA2A: " + applicazione.getCodApplicazione()	
					+" | IdPendenza: " + printAvviso.getVersamento().getCodVersamentoEnte() + "] non trovato.");
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
						log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Salvataggio su DB...");
						avvisiBD.insertStampa(avviso);
						log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Salvataggio su DB completato.");
					}
				} catch (UtilsException | JAXBException | IOException | JRException e) {
					log.error("Creazione Pdf Avviso Pagamento fallito: "+ e.getMessage() , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error("Creazione Pdf Avviso Pagamento fallito: "+ e.getMessage() , e);
					throw e;
				}
			} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
				try {
					byte[] pdfBytes = getBytesAvvisoVersamento(printAvviso, true);

					avviso.setDataCreazione(new Date());
					avviso.setPdf(pdfBytes);
					
					if(printAvviso.isSalvaSuDB()) {
						log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Salvataggio su DB...");
						avvisiBD.updatePdfStampa(avviso);
						log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Salvato.");
					}
				} catch (UtilsException | JAXBException | IOException | JRException e) {
					log.error("Aggiornamento Pdf Avviso Pagamento fallito: "+ e.getMessage() , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error("Aggiornamento Pdf Avviso Pagamento fallito: "+ e.getMessage() , e);
					throw e;
				}
			}

			log.debug("Stampa PDF Avviso Pagamento [IDA2A: " + applicazione.getCodApplicazione()	+" | IdPendenza: " + printAvviso.getVersamento().getCodVersamentoEnte() + "]  Creazione Stampa completata.");
			response.setAvviso(avviso);
		}finally {
			if(avvisiBD != null)
				avvisiBD.closeConnection();
		}
		return response;
	}

	private byte[] getBytesAvvisoVersamento(PrintAvvisoVersamentoDTO printAvviso, boolean update) throws ServiceException, JAXBException, IOException, JRException, UtilsException {
		String logPrefix = update ? "Aggiornamento" : "Creazione";
		
		log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties...");
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties completata.");
		
		LinguaSecondaria linguaSelezionata = this.getSecondaLingua(printAvviso.getLinguaSecondaria(), printAvviso.getVersamento());
		
		byte[] pdfBytes = null;
		if(linguaSelezionata != null) {
			log.debug(logPrefix + " PDF Avviso Pagamento Multilingua [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input...");
			it.govpay.stampe.model.v2.AvvisoPagamentoInput input = AvvisoPagamentoV2Utils.fromVersamento(printAvviso, linguaSelezionata);
			log.debug(logPrefix + " PDF Avviso Pagamento Multilingua [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input completata.");

			log.debug(logPrefix + " PDF Avviso Pagamento Multilingua [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf...");
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, input, printAvviso.getCodDominio(), avProperties);
			log.debug(logPrefix + " PDF Avviso Pagamento Multilingua [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf completata.");
		} else {
			log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input...");
			AvvisoPagamentoInput input = AvvisoPagamentoUtils.fromVersamento(printAvviso);
			log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input completata.");

			log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf...");
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);
			log.debug(logPrefix + " PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf completata.");
		}

		return pdfBytes;
	}

	private LinguaSecondaria getSecondaLingua(LinguaSecondaria linguaSecondaria, Versamento versamento)  {
		LinguaSecondaria linguaSelezionata = null;
		
		if(linguaSecondaria != null) {
			if(linguaSecondaria.equals(LinguaSecondaria.FALSE)) {
				log.debug("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore ["+linguaSecondaria+"], l'avviso verra' stampato solo in italiano.");
				linguaSelezionata = null;
			} else {
				log.debug("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore ["+linguaSecondaria+"], l'avviso verra' stampato in formato bilingue.");
				linguaSelezionata = linguaSecondaria;
			}
		} else {
			LinguaSecondaria linguaSecondariaDefault = versamento.getProprietaPendenza() != null ? versamento.getProprietaPendenza().getLinguaSecondaria() : null;
			if(linguaSecondariaDefault != null) {			
				if(linguaSecondariaDefault.equals(LinguaSecondaria.FALSE)) {
					log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: ["+linguaSecondariaDefault+"], l'avviso verra' stampato solo in italiano.");
					linguaSelezionata = null;
				} else {
					log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: ["+linguaSecondariaDefault+"], l'avviso verra' stampato in formato bilingue.");
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
					log.debug("Non sono state definite le label per la lingua secondaria ["+linguaSelezionata+"], l'avviso verra' stampato solo in italiano.");
					linguaSelezionata = null;
				}
			} catch (UtilsException e) {
				log.warn("Errore durante la lettura delle label per la lingua secondaria ["+linguaSelezionata+"] :", e.getMessage());
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
					log.debug("Lettura PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() 
					+" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Check Esistenza DB...");
					avviso = avvisiBD.getAvvisoDocumento(printAvviso.getDocumento().getId());
					log.debug("Lettura PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() 
					+" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] trovato].");
				}catch (NotFoundException e) {
					log.debug("Lettura PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() 
					+" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] non trovato].");
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
						log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Salvataggio su DB...");
						avvisiBD.insertStampa(avviso);
						log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Salvataggio su DB completato.");
					}
				} catch (UtilsException | JAXBException | IOException | JRException e) {
					log.error("Creazione Pdf Avviso Documento fallito: " + e.getMessage(), e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error("Creazione Pdf Avviso Documento fallito: " + e.getMessage(), e);
					throw e;
				} catch (UnprocessableEntityException e) {
					log.error("Creazione Pdf Avviso Documento fallito: " + e.getDetails(), e);
					throw e;
				}
			} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
				try {
					byte[] pdfBytes = getBytesAvvisoDocumento(printAvviso, applicazione, dominio, false);

					avviso.setDataCreazione(new Date());
					avviso.setPdf(pdfBytes);
					if(printAvviso.isSalvaSuDB()) {
						log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Salvataggio su DB...");
						avvisiBD.updatePdfStampa(avviso);
						log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Salvataggio su DB completato.");
					}
				} catch (UtilsException | JAXBException | IOException | JRException e) {
					log.error("Aggiornamento Pdf Avviso Documento fallito: "+ e.getMessage() , e);
					throw new ServiceException(e);
				} catch (ServiceException e) {
					log.error("Aggiornamento Pdf Avviso Documento fallito: "+ e.getMessage() , e);
					throw e;
				} catch (UnprocessableEntityException e) {
					log.error("Aggiornamento Pdf Avviso Documento fallito: "+ e.getDetails() , e);
					throw e;
				}
			}

			log.debug("Stampa PDF Avviso Pagamento Documento [IDA2A: " + applicazione.getCodApplicazione() +" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione Stampa completata.");
			response.setAvviso(avviso);
		}finally {
			if(avvisiBD != null)
				avvisiBD.closeConnection();
		}
		return response;
	}

	private byte[] getBytesAvvisoDocumento(PrintAvvisoDocumentoDTO printAvviso, Applicazione applicazione, Dominio dominio, boolean update) throws ServiceException, UnprocessableEntityException, JAXBException, IOException, JRException,	UtilsException {
		String logPrefix = update ? "Aggiornamento" : "Creazione";
		
		log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties...");
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties completata.");
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Documento documento = printAvviso.getDocumento();
		List<String> numeriAvviso = printAvviso.getNumeriAvviso();
		String codApplicazione = applicazione.getCodApplicazione();
		
		// Le pendenze che non sono rate (dovrebbe esserceni al piu' una, ma non si sa mai...) 
		// vanno su una sola pagina
		List<Versamento> versamenti = documento.getVersamentiPagabili(configWrapper, numeriAvviso);
		
		if(versamenti == null || versamenti.isEmpty()) {
			if(numeriAvviso == null || numeriAvviso.isEmpty()) {
				throw new UnprocessableEntityException("Non sono state trovate pendenze da includere nella stampa del documento [IDA2A: " 
						+ codApplicazione + " | CodDocumento: " + documento.getCodDocumento() + "].");
			} else {
				throw new UnprocessableEntityException("I numeri avviso indicati ["+StringUtils.join(numeriAvviso,",")+"] non individuano alcuna pendenza valida da includere nella stampa del documento [IDA2A: " 
						+ codApplicazione + " | CodDocumento: " + documento.getCodDocumento() + "].");
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
					if(v1.getGiorniSoglia() == v2.getGiorniSoglia())
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
			log.debug(logPrefix + " PDF Avviso Documento Multilingua [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input...");
			it.govpay.stampe.model.v2.AvvisoPagamentoInput input = AvvisoPagamentoV2Utils.fromDocumento(printAvviso, versamenti, linguaSelezionata, log);
			log.debug(logPrefix + " PDF Avviso Documento Multilingua [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input completata.");

			log.debug(logPrefix + " PDF Avviso Documento Multilingua [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf...");
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, input, dominio.getCodDominio(), avProperties);
			log.debug(logPrefix + " PDF Avviso Documento Multilingua [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf completata.");
		} else {
			log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input...");
			AvvisoPagamentoInput input = AvvisoPagamentoUtils.fromDocumento(printAvviso, versamenti, log);
			log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input completata.");

			log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf...");
			pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, dominio.getCodDominio(), avProperties);
			log.debug(logPrefix + " PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf completata.");
		}
		
		return pdfBytes;
	}

	
	
	
}
