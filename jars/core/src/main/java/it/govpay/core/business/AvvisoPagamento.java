package it.govpay.core.business;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.StampeBD;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Stampa;
import it.govpay.model.Stampa.TIPO;
import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.model.PaginaAvvisoDoppia;
import it.govpay.stampe.model.PaginaAvvisoSingola;
import it.govpay.stampe.model.PaginaAvvisoTripla;
import it.govpay.stampe.model.PagineAvviso;
import it.govpay.stampe.model.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

public class AvvisoPagamento extends BasicBD {


	private SimpleDateFormat sdfDataScadenza = new SimpleDateFormat("dd/MM/yyyy");
	private static Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamento.class);

	public AvvisoPagamento(BasicBD basicBD) {
		super(basicBD);
	}

	public void cancellaAvviso(Versamento versamento) throws GovPayException {
		try {
			log.debug("Delete Avviso Pagamento per la pendenza [IDA2A: " + versamento.getApplicazione(this).getCodApplicazione() 
					+" | Id: " + versamento.getCodVersamentoEnte() + "]");

			StampeBD avvisiBD = new StampeBD(this);
			avvisiBD.cancellaAvviso(versamento.getId());
		} catch (ServiceException e) {
			log.error("Delete Avviso Pagamento fallito", e);
			throw new GovPayException(e);
		} catch (NotFoundException e) {
		}
	}


	public PrintAvvisoDTOResponse printAvvisoVersamento(PrintAvvisoVersamentoDTO printAvviso) throws ServiceException{
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();

		StampeBD avvisiBD = new StampeBD(this);
		Stampa avviso = null;
		try {
			log.debug("Lettura PDF Avviso Pagamento Pendenza [IDA2A: " + printAvviso.getVersamento().getApplicazione(this).getCodApplicazione() 
					+" | IdPendenza: " + printAvviso.getVersamento().getCodVersamentoEnte() + "]");
			avviso = avvisiBD.getAvvisoVersamento(printAvviso.getVersamento().getId());
		}catch (NotFoundException e) {
		}

		// se non c'e' allora vien inserito
		if(avviso == null) {
			try {
				log.debug("Creazione PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "]");
				AvvisoPagamentoInput input = this.fromVersamento(printAvviso.getVersamento());
				AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();

				byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);

				avviso = new Stampa();
				avviso.setDataCreazione(new Date());
				avviso.setIdVersamento(printAvviso.getVersamento().getId());
				avviso.setTipo(TIPO.AVVISO);
				avviso.setPdf(pdfBytes);
				avvisiBD.insertStampa(avviso);
				log.debug("Salvataggio PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] sul db completato.");
			} catch (Exception e) {
				log.error("Creazione Pdf Avviso Pagamento fallito", e);
			}
		} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
			try {
				log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "]");
				AvvisoPagamentoInput input = this.fromVersamento(printAvviso.getVersamento());
				AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();

				byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getCodDominio(), avProperties);

				avviso.setDataCreazione(new Date());
				avviso.setPdf(pdfBytes);
				avvisiBD.updatePdfStampa(avviso);
				log.debug("Aggiornamento PDF Avviso Pagamento [Dominio: " + printAvviso.getCodDominio() +" | IUV: " + printAvviso.getIuv() + "] sul db completato.");
			} catch (Exception e) {
				log.error("Aggiornamento Pdf Avviso Pagamento fallito", e);
			}
		}

		response.setAvviso(avviso);
		return response;
	}
	
	public PrintAvvisoDTOResponse printAvvisoDocumento(PrintAvvisoDocumentoDTO printAvviso) throws ServiceException{
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();

		StampeBD avvisiBD = new StampeBD(this);
		Stampa avviso = null;
		try {
			log.debug("Lettura PDF Avviso Pagamento Documento [IDA2A: " + printAvviso.getDocumento().getApplicazione(this).getCodApplicazione() 
					+" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "]");
			avviso = avvisiBD.getAvvisoDocumento(printAvviso.getDocumento().getId());
		}catch (NotFoundException e) {
		}

		// se non c'e' allora vien inserito
		if(avviso == null) {
			try {
				log.debug("Creazione PDF Avviso Documento [IDA2A: " + printAvviso.getDocumento().getApplicazione(this).getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "]");
				
				AvvisoPagamentoInput input = this.fromDocumento(printAvviso.getDocumento());
				AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();

				byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getDocumento().getDominio(this).getCodDominio(), avProperties);

				avviso = new Stampa();
				avviso.setDataCreazione(new Date());
				avviso.setIdDocumento(printAvviso.getDocumento().getId());
				avviso.setTipo(TIPO.AVVISO);
				avviso.setPdf(pdfBytes);
				avvisiBD.insertStampa(avviso);
				log.debug("Salvataggio PDF Avviso Documento [IDA2A: " + printAvviso.getDocumento().getApplicazione(this).getCodApplicazione() +" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] sul db completato.");
			} catch (Exception e) {
				log.error("Creazione Pdf Avviso Documento fallito", e);
			}
		} else if(printAvviso.isUpdate()) { // se ho fatto l'update della pendenza allora viene aggiornato
			try {
				log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + printAvviso.getDocumento().getApplicazione(this).getCodApplicazione() + " | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "]");

				AvvisoPagamentoInput input = this.fromDocumento(printAvviso.getDocumento());
				AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();

				byte[]  pdfBytes = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, printAvviso.getDocumento().getDominio(this).getCodDominio(), avProperties);

				avviso.setDataCreazione(new Date());
				avviso.setPdf(pdfBytes);
				avvisiBD.updatePdfStampa(avviso);
				log.debug("Aggiornamento PDF Avviso Documento [IDA2A: " + printAvviso.getDocumento().getApplicazione(this).getCodApplicazione() +" | CodDocumento: " + printAvviso.getDocumento().getCodDocumento() + "] sul db completato.");
			} catch (Exception e) {
				log.error("Creazione Pdf Avviso Documento fallito", e);
			}
		}

		response.setAvviso(avviso);
		return response;
	}
	
	public AvvisoPagamentoInput fromVersamento(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		
		String causaleVersamento = "";
		if(versamento.getCausaleVersamento() != null) {
			try {
				causaleVersamento = versamento.getCausaleVersamento().getSimple();
				input.setOggettoDelPagamento(causaleVersamento);
			}catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
		
		this.impostaAnagraficaEnteCreditore(versamento.getDominio(this), input);
		this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);

		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(getRata(versamento, input));
		
		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());
		
		input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);

		return input;
	}

	public AvvisoPagamentoInput fromDocumento(Documento documento) throws ServiceException {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		
		input.setOggettoDelPagamento(documento.getDescrizione());
		this.impostaAnagraficaEnteCreditore(documento.getDominio(this), input);
		
		// Le pendenze che non sono rate (dovrebbe esserceni al piu' una, ma non si sa mai...) 
		// vanno su una sola pagina
		List<Versamento> versamenti = documento.getVersamentiPagabili(this);
		
		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());
		
		while(versamenti.size() > 0 && versamenti.get(0).getNumeroRata() == null) {
			Versamento versamento = versamenti.remove(0);
			this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}
		
		while(versamenti.size() > 1 && versamenti.size()%3 != 0) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			this.impostaAnagraficaDebitore(v2.getAnagraficaDebitore(), input);
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			pagina.getRata().add(getRata(v1, input));
			pagina.getRata().add(getRata(v2, input));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}
		
		while(versamenti.size() > 1) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			Versamento v3 = versamenti.remove(0);
			this.impostaAnagraficaDebitore(v3.getAnagraficaDebitore(), input);
			PaginaAvvisoTripla pagina = new PaginaAvvisoTripla();
			pagina.getRata().add(getRata(v1, input));
			pagina.getRata().add(getRata(v2, input));
			pagina.getRata().add(getRata(v3, input));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}
		
		if(versamenti.size() == 1) {
			Versamento versamento = versamenti.remove(0);
			this.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}
		
		return input;
	}
	
	private RataAvviso getRata(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input) throws ServiceException {
		RataAvviso rata = new RataAvviso();
		if(versamento.getNumeroRata() != null)
			rata.setNumeroRata(BigInteger.valueOf(versamento.getNumeroRata()));
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(this);
		SingoloVersamento sv = singoliVersamenti.get(0);

		IbanAccredito postale = null;

		if(sv.getIbanAccredito(this) != null && sv.getIbanAccredito(this).isPostale())
			postale = sv.getIbanAccredito(this);
		else if(sv.getIbanAppoggio(this) != null && sv.getIbanAppoggio(this).isPostale())
			postale = sv.getIbanAppoggio(this);

		if(postale != null) {
			input.setDiPoste(AvvisoPagamentoCostanti.DI_POSTE);
			rata.setDataMatrix(this.creaDataMatrix(versamento.getNumeroAvviso(), this.getNumeroCCDaIban(postale.getCodIban()), 
					versamento.getImportoTotale().doubleValue(),
					input.getCfEnte(),
					input.getCfDestinatario(),
					input.getNomeCognomeDestinatario(),
					input.getOggettoDelPagamento()));
			rata.setNumeroCcPostale(this.getNumeroCCDaIban(postale.getCodIban()));
			input.setIntestatarioContoCorrentePostale(input.getEnteCreditore());
			rata.setCodiceAvvisoPostale(versamento.getNumeroAvviso()); 
		} else {
			input.setDelTuoEnte(AvvisoPagamentoCostanti.DEL_TUO_ENTE_CREDITORE);
		}

		if(versamento.getImportoTotale() != null)
			rata.setImporto(versamento.getImportoTotale().doubleValue());

		if(versamento.getDataValidita() != null)
			rata.setData(this.sdfDataScadenza.format(versamento.getDataValidita()));

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

		it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(this), versamento.getUo(this).getDominio(this));
		if(iuvGenerato.getQrCode() != null)
			rata.setQrCode(new String(iuvGenerato.getQrCode()));
		
		return rata;
	}

	private void impostaAnagraficaEnteCreditore(Dominio dominio, AvvisoPagamentoInput input)
			throws ServiceException {
		
		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDominio = dominio.getAnagrafica();

		input.setEnteCreditore(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		input.setCbill(dominio.getCbill() != null ? dominio.getCbill()  : " ");

		String infoEnte = null;
		if(anagraficaDominio != null) {
			input.setSettoreEnte(anagraficaDominio.getArea());
			StringBuilder sb = new StringBuilder();

			if(StringUtils.isNotEmpty(anagraficaDominio.getUrlSitoWeb())) {
				sb.append("sito web: ").append(anagraficaDominio.getUrlSitoWeb());
			}

			if(StringUtils.isNotEmpty(anagraficaDominio.getEmail())){
				if(sb.length() > 0)
					sb.append("<br/>");

				sb.append("email: ").append(anagraficaDominio.getEmail());
			}

			if(StringUtils.isNotEmpty(anagraficaDominio.getPec())) {
				if(sb.length() > 0)
					sb.append("<br/>");

				sb.append("PEC: ").append(anagraficaDominio.getPec());
			}

			infoEnte = sb.toString();
		}

		input.setAutorizzazione(dominio.getAutStampaPoste());
		input.setInfoEnte(infoEnte);
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
}
