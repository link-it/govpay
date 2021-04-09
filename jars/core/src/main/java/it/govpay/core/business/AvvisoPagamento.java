package it.govpay.core.business;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.StampeBD;
import it.govpay.core.beans.tracciati.LinguaSecondaria;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Stampa;
import it.govpay.model.Stampa.TIPO;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.model.Etichette;
import it.govpay.stampe.model.PaginaAvvisoDoppia;
import it.govpay.stampe.model.PaginaAvvisoSingola;
import it.govpay.stampe.model.PaginaAvvisoTripla;
import it.govpay.stampe.model.PagineAvviso;
import it.govpay.stampe.model.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import net.sf.jasperreports.engine.JRException;

public class AvvisoPagamento {


	private SimpleDateFormat sdfDataScadenza = new SimpleDateFormat("dd/MM/yyyy");
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
					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties...");
					AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties completata.");
					
					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input...");
					AvvisoPagamentoInput input = this.fromVersamento(printAvviso.getVersamento(), printAvviso.getLinguaSecondaria());
					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input completata.");

					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf...");
					byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);
					log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf completata.");

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
					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties...");
					AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Lettura properties completata.");
					
					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input...");
					AvvisoPagamentoInput input = this.fromVersamento(printAvviso.getVersamento(), printAvviso.getLinguaSecondaria());
					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Creazione input completata.");

					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf... ");
					byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);
					log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] Generazione pdf completata.");

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

	private LinguaSecondaria getSecondaLingua(LinguaSecondaria linguaSecondaria, Versamento versamento) {
		if(linguaSecondaria != null) {
			if(linguaSecondaria.equals(LinguaSecondaria.FALSE)) {
				log.debug("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore ["+linguaSecondaria+"], l'avviso verra' stampato solo in italiano.");
				return null;
			} else {
				log.debug("Lingua secondaria decisa sovrascrivendo il valore di default impostato nella pendenza: ricevuto valore ["+linguaSecondaria+"], l'avviso verra' stampato in formato bilingue.");
				return linguaSecondaria;
			}
		} else {
			LinguaSecondaria linguaSecondariaDefault = versamento.getProprietaPendenza() != null ? versamento.getProprietaPendenza().getLinguaSecondaria() : null;
			if(linguaSecondariaDefault != null) {			
				if(linguaSecondariaDefault.equals(LinguaSecondaria.FALSE)) {
					log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: ["+linguaSecondariaDefault+"], l'avviso verra' stampato solo in italiano.");
					return null;
				} else {
					log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: ["+linguaSecondariaDefault+"], l'avviso verra' stampato in formato bilingue.");
					return linguaSecondariaDefault;
				}
			} else {
				log.debug("Lingua secondaria decisa utilizzando il valore di default impostato nella pendenza: valore non impostato, l'avviso verra' stampato solo in italiano.");
				return null;
			}
		}
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
					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties...");
					AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties completata.");
					
					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input...");
					AvvisoPagamentoInput input = this.fromDocumento(printAvviso.getDocumento(), applicazione.getCodApplicazione(), printAvviso.getNumeriAvviso(), printAvviso.getLinguaSecondaria());
					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input completata.");

					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf...");
					byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, dominio.getCodDominio(), avProperties);
					log.debug("Creazione PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf completata.");

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
					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties...");
					AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Lettura properties completata.");
					
					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input...");
					AvvisoPagamentoInput input = this.fromDocumento(printAvviso.getDocumento(), applicazione.getCodApplicazione(), printAvviso.getNumeriAvviso(), printAvviso.getLinguaSecondaria());
					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Creazione input completata.");

					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf...");
					byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, dominio.getCodDominio(), avProperties);
					log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + applicazione.getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] Generazione pdf completata.");

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

	public AvvisoPagamentoInput fromVersamento(it.govpay.bd.model.Versamento versamento, LinguaSecondaria linguaSecondaria) throws ServiceException, UtilsException {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		String causaleVersamento = "";
		if(versamento.getCausaleVersamento() != null) {
			try {
				causaleVersamento = versamento.getCausaleVersamento().getSimple();
				input.setOggettoDelPagamento(causaleVersamento);
			}catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
		
		LinguaSecondaria secondaLinguaScelta = getSecondaLingua(linguaSecondaria, versamento);
		
		it.govpay.stampe.model.AvvisoPagamentoInput.Etichette etichettes = new it.govpay.stampe.model.AvvisoPagamentoInput.Etichette();
		etichettes.setItaliano(getEtichetteItaliano());
		etichettes.setTraduzione(getEtichetteTraduzione(secondaLinguaScelta));
		input.setEtichette(etichettes);

		this.impostaAnagraficaEnteCreditore(versamento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
		this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);

		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(getRata(versamento, input, secondaLinguaScelta));
		
		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());

		input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);

		return input;
	} 

	public AvvisoPagamentoInput fromDocumento(Documento documento, String codApplicazione, List<String> numeriAvviso, LinguaSecondaria linguaSecondaria) throws ServiceException, UnprocessableEntityException, UtilsException { 
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		input.setOggettoDelPagamento(documento.getDescrizione());

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
		
		LinguaSecondaria secondaLinguaScelta = getSecondaLingua(linguaSecondaria, versamenti.get(0)); 
		
		it.govpay.stampe.model.AvvisoPagamentoInput.Etichette etichettes = new it.govpay.stampe.model.AvvisoPagamentoInput.Etichette();
		etichettes.setItaliano(getEtichetteItaliano());
		etichettes.setTraduzione(getEtichetteTraduzione(secondaLinguaScelta));
		input.setEtichette(etichettes);

		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());

		// pagina principale
		while(versamenti.size() > 0 && versamenti.get(0).getNumeroRata() == null && versamenti.get(0).getTipoSoglia() == null) {
			Versamento versamento = versamenti.remove(0);
			this.impostaAnagraficaEnteCreditore(documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input, secondaLinguaScelta));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		// calcolo il numero delle rate
		int numeroRate = 0;
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() != null) {
				numeroRate ++;
			}
		}
		
		if(numeroRate > 0) {
			input.getEtichette().getItaliano().setNotaPrimaRata(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA, numeroRate));
		//	input.getEtichette().getItaliano().setNotaRataUnica(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
			
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setNotaPrimaRata(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA, numeroRate));
			//	input.getEtichette().getTraduzione().setNotaRataUnica(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
			}
		}
		
		// 2 rate per pagina
		while(versamenti.size() > 1 && versamenti.size()%3 != 0) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			this.impostaAnagraficaEnteCreditore(documento.getDominio(configWrapper), v2.getUo(configWrapper), input);
			this.impostaAnagraficaDebitore(v2.getAnagraficaDebitore(), input);
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			RataAvviso rataSx = getRata(v1, input, secondaLinguaScelta);
			RataAvviso rataDx = getRata(v2, input, secondaLinguaScelta);
			
			if(v1.getNumeroRata() != null && v2.getNumeroRata() != null) {
				// Titolo della pagina con 2 Rate
				String titoloRateIta = getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ELENCO_RATE_2, v1.getNumeroRata(), v2.getNumeroRata());
				rataSx.setElencoRate(titoloRateIta);
				rataDx.setElencoRate(titoloRateIta);
				if(secondaLinguaScelta != null) {
					String titoloRateSL = getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ELENCO_RATE_2, v1.getNumeroRata(), v2.getNumeroRata());
					rataSx.setElencoRateTra(titoloRateSL);
					rataDx.setElencoRateTra(titoloRateSL);
				}
			}
			
			pagina.getRata().add(rataSx);
			pagina.getRata().add(rataDx);
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		// 3 rate per pagina
		while(versamenti.size() > 1) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			Versamento v3 = versamenti.remove(0);
			this.impostaAnagraficaEnteCreditore(documento.getDominio(configWrapper), v3.getUo(configWrapper), input);
			this.impostaAnagraficaDebitore(v3.getAnagraficaDebitore(), input);
			PaginaAvvisoTripla pagina = new PaginaAvvisoTripla();
			
			
			RataAvviso rataSx = getRata(v1, input, secondaLinguaScelta);
			RataAvviso rataCentro = getRata(v2, input, secondaLinguaScelta);
			RataAvviso rataDx = getRata(v3, input, secondaLinguaScelta);
			
			if(v1.getNumeroRata() != null && v2.getNumeroRata() != null && v2.getNumeroRata() != null) {
				// Titolo della pagina con 3 Rate
				String titoloRateIta = getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ELENCO_RATE_3, v1.getNumeroRata(), v2.getNumeroRata(), v3.getNumeroRata());
				rataSx.setElencoRate(titoloRateIta);
				rataCentro.setElencoRate(titoloRateIta);
				rataDx.setElencoRate(titoloRateIta);
				if(secondaLinguaScelta != null) {
					String titoloRateSL = getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ELENCO_RATE_3, v1.getNumeroRata(), v2.getNumeroRata(), v3.getNumeroRata());
					rataSx.setElencoRateTra(titoloRateSL);
					rataCentro.setElencoRateTra(titoloRateSL);
					rataDx.setElencoRateTra(titoloRateSL);
				}
			}

			pagina.getRata().add(rataSx);
			pagina.getRata().add(rataCentro);
			pagina.getRata().add(rataDx);
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		// rata unica?
		if(versamenti.size() == 1) {
			Versamento versamento = versamenti.remove(0);
			this.impostaAnagraficaEnteCreditore(documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input, secondaLinguaScelta));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		return input;
	}

	private RataAvviso getRata(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input, LinguaSecondaria secondaLinguaScelta) throws ServiceException, UtilsException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RataAvviso rata = new RataAvviso();
		
		rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_RATA_UNICA_ENTRO_IL));
		if(secondaLinguaScelta != null)
			rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_RATA_UNICA_ENTRO_IL));
		
		
		if(versamento.getNumeroRata() != null) {
			rata.setNumeroRata(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NUMERO_RATA, versamento.getNumeroRata()));
			if(secondaLinguaScelta != null)
				rata.setNumeroRataTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NUMERO_RATA, versamento.getNumeroRata()));
		}

		if(versamento.getGiorniSoglia() != null && versamento.getTipoSoglia() != null) {
			
			switch (versamento.getTipoSoglia()) {
			case ENTRO:
				rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
				if(secondaLinguaScelta != null)
					rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
				break;
			case OLTRE:
				rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
				if(secondaLinguaScelta != null)
					rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
				break;
			}
			
//			rata.setGiorni(BigInteger.valueOf(versamento.getGiorniSoglia()));
//			rata.setTipo(versamento.getTipoSoglia().toString().toLowerCase());
		}

		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento sv = singoliVersamenti.get(0);

		IbanAccredito postale = null;

		if(sv.getIbanAccredito(configWrapper) != null && sv.getIbanAccredito(configWrapper).isPostale())
			postale = sv.getIbanAccredito(configWrapper);
		else if(sv.getIbanAppoggio(configWrapper) != null && sv.getIbanAppoggio(configWrapper).isPostale())
			postale = sv.getIbanAppoggio(configWrapper);
		
		if(versamento.getNumeroAvviso() != null) {
			// split del numero avviso a gruppi di 4 cifre
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < versamento.getNumeroAvviso().length(); i++) {
				if(sb.length() > 0 && (i % 4 == 0)) {
					sb.append(" ");
				}

				sb.append(versamento.getNumeroAvviso().charAt(i));
			}

			rata.setCodiceAvviso(sb.toString());
		}

		if(postale != null) {
			// ho gia' caricato tutte le label, spengo il pagamento standard
			input.getEtichette().getItaliano().setPagaTerritorio2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_POSTE));
			input.getEtichette().getItaliano().setPagaApp2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_APP_POSTE));
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null) {
				input.getEtichette().getTraduzione().setPagaTerritorio2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_POSTE));
				input.getEtichette().getTraduzione().setPagaApp2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_POSTE));
			}
			
			rata.setDataMatrix(this.creaDataMatrix(versamento.getNumeroAvviso(), this.getNumeroCCDaIban(postale.getCodIban()), 
					versamento.getImportoTotale().doubleValue(),
					input.getCfEnte(),
					input.getCfDestinatario(),
					input.getNomeCognomeDestinatario(),
					input.getOggettoDelPagamento()));
			rata.setNumeroCcPostale(this.getNumeroCCDaIban(postale.getCodIban()));
			if(StringUtils.isBlank(postale.getIntestatario()))
				input.setIntestatarioContoCorrentePostale(input.getEnteCreditore());
			else 
				input.setIntestatarioContoCorrentePostale(postale.getIntestatario());
			rata.setCodiceAvvisoPostale(rata.getCodiceAvviso()); 
			input.setPoste(true);
		} else {
			// ho gia' caricato tutte le label, spengo il pagamento poste
			input.getEtichette().getItaliano().setPagaTerritorio2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_STANDARD));
			input.getEtichette().getItaliano().setPagaApp2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_APP_STANDARD));
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setPagaTerritorio2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_STANDARD));
				input.getEtichette().getTraduzione().setPagaApp2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_STANDARD));
			}
		}

		if(versamento.getImportoTotale() != null)
			rata.setImporto(versamento.getImportoTotale().doubleValue());

		if(versamento.getDataValidita() != null) {
			String dataValidita = this.sdfDataScadenza.format(versamento.getDataValidita());
			rata.setData(dataValidita);
			
			// entro il?
//			rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ENTRO_IL));
//			if(secondaLinguaScelta != null)
//				rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ENTRO_IL));
		}

		it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));
		if(iuvGenerato.getQrCode() != null)
			rata.setQrCode(new String(iuvGenerato.getQrCode()));

		return rata;
	}

	private void impostaAnagraficaEnteCreditore(Dominio dominio, UnitaOperativa uo, AvvisoPagamentoInput input)
			throws ServiceException {

		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDominio = dominio.getAnagrafica();
		
		Anagrafica anagraficaUO = null;
		if(uo!=null)
			anagraficaUO = uo.getAnagrafica();

		input.setEnteCreditore(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		input.setCbill(dominio.getCbill() != null ? dominio.getCbill()  : " ");

		
		if(anagraficaUO != null) {	
			input.setSettoreEnte(anagraficaUO.getArea());
		} else if(anagraficaDominio != null) { 
			input.setSettoreEnte(anagraficaDominio.getArea());
		}
		
		StringBuilder sb = new StringBuilder();

		if(StringUtils.isNotEmpty(anagraficaUO.getUrlSitoWeb())) {
			sb.append(anagraficaUO.getUrlSitoWeb());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getUrlSitoWeb())) {
			sb.append(anagraficaDominio.getUrlSitoWeb());
		}
		
		if(sb.length() > 0)
			sb.append("<br/>");
		
		boolean line2=false;
		if(StringUtils.isNotEmpty(anagraficaUO.getTelefono())){
			sb.append("Tel: ").append(anagraficaUO.getTelefono());
			sb.append(" - ");
			line2=true;
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getTelefono())) {
			sb.append("Tel: ").append(anagraficaDominio.getTelefono());
			sb.append(" - ");
			line2=true;
		} 
		
		if(StringUtils.isNotEmpty(anagraficaUO.getFax())){
			sb.append("Fax: ").append(anagraficaUO.getFax());
			line2=true;
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getFax())) {
			sb.append("Fax: ").append(anagraficaDominio.getFax());
			line2=true;
		}
		
		if(line2) sb.append("<br/>");
		
		if(StringUtils.isNotEmpty(anagraficaUO.getPec())) {
			sb.append("pec: ").append(anagraficaUO.getPec());
		} else if(StringUtils.isNotEmpty(anagraficaUO.getEmail())){
			sb.append("email: ").append(anagraficaUO.getEmail());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getPec())) {
			sb.append("pec: ").append(anagraficaDominio.getPec());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getEmail())){
			sb.append("email: ").append(anagraficaDominio.getEmail());
		}

		input.setAutorizzazione(dominio.getAutStampaPoste());
		input.setInfoEnte(sb.toString());
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));
		return;
	}

	private void impostaAnagraficaDebitore(Anagrafica anagraficaDebitore, AvvisoPagamentoInput input) {
		if(anagraficaDebitore != null) {
			String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
			String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
			String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
			String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
			String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoCivicoDebitore = StringUtils.isNotEmpty(indirizzoDebitore) ? indirizzoDebitore + " " + civicoDebitore : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCittaDebitore = StringUtils.isNotEmpty(localitaDebitore) ? (capDebitore + " " + localitaDebitore + provinciaDebitore) : "";

			// Inserisco la virgola se la prima riga non e' vuota
			String indirizzoDestinatario = StringUtils.isNotEmpty(indirizzoCivicoDebitore) ? indirizzoCivicoDebitore + "," : "";
			input.setNomeCognomeDestinatario(anagraficaDebitore.getRagioneSociale());
			input.setCfDestinatario(anagraficaDebitore.getCodUnivoco());

			if(indirizzoDestinatario.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoDestinatario1(indirizzoDestinatario);
			}else {
				input.setIndirizzoDestinatario1(indirizzoDestinatario);
			}

			if(capCittaDebitore.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoDestinatario2(capCittaDebitore);
			}else {
				input.setIndirizzoDestinatario2(capCittaDebitore);
			}
		}
	}

	public String splitString(String start) {
		if(start == null || start.length() <= 4)
			return start;

		int length = start.length();
		int bonusSpace = length / 4;
		int charCount = 0;
		int iteration = 1;
		char [] tmp = new char[length + bonusSpace];

		for (int i = length -1; i >= 0; i --) {
			char c = start.charAt(i);
			tmp[charCount ++] = c;

			if(iteration % 4 == 0) {
				tmp[charCount ++] = ' ';
			}

			iteration ++;
		}
		if(length % 4 == 0)
			charCount --;

		String toRet = new String(tmp, 0, charCount); 
		toRet = StringUtils.reverse(toRet);

		return toRet;
	}


	private String creaDataMatrix(String numeroAvviso, String numeroCC, double importo, String codDominio, String cfDebitore, String denominazioneDebitore, String causale) {

		String importoInCentesimi = getImportoInCentesimi(importo);
		String codeLine = createCodeLine(numeroAvviso, numeroCC, importoInCentesimi);
		//		log.debug("CodeLine ["+codeLine+"] Lunghezza["+codeLine.length()+"]");
		String cfDebitoreFilled = getCfDebitoreFilled(cfDebitore);
		String denominazioneDebitoreFilled = getDenominazioneDebitoreFilled(denominazioneDebitore);
		String causaleFilled = getCausaleFilled(causale);

		String dataMatrix = MessageFormat.format(AvvisoPagamentoCostanti.PATTERN_DATAMATRIX, codeLine, codDominio, cfDebitoreFilled, denominazioneDebitoreFilled, causaleFilled, AvvisoPagamentoCostanti.FILLER_DATAMATRIX);
		//		log.debug("DataMatrix ["+dataMatrix+"] Lunghezza["+dataMatrix.length()+"]"); 
		return dataMatrix;
	}

	private String createCodeLine(String numeroAvviso, String numeroCC, String importoInCentesimi) {
		return MessageFormat.format(AvvisoPagamentoCostanti.PATTERN_CODELINE, numeroAvviso,numeroCC,importoInCentesimi);
	}

	private String fillSx(String start, String charToFillWith, int lunghezza) {
		int iterazioni = lunghezza - start.length();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < iterazioni; i++) {
			sb.append(charToFillWith);
		}
		sb.append(start);

		return sb.toString();
	}

	private String fillDx(String start, String charToFillWith, int lunghezza) {
		int iterazioni = lunghezza - start.length();

		StringBuilder sb = new StringBuilder();

		sb.append(start);
		for (int i = 0; i < iterazioni; i++) {
			sb.append(charToFillWith);
		}

		return sb.toString();
	}

	private String getNumeroCCDaIban(String iban) {
		return iban.substring(iban.length() - 12, iban.length());
	}

	private String getImportoInCentesimi(double importo) {
		int tmpImporto = (int) (importo  * 100);
		String stringImporto = Integer.toString(tmpImporto);

		if(stringImporto.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO)
			return stringImporto.toUpperCase();

		if(stringImporto.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO) {
			return stringImporto.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO).toUpperCase();
		}


		return fillSx(stringImporto, "0", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO).toUpperCase();
	}

	private String getCfDebitoreFilled(String cfDebitore) {
		if(cfDebitore.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CF_DEBITORE)
			return cfDebitore.toUpperCase();

		if(cfDebitore.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CF_DEBITORE) {
			return cfDebitore.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CF_DEBITORE).toUpperCase();
		}


		return fillDx(cfDebitore, " ", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CF_DEBITORE).toUpperCase();
	}

	/***
	 * numero caratteri denominazione debitore 40
	 * @param denominazioneDebitore
	 * @return
	 */
	private String getDenominazioneDebitoreFilled(String denominazioneDebitore) {
		if(denominazioneDebitore.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_ANAGRAFICA_DEBITORE)
			return denominazioneDebitore.toUpperCase();

		if(denominazioneDebitore.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_ANAGRAFICA_DEBITORE) {
			return denominazioneDebitore.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_ANAGRAFICA_DEBITORE).toUpperCase();
		}


		return fillDx(denominazioneDebitore, " ", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_ANAGRAFICA_DEBITORE).toUpperCase();
	}

	/**
	 * numero caratteri del campo causale 110
	 * @param causale
	 * @return
	 */
	private String getCausaleFilled(String causale) {
		if(causale.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE)
			return causale.toUpperCase();

		if(causale.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE) {
			return causale.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE).toUpperCase();
		}


		return fillDx(causale, " ", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE).toUpperCase();
	}
	
	private String getLabel(String lingua, String nomeLabel, Object ... parameter) throws UtilsException {
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(lingua);
		
		String propertyValue = labelsLingua.getProperty(nomeLabel);
		
		if(parameter != null && parameter.length > 0) {
			return MessageFormat.format(propertyValue, parameter);
		}
		
		return propertyValue;
	}
	
	private Etichette getEtichetteItaliano() throws UtilsException { 
		Etichette etichette = new Etichette();
		
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(LabelAvvisiProperties.DEFAULT_PROPS);
				
		etichette.setAvvisoPagamento(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_AVVISO_PAGAMENTO));
		etichette.setCanali(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CANALI));
		etichette.setCodiceAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_AVVISO));
		etichette.setCodiceCbill(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_CBILL));
		etichette.setCodiceFiscaleEnte(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_FISCALE_ENTE));
		etichette.setCome(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_COME));
		etichette.setDescrizione(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESCRIZIONE));
		etichette.setDestinatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO));
		etichette.setDestinatarioAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO_AVVISO));
		etichette.setEnteCreditore(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTE_CREDITORE));
		etichette.setEntro(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTRO_IL));
		etichette.setImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_IMPORTO));
		etichette.setIntestatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_INTESTATARIO));
		etichette.setNota(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA));
		etichette.setNotaImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		//etichette.setNotaPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA));
		etichette.setNotaRataUnica(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
		etichette.setOggetto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_OGGETTO));
		etichette.setPagaApp(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_APP));
		etichette.setPagaTerritorio(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_TERRITORIO));
		etichette.setPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PRIMA_RATA));
		etichette.setQuantoQuando(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_QUANTO_QUANDO));
		etichette.setTipo(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_TIPO));
		
		return etichette;
	}
	
	private Etichette getEtichetteTraduzione(LinguaSecondaria linguaSecondaria) throws UtilsException {
		if(linguaSecondaria == null) {
			return null;
		}
		
		Etichette etichette = new Etichette();
		
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(linguaSecondaria.toString());
				
		etichette.setAvvisoPagamento(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_AVVISO_PAGAMENTO));
		etichette.setCanali(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CANALI));
		etichette.setCodiceAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_AVVISO));
		etichette.setCodiceCbill(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_CBILL));
		etichette.setCodiceFiscaleEnte(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_FISCALE_ENTE));
		etichette.setCome(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_COME));
		etichette.setDescrizione(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESCRIZIONE));
		etichette.setDestinatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO));
		etichette.setDestinatarioAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO_AVVISO));
		etichette.setEnteCreditore(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTE_CREDITORE));
		etichette.setEntro(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTRO_IL));
		etichette.setImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_IMPORTO));
		etichette.setIntestatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_INTESTATARIO));
		etichette.setNota(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA));
		etichette.setNotaImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		//etichette.setNotaPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA));
		etichette.setNotaRataUnica(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
		etichette.setOggetto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_OGGETTO));
		etichette.setPagaApp(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_APP));
		etichette.setPagaTerritorio(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_TERRITORIO));
		etichette.setPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PRIMA_RATA));
		etichette.setQuantoQuando(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_QUANTO_QUANDO));
		etichette.setTipo(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_TIPO));
		
		return etichette;
	}
}
