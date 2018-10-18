package it.govpay.core.business;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.pagamento.AvvisiPagamentoBD;
import it.govpay.bd.pagamento.filters.AvvisoPagamentoFilter;
import it.govpay.core.business.model.InserisciAvvisoDTO;
import it.govpay.core.business.model.InserisciAvvisoDTOResponse;
import it.govpay.core.business.model.LeggiAvvisoDTO;
import it.govpay.core.business.model.LeggiAvvisoDTOResponse;
import it.govpay.core.business.model.ListaAvvisiDTO;
import it.govpay.core.business.model.ListaAvvisiDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.model.avvisi.AvvisoPagamentoInput;
import it.govpay.model.avvisi.InfoEnte;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

public class AvvisoPagamento extends BasicBD {


	private SimpleDateFormat sdfDataScadenza = new SimpleDateFormat("dd/MM/yyyy");
	private static Logger log = LoggerWrapperFactory.getLogger(AvvisoPagamento.class);

	public AvvisoPagamento(BasicBD basicBD) {
		super(basicBD);
	}

	public InserisciAvvisoDTOResponse inserisciAvviso(InserisciAvvisoDTO inserisciAvviso) throws InternalException {
		InserisciAvvisoDTOResponse response = new InserisciAvvisoDTOResponse();
		try {
			log.info("Inserimento Avviso Pagamento [Dominio: " + inserisciAvviso.getCodDominio() +" | IUV: " + inserisciAvviso.getIuv() + "]");

			it.govpay.model.avvisi.AvvisoPagamento avviso = new it.govpay.model.avvisi.AvvisoPagamento();
			avviso.setCodDominio(inserisciAvviso.getCodDominio());
			avviso.setDataCreazione(inserisciAvviso.getDataCreazione());
			avviso.setIuv(inserisciAvviso.getIuv());
			avviso.setStato(inserisciAvviso.getStato());

			AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
			avvisiBD.insertAvviso(avviso);

			response.setAvviso(avviso);

			log.info("Avviso Pagamento inserito con id: " + avviso.getId());
			return response;
		} catch (ServiceException e) {
			log.error("Inserimento Avviso Pagamento fallito", e);
			throw new InternalException(e);
		}
	}


	public ListaAvvisiDTOResponse getAvvisi(ListaAvvisiDTO listaAvvisi) throws ServiceException {
		ListaAvvisiDTOResponse response = new ListaAvvisiDTOResponse();

		AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
		AvvisoPagamentoFilter filter = avvisiBD.newFilter();
		filter.setStato(StatoAvviso.DA_STAMPARE);
		filter.setOffset(listaAvvisi.getOffset());
		filter.setLimit(listaAvvisi.getLimit());

		List<it.govpay.model.avvisi.AvvisoPagamento> avvisi = avvisiBD.findAll(filter);
		response.setAvvisi(avvisi);

		return response;
	}


