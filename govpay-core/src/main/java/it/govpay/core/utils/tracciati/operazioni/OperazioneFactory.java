package it.govpay.core.utils.tracciati.operazioni;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Format;
import org.openspcoop2.utils.csv.FormatReader;
import org.openspcoop2.utils.csv.Parser;
import org.openspcoop2.utils.csv.ParserResult;
import org.openspcoop2.utils.csv.Record;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Tracciato;
import it.govpay.core.business.PagamentiAttesa;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.tracciati.CSVReaderProperties;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;

public class OperazioneFactory {

	private static Logger logger = LogManager.getLogger(OperazioneFactory.class);
	private static Format formatW;
	private static Parser caricamentoParser;
	private static Parser annullamentoParser;
	private static Parser caricamentoResponseParser;
	private static Parser annullamentoResponseParser;
	private static String delimiter;

	public static void init() throws UtilsException, Exception {
		FormatReader formatReader = new FormatReader(CSVReaderProperties.getInstance(logger).getProperties());
		formatW = formatReader.getFormat();
		delimiter = "" + formatW.getCsvFormat().getDelimiter();
		caricamentoParser = new Parser(OperazioneFactory.class.getResourceAsStream("/caricamento.mapping.properties"), true);
		annullamentoParser = new Parser(OperazioneFactory.class.getResourceAsStream("/annullamento.mapping.properties"), true);
		caricamentoResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/caricamento.response.mapping.properties"), true);
		annullamentoResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/annullamento.response.mapping.properties"), true);
	}

	public AbstractOperazioneRequest acquisisci(Record record, String op) throws ValidationException {
		try {
			if("ADD".equals(op)) {
				return new CaricamentoRequest(record);
			} else if("DEL".equals(op)) {
				return new AnnullamentoRequest(record);
			} else {
				throw new ValidationException("Codice operazione "+op+" non supportata");
			}
		} catch(ValidationException e) {
			throw new ValidationException(CostantiCaricamento.ERRORE_SINTASSI + " : " + e.getMessage());
		}
	}

	public AbstractOperazioneRequest acquisisci(byte[] linea, long idtracciato, long numLinea) throws ServiceException {
		AbstractOperazioneRequest request = parseLineaOperazioneRequest(linea);
		request.setIdTracciato(idtracciato);
		request.setLinea(numLinea);
		request.setDati(linea);
		return request;
	}

	public AbstractOperazioneRequest parseLineaOperazioneRequest(byte[] linea) throws ServiceException {

		String lineaString = new String(linea);

		if(lineaString == null || lineaString.trim().isEmpty())
			return getOperazioneNonValida(CostantiCaricamento.EMPTY, "Linea vuota");

		String[] lineaSplitted = lineaString.split(delimiter);
		String op = lineaSplitted[0];

		Parser parser = null;
		if("ADD".equals(op)) {
			parser = caricamentoParser;
		} else if("DEL".equals(op)) {
			parser = annullamentoParser;
		} else {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Codice operazione "+op+" non supportata");
		}

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Errore sintassi sconosciuto");

		if(parserResult.getRecords().size() > 1) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Errore sintassi sconosciuto");
		}

