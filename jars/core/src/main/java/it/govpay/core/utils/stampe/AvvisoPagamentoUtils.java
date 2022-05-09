package it.govpay.core.utils.stampe;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.stampe.model.AvvisoPagamentoInput;
import it.govpay.stampe.model.PaginaAvvisoDoppia;
import it.govpay.stampe.model.PaginaAvvisoSingola;
import it.govpay.stampe.model.PaginaAvvisoTripla;
import it.govpay.stampe.model.PagineAvviso;
import it.govpay.stampe.model.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;

public class AvvisoPagamentoUtils {
	
	public static AvvisoPagamentoInput fromVersamento(PrintAvvisoVersamentoDTO printAvviso) throws ServiceException, UtilsException {
		it.govpay.bd.model.Versamento versamento = printAvviso.getVersamento();
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

		AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(versamento, versamento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
		AvvisoPagamentoUtils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);

		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(getRata(versamento, input, printAvviso.getSdfDataScadenza()));

		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());

		input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);

		return input;
	}

	public static AvvisoPagamentoInput fromDocumento(PrintAvvisoDocumentoDTO printAvviso, List<Versamento> versamenti, Logger log) throws ServiceException, UnprocessableEntityException, UtilsException { 
		Documento documento = printAvviso.getDocumento();
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		SimpleDateFormat sdfDataScadenza = printAvviso.getSdfDataScadenza();
		
		input.setOggettoDelPagamento(documento.getDescrizione());

		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());
		
		int numeroViolazioneCDS = 0; 
		
		for (Versamento versamento : versamenti) {
			if(versamento.getTipoSoglia() != null && 
					(versamento.getTipoSoglia().equals(TipoSogliaVersamento.RIDOTTO)
					|| versamento.getTipoSoglia().equals(TipoSogliaVersamento.SCONTATO))) {
				numeroViolazioneCDS ++;
			}
		}
		
		boolean violazioneCDS = numeroViolazioneCDS == versamenti.size();
		
		// caso speciale Violazione CDS
		if(violazioneCDS && versamenti.size() == 2) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			
			AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(v2, documento.getDominio(configWrapper), v2.getUo(configWrapper), input);
			AvvisoPagamentoUtils.impostaAnagraficaDebitore(v2.getAnagraficaDebitore(), input);
			
			RataAvviso rata1 = getRata(v1, input, sdfDataScadenza);
			RataAvviso rata2 = getRata(v2, input, sdfDataScadenza);
			
			if(input.getDiPoste() == null) { // pagina singola
				RataAvviso rataScontato = null, rataRidotto = null ;
				
				if(rata1.getTipo().equals(TipoSogliaVersamento.RIDOTTO.toString().toLowerCase())) {
					rataRidotto = rata1;
				}
				
				if(rata1.getTipo().equals(TipoSogliaVersamento.SCONTATO.toString().toLowerCase())) {
					rataScontato = rata1;
				}
				
				if(rata2.getTipo().equals(TipoSogliaVersamento.RIDOTTO.toString().toLowerCase())) {
					rataRidotto = rata2;
				}
				
				if(rata2.getTipo().equals(TipoSogliaVersamento.SCONTATO.toString().toLowerCase())) {
					rataScontato = rata2;
				}
				
				// riporto gli unici campi che sono differenti tra le due rate e creo la pagina unica
				rataScontato.setCodiceAvviso2(rataRidotto.getCodiceAvviso2());
				rataScontato.setQrCode2(rataRidotto.getQrCode2());
				rataScontato.setImportoRidotto(rataRidotto.getImportoRidotto());
				
				PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
				pagina.setRata(rataScontato);
				input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
			} else { // due pagine
				PaginaAvvisoSingola pagina1 = new PaginaAvvisoSingola();
				pagina1.setRata(rata1);
				PaginaAvvisoSingola pagina2 = new PaginaAvvisoSingola();
				pagina2.setRata(rata2);
				input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina1);
				input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina2);
			}
		}

		while(versamenti.size() > 0 && versamenti.get(0).getNumeroRata() == null && versamenti.get(0).getTipoSoglia() == null) {
			Versamento versamento = versamenti.remove(0);
			AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			AvvisoPagamentoUtils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input, sdfDataScadenza));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		while(versamenti.size() > 1 && versamenti.size()%3 != 0) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(v2, documento.getDominio(configWrapper), v2.getUo(configWrapper), input);
			AvvisoPagamentoUtils.impostaAnagraficaDebitore(v2.getAnagraficaDebitore(), input);
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			pagina.getRata().add(getRata(v1, input, sdfDataScadenza));
			pagina.getRata().add(getRata(v2, input, sdfDataScadenza));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		while(versamenti.size() > 1) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			Versamento v3 = versamenti.remove(0);
			AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(v3, documento.getDominio(configWrapper), v3.getUo(configWrapper), input);
			AvvisoPagamentoUtils.impostaAnagraficaDebitore(v3.getAnagraficaDebitore(), input);
			PaginaAvvisoTripla pagina = new PaginaAvvisoTripla();
			pagina.getRata().add(getRata(v1, input, sdfDataScadenza));
			pagina.getRata().add(getRata(v2, input, sdfDataScadenza));
			pagina.getRata().add(getRata(v3, input, sdfDataScadenza));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}

		if(versamenti.size() == 1) {
			Versamento versamento = versamenti.remove(0);
			AvvisoPagamentoUtils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			AvvisoPagamentoUtils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input, sdfDataScadenza));
			input.getPagine().getSingolaOrDoppiaOrTripla().add(pagina);
		}
		
		return input;
	}

	public static RataAvviso getRata(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input, SimpleDateFormat sdfDataScadenza) throws ServiceException, UtilsException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RataAvviso rata = new RataAvviso();
		if(versamento.getNumeroRata() != null)
			rata.setNumeroRata(BigInteger.valueOf(versamento.getNumeroRata()));

		
		if(versamento.getTipoSoglia() != null) {
			rata.setTipo(versamento.getTipoSoglia().toString().toLowerCase());
		}
		
		if(versamento.getGiorniSoglia() != null) {
			rata.setGiorni(BigInteger.valueOf(versamento.getGiorniSoglia()));
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
			input.setDiPoste(AvvisoPagamentoCostanti.DI_POSTE);
			rata.setDataMatrix(AvvisoPagamentoUtils.creaDataMatrix(versamento.getNumeroAvviso(), AvvisoPagamentoUtils.getNumeroCCDaIban(postale.getCodIban()), 
					versamento.getImportoTotale().doubleValue(),
					input.getCfEnte(),
					input.getCfDestinatario(),
					input.getNomeCognomeDestinatario(),
					input.getOggettoDelPagamento()));
			rata.setNumeroCcPostale(AvvisoPagamentoUtils.getNumeroCCDaIban(postale.getCodIban()));
			if(StringUtils.isBlank(postale.getIntestatario()))
				input.setIntestatarioContoCorrentePostale(input.getEnteCreditore());
			else 
				input.setIntestatarioContoCorrentePostale(postale.getIntestatario());
			rata.setCodiceAvvisoPostale(rata.getCodiceAvviso()); 
			rata.setAutorizzazione(AvvisoPagamentoUtils.getAutorizzazionePoste(versamento.getDominio(configWrapper).getAutStampaPoste(), postale.getAutStampaPoste()));
		} else {
			input.setDelTuoEnte(AvvisoPagamentoCostanti.DEL_TUO_ENTE_CREDITORE);
		}

		if(versamento.getImportoTotale() != null)
			rata.setImporto(versamento.getImportoTotale().doubleValue());

		if(versamento.getDataValidita() != null) {
			rata.setData(sdfDataScadenza.format(versamento.getDataValidita()));
		} else if(versamento.getDataScadenza() != null) {
			rata.setData(sdfDataScadenza.format(versamento.getDataScadenza()));
		} else {
			rata.setData(null); 
		}

		it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));
		if(iuvGenerato.getQrCode() != null)
			rata.setQrCode(new String(iuvGenerato.getQrCode()));
		
		// controllo se sono nel caso violazioneCDS allora devo impostare correttamente importo. numero avviso e qr
		if(versamento.getGiorniSoglia() != null && versamento.getTipoSoglia() != null) {
			if(versamento.getTipoSoglia().equals(TipoSogliaVersamento.RIDOTTO)
					|| versamento.getTipoSoglia().equals(TipoSogliaVersamento.SCONTATO)) {
				
				Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(LabelAvvisiProperties.DEFAULT_PROPS);
				
				input.setScadenzaRidotto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_VIOLAZIONE_CDS_SCADENZA_RIDOTTO));
				input.setScadenzaScontato(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_VIOLAZIONE_CDS_SCADENZA_SCONTATO));
				
				if(versamento.getTipoSoglia().equals(TipoSogliaVersamento.RIDOTTO)) {
					if(versamento.getImportoTotale() != null)
						rata.setImportoRidotto(versamento.getImportoTotale().doubleValue());
					
					if(iuvGenerato.getQrCode() != null)
						rata.setQrCode2(new String(iuvGenerato.getQrCode()));
					
					rata.setCodiceAvviso2(rata.getCodiceAvviso());
				}
				
				if(versamento.getTipoSoglia().equals(TipoSogliaVersamento.SCONTATO)) {
					if(versamento.getImportoTotale() != null)
						rata.setImportoScontato(versamento.getImportoTotale().doubleValue());
				}
			}
		}

		return rata;
	}

	public static void impostaAnagraficaEnteCreditore(Versamento versamento, Dominio dominio, UnitaOperativa uo, AvvisoPagamentoInput input)
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

		input.setInfoEnte(sb.toString());
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		if(VersamentoUtils.isPendenzaMultibeneficiario(versamento, configWrapper)) {
			// se il versamento e' multibeneficiario inserisco anche il logo del primo dominio diverso che trovo
			for (SingoloVersamento sv : versamento.getSingoliVersamenti(configWrapper)) {
				Dominio dominioSingoloVersamento = VersamentoUtils.getDominioSingoloVersamento(sv, dominio, configWrapper);
				if(dominioSingoloVersamento != null && !dominioSingoloVersamento.getCodDominio().equals(dominio.getCodDominio())) {
					if(dominioSingoloVersamento.getLogo() != null && dominioSingoloVersamento.getLogo().length > 0)
					input.setLogoEnteSecondario(new String(dominioSingoloVersamento.getLogo()));
					break;
				}
			}
		}
		return;
	}

	public static void impostaAnagraficaDebitore(Anagrafica anagraficaDebitore, AvvisoPagamentoInput input) {
		if(anagraficaDebitore != null) {
			String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
			String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
			String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
			String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
			String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoDestinatario = StringUtils.isNotEmpty(indirizzoDebitore) ? indirizzoDebitore + " " + civicoDebitore : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCittaDebitore = StringUtils.isNotEmpty(localitaDebitore) ? (capDebitore + " " + localitaDebitore + provinciaDebitore) : "";

			input.setNomeCognomeDestinatario(anagraficaDebitore.getRagioneSociale());
			input.setCfDestinatario(anagraficaDebitore.getCodUnivoco().toUpperCase());

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

	public static String splitString(String start) {
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


	public static String creaDataMatrix(String numeroAvviso, String numeroCC, double importo, String codDominio, String cfDebitore, String denominazioneDebitore, String causale) {
		
		
		String importoInCentesimi = getImportoInCentesimi(importo);
		String codeLine = createCodeLine(numeroAvviso, numeroCC, importoInCentesimi);
		//		log.debug("CodeLine ["+codeLine+"] Lunghezza["+codeLine.length()+"]");
		

		String cfDebitoreFilled = getCfDebitoreFilled(cfDebitore);
		
		String denominazioneDebitoreASCII = Normalizer.normalize(denominazioneDebitore, Normalizer.Form.NFD);
		denominazioneDebitoreASCII = denominazioneDebitoreASCII.replaceAll("[^\\x00-\\x7F]", "");
		String denominazioneDebitoreFilled = getDenominazioneDebitoreFilled(denominazioneDebitoreASCII);
		
		String causaleASCII = Normalizer.normalize(causale, Normalizer.Form.NFD);
		causaleASCII = causaleASCII.replaceAll("[^\\x00-\\x7F]", "");
		String causaleFilled = getCausaleFilled(causaleASCII);

		String dataMatrix = MessageFormat.format(AvvisoPagamentoCostanti.PATTERN_DATAMATRIX, codeLine, codDominio, cfDebitoreFilled, denominazioneDebitoreFilled, causaleFilled, AvvisoPagamentoCostanti.FILLER_DATAMATRIX);
		//		log.debug("DataMatrix ["+dataMatrix+"] Lunghezza["+dataMatrix.length()+"]"); 
		return dataMatrix;
	}

	public static String createCodeLine(String numeroAvviso, String numeroCC, String importoInCentesimi) {
		return MessageFormat.format(AvvisoPagamentoCostanti.PATTERN_CODELINE, numeroAvviso,numeroCC,importoInCentesimi);
	}

	public static String fillSx(String start, String charToFillWith, int lunghezza) {
		int iterazioni = lunghezza - start.length();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < iterazioni; i++) {
			sb.append(charToFillWith);
		}
		sb.append(start);

		return sb.toString();
	}

	public static String fillDx(String start, String charToFillWith, int lunghezza) {
		int iterazioni = lunghezza - start.length();

		StringBuilder sb = new StringBuilder();

		sb.append(start);
		for (int i = 0; i < iterazioni; i++) {
			sb.append(charToFillWith);
		}

		return sb.toString();
	}

	public static String getNumeroCCDaIban(String iban) {
		return iban.substring(iban.length() - 12, iban.length());
	}

	public static String getImportoInCentesimi(double importo) {
		int tmpImporto = (int) (importo  * 100);
		String stringImporto = Integer.toString(tmpImporto);

		if(stringImporto.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO)
			return stringImporto.toUpperCase();

		if(stringImporto.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO) {
			return stringImporto.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO).toUpperCase();
		}


		return fillSx(stringImporto, "0", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_IMPORTO).toUpperCase();
	}

	public static String getCfDebitoreFilled(String cfDebitore) {
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
	public static String getDenominazioneDebitoreFilled(String denominazioneDebitore) {
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
	public static String getCausaleFilled(String causale) {
		if(causale.length() == AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE)
			return causale.toUpperCase();

		if(causale.length() > AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE) {
			return causale.substring(0, AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE).toUpperCase();
		}


		return fillDx(causale, " ", AvvisoPagamentoCostanti.DATAMATRIX_LUNGHEZZA_CAMPO_CAUSALE).toUpperCase();
	}
	
	
	/***
	 * Restituisce la stringa con l'autorizzazione da includere nel bollettino postale
	 * 
	 * @param autDominio
	 * @param autIban
	 * @return
	 */
	public static String getAutorizzazionePoste(String autDominio, String autIban) {
		if(StringUtils.isNotBlank(autIban))
			return autIban;
		
		return autDominio;
		
	}
}