	public LeggiAvvisoDTOResponse getAvviso(LeggiAvvisoDTO leggiAvviso) throws ServiceException {
		LeggiAvvisoDTOResponse response = new LeggiAvvisoDTOResponse();
		try {
			AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);

			it.govpay.model.avvisi.AvvisoPagamento avviso = avvisiBD.getAvviso(leggiAvviso.getCodDominio(), leggiAvviso.getIuv());

			response.setAvviso(avviso);
			return response;
		} catch (NotFoundException e) {
			return null;
		} 
	}

	public PrintAvvisoDTOResponse printAvviso(PrintAvvisoDTO printAvviso) {
		return this.printAvviso(printAvviso, false);
	}

	public PrintAvvisoDTOResponse printAvviso(PrintAvvisoDTO printAvviso, boolean update) {
		PrintAvvisoDTOResponse response = new PrintAvvisoDTOResponse();

		it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento = printAvviso.getAvviso();
		log.info("Creazione PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "]");
		AvvisoPagamentoInput input = printAvviso.getInput();
		AvvisoPagamentoProperties avProperties = AvvisoPagamentoProperties.getInstance();
		try {
			it.govpay.model.avvisi.AvvisoPagamento avvisoPagamentoResponse  = AvvisoPagamentoPdf.getInstance().creaAvviso(log, input, avvisoPagamento, avProperties);
			
			if(update) {
				log.info("Salvataggio PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "] sul db in corso...");
				// aggiornamento della entry sul db
				AvvisiPagamentoBD avvisiBD = new AvvisiPagamentoBD(this);
				avvisoPagamentoResponse.setStato(StatoAvviso.STAMPATO); 
				avvisiBD.updateAvviso(avvisoPagamentoResponse);
				log.info("Salvataggio PDF Avviso Pagamento [Dominio: " + avvisoPagamento.getCodDominio() +" | IUV: " + avvisoPagamento.getIuv() + "] sul db completato.");
			}
			response.setAvviso(avvisoPagamentoResponse);
		} catch (Exception e) {
			log.error("Creazione Pdf Avviso Pagamento fallito", e);
		}

		return response;
	}

	public AvvisoPagamentoInput fromVersamento(it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento, it.govpay.bd.model.Versamento versamento) throws ServiceException {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();

		Dominio dominio = this.impostaAnagraficaEnteCreditore(versamento, input);

		this.impostaAnagraficaDebitore(versamento, input);

		it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(this), versamento.getUo(this).getDominio(this));
		
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(this);
		SingoloVersamento sv = singoliVersamenti.get(0);
		
		String causaleVersamento = "";
		if(versamento.getCausaleVersamento() != null) {
			try {
				causaleVersamento = versamento.getCausaleVersamento().getSimple();
				input.setOggettoDelPagamento(causaleVersamento);
			}catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
		
		IbanAccredito postale = null;
		
		if(sv.getIbanAccredito(this).isPostale())
			postale = sv.getIbanAccredito(this);
		else if(sv.getIbanAppoggio(this) != null && sv.getIbanAppoggio(this).isPostale())
			postale = sv.getIbanAppoggio(this);
		
		
		if(postale != null) {
			input.setDiPoste(AvvisoPagamentoCostanti.DI_POSTE);
			input.setDataMatrix(this.creaDataMatrix(versamento.getNumeroAvviso(), this.getNumeroCCDaIban(postale.getCodIban()), 
					versamento.getImportoTotale().doubleValue(),
					dominio.getCodDominio(),
					input.getCfDestinatario(),
					input.getNomeCognomeDestinatario(),
					causaleVersamento));
			input.setNumeroCcPostale(this.getNumeroCCDaIban(postale.getCodIban()));
			input.setIntestatarioContoCorrentePostale(dominio.getRagioneSociale());
		} else {
			input.setDelTuoEnte(AvvisoPagamentoCostanti.DEL_TUO_ENTE_CREDITORE);
		}
		
		if(versamento.getImportoTotale() != null)
			input.setImporto(versamento.getImportoTotale().doubleValue());
		
		if(versamento.getDataScadenza() != null)
			input.setData(this.sdfDataScadenza.format(versamento.getDataScadenza()));
		
		if(iuvGenerato.getNumeroAvviso() != null) {
			input.setCodiceAvviso(iuvGenerato.getNumeroAvviso());
		}
		
		if(iuvGenerato.getQrCode() != null)
		input.setQrCode(new String(iuvGenerato.getQrCode()));

		return input;
	}

	private Dominio impostaAnagraficaEnteCreditore(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input)
			throws ServiceException {
		Dominio dominio = versamento.getUo(this).getDominio(this);
		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDominio = dominio.getAnagrafica();
		
		input.setEnteCreditore(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		input.setCbill(dominio.getCbill());
		
		InfoEnte infoEnte = new InfoEnte();
		if(anagraficaDominio != null) {
			input.setSettoreEnte(anagraficaDominio.getArea());
			infoEnte.setWeb(anagraficaDominio.getUrlSitoWeb());
			infoEnte.setEmail(anagraficaDominio.getEmail());
			infoEnte.setPec(anagraficaDominio.getPec());
		}

		input.setAutorizzazione(dominio.getAutStampaPoste());
		input.setInfoEnte(infoEnte);
		return dominio;
	}

	private void impostaAnagraficaDebitore(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input) {
		Anagrafica anagraficaDebitore = versamento.getAnagraficaDebitore();
		if(anagraficaDebitore != null) {
			String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
			String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
			String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
			String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
			String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
			String indirizzoCivicoDebitore = indirizzoDebitore + " " + civicoDebitore;
			String capCittaDebitore = capDebitore + " " + localitaDebitore + provinciaDebitore;
			
			String indirizzoDestinatario = indirizzoCivicoDebitore + " " + capCittaDebitore;
			input.setNomeCognomeDestinatario(anagraficaDebitore.getRagioneSociale());
			input.setCfDestinatario(anagraficaDebitore.getCodUnivoco());
			input.setIndirizzoDestinatario(indirizzoDestinatario);
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
		String cfDebitoreFilled = getCfDebitoreFilled(cfDebitore);
		String denominazioneDebitoreFilled = getDenominazioneDebitoreFilled(denominazioneDebitore);
		String causaleFilled = getCausaleFilled(causale);
		
		return MessageFormat.format(AvvisoPagamentoCostanti.PATTERN_DATAMATRIX, codeLine, codDominio, cfDebitoreFilled, denominazioneDebitoreFilled, causaleFilled, AvvisoPagamentoCostanti.FILLER_DATAMATRIX);
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