		try {
			return acquisisci(parserResult.getRecords().get(0), op);
		} catch(ValidationException e) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

	}

	public AbstractOperazioneResponse parseLineaOperazioneResponse(TipoOperazioneType tipoOperazione, byte[] linea) throws ServiceException {

		String lineaString = new String(linea);

		if(lineaString == null || "".equals(lineaString))
			return null;

		String[] lineaSplitted = lineaString.split(delimiter);
		if(lineaSplitted.length < 0) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Errore sintassi sconosciuto: risposta vuota");
		}

		Parser parser = null;
		if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
			parser = caricamentoResponseParser;
		} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
			parser = annullamentoResponseParser;
		} else {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" non supportata");
		}

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return null;

		if(parserResult.getRecords().size() > 1) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Errore sintassi sconosciuto");
		}

		try {
			if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
				return new CaricamentoResponse(parserResult.getRecords().get(0));
			} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
				return new AnnullamentoResponse(parserResult.getRecords().get(0));
			} else {
				return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" non supportata");
			}
		} catch(ValidationException e) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

	}

	private OperazioneNonValidaRequest getOperazioneNonValida(String codice, String dettaglio) {
		OperazioneNonValidaRequest request = new OperazioneNonValidaRequest();
		request.setCodiceErrore(codice);
		request.setDettaglioErrore(dettaglio);
		return request;
	}

	private OperazioneNonValidaResponse getOperazioneNonValidaResponse(String codice, String dettaglio) {
		OperazioneNonValidaResponse response = new OperazioneNonValidaResponse();
		response.setEsito(codice);
		response.setDescrizioneEsito(dettaglio);
		response.setStato(StatoOperazioneType.NON_VALIDO);
		return response;
	}

	/**
	 * @param request
	 * @param tracciato
	 * @param bd
	 * @return
	 * @throws Exception 
	 */
	public AbstractOperazioneResponse eseguiOperazione(AbstractOperazioneRequest request, Tracciato tracciato, BasicBD bd) throws ServiceException {
		AbstractOperazioneResponse response = null;

		if(request instanceof CaricamentoRequest) {
			CaricamentoRequest caricamentoRequest = (CaricamentoRequest) request;
			response = caricaVersamento(tracciato, caricamentoRequest, bd);
		} else if(request instanceof AnnullamentoRequest) {
			AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) request;
			response = annullaVersamento(tracciato, annullamentoRequest, bd);
		} else  if(request instanceof OperazioneNonValidaRequest) {
			OperazioneNonValidaRequest operazioneNonValidaRequest = (OperazioneNonValidaRequest) request;
			response = new OperazioneNonValidaResponse();
			response.setStato(StatoOperazioneType.NON_VALIDO);
			response.setEsito(operazioneNonValidaRequest.getCodiceErrore());
			response.setDescrizioneEsito(operazioneNonValidaRequest.getDettaglioErrore());
		}

		response.setDelim(delimiter);

		return response;
	}

	/**
	 * @param request
	 * @return
	 */
	public String getCodiceApplicazione(AbstractOperazioneRequest request) {
		if(request instanceof CaricamentoRequest) {
			return ((CaricamentoRequest)request).getCodApplicazione();
		} else if(request instanceof AnnullamentoRequest) {
			return ((AnnullamentoRequest) request).getCodApplicazione();
		} else {
			return null;
		}
	}
	/**
	 * @param request
	 * @return
	 */
	public String getCodVersamentoEnte(AbstractOperazioneRequest request) {
		if(request instanceof CaricamentoRequest) {
			return ((CaricamentoRequest)request).getCodVersamentoEnte();
		} else if(request instanceof AnnullamentoRequest) {
			return ((AnnullamentoRequest) request).getCodVersamentoEnte();
		} else {
			return null;
		}
	}


	private CaricamentoResponse caricaVersamento(Tracciato tracciato, CaricamentoRequest request, BasicBD basicBD) throws ServiceException {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		caricamentoResponse.setCodApplicazione(request.getCodApplicazione());
		caricamentoResponse.setCodVersamentoEnte(request.getCodVersamentoEnte());
		try {
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(request, basicBD);
			CaricaVersamentoDTO caricaVersamentoDTO = null;

			if(tracciato.getApplicazione(basicBD) != null) {
				caricaVersamentoDTO = new CaricaVersamentoDTO(tracciato.getApplicazione(basicBD), versamentoModel);
			} else {
				caricaVersamentoDTO = new CaricaVersamentoDTO(tracciato.getOperatore(basicBD), versamentoModel);
			}
			caricaVersamentoDTO.setAggiornaSeEsiste(true);
			caricaVersamentoDTO.setGeneraIuv(true);

			PagamentiAttesa pagamentiAttesa = new PagamentiAttesa(basicBD);
			CaricaVersamentoDTOResponse caricaVersamento = pagamentiAttesa.caricaVersamento(caricaVersamentoDTO);
			caricamentoResponse.setBarCode(caricaVersamento.getBarCode());
			caricamentoResponse.setQrCode(caricaVersamento.getQrCode());
			caricamentoResponse.setIuv(caricaVersamento.getIuv());

			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_OK);
		} catch(GovPayException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(e.getCodEsito().name() + ": " + e.getMessage());
		} catch(NotAuthorizedException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
		}

		return caricamentoResponse;
	}

	private AnnullamentoResponse annullaVersamento(Tracciato tracciato, AnnullamentoRequest request, BasicBD basicBD) throws ServiceException {


		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();
		annullamentoResponse.setCodApplicazione(request.getCodApplicazione());
		annullamentoResponse.setCodVersamentoEnte(request.getCodVersamentoEnte());

		try {
			PagamentiAttesa pagamentiAttesa = new PagamentiAttesa(basicBD);
			AnnullaVersamentoDTO annullaVersamentoDTO = null;
			if(tracciato.getApplicazione(basicBD) != null) {
				annullaVersamentoDTO = new AnnullaVersamentoDTO(tracciato.getApplicazione(basicBD), request.getCodApplicazione(), request.getCodVersamentoEnte());
			} else {
				annullaVersamentoDTO = new AnnullaVersamentoDTO(tracciato.getOperatore(basicBD), request.getCodApplicazione(), request.getCodVersamentoEnte());
			}

			annullaVersamentoDTO.setMotivoAnnullamento(request.getMotivoAnnullamento()); 
			pagamentiAttesa.annullaVersamento(annullaVersamentoDTO);

			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			annullamentoResponse.setEsito("DEL_OK");
			annullamentoResponse.setDescrizioneEsito("Pagamento in Attesa [App:" + request.getCodApplicazione() + " Id:" + request.getCodVersamentoEnte() + "] eliminato con successo");
		} catch(GovPayException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(e.getCodEsito().name());
			annullamentoResponse.setDescrizioneEsito(e.getCodEsito().name() + ": " +e.getMessage());
		} catch(NotAuthorizedException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
		}
		return annullamentoResponse;

	}


}
