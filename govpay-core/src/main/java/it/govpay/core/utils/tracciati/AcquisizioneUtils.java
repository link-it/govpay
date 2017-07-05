package it.govpay.core.utils.tracciati;

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
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneRequest;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.OperazioneNonValidaRequest;
import it.govpay.core.utils.tracciati.operazioni.OperazioneNonValidaResponse;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;

public class AcquisizioneUtils {


	private Logger logger = LogManager.getLogger(AcquisizioneUtils.class);
	private Format formatW;
	private Parser caricamentoParser;
	private Parser annullamentoParser;
	private Parser caricamentoResponseParser;
	private Parser annullamentoResponseParser;
	private String delimiter;

	public AcquisizioneUtils() {
		try{
			FormatReader formatReader = new FormatReader(CSVReaderProperties.getInstance(logger).getProperties());
			this.formatW = formatReader.getFormat();
			this.delimiter = "" + this.formatW.getCsvFormat().getDelimiter();
			this.caricamentoParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/caricamento.mapping.properties"), true);
			this.annullamentoParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/annullamento.mapping.properties"), true);
			this.caricamentoResponseParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/caricamento.response.mapping.properties"), true);
			this.annullamentoResponseParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/annullamento.response.mapping.properties"), true);
		}catch(Exception e){
			logger.error("Errore durante l'inizializzazione di AcquisizioneUtils: " + e.getMessage(),e);
		}

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

	public AbstractOperazioneRequest acquisisci(byte[] linea, Tracciato tracciato, long numLinea) throws ServiceException {
		AbstractOperazioneRequest request = this.parseLineaOperazioneRequest(linea);
		request.setIdTracciato(tracciato.getId());
		request.setLinea(numLinea);
		request.setDati(linea);

		return request;
	}

	public AbstractOperazioneRequest parseLineaOperazioneRequest(byte[] linea) throws ServiceException {

		String lineaString = new String(linea);

		if(lineaString == null || "".equals(lineaString))
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");

		String[] lineaSplitted = lineaString.split(this.delimiter);
		if(lineaSplitted.length < 0) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		String op = lineaSplitted[0];

		Parser parser = null;
		if("ADD".equals(op)) {
			parser = caricamentoParser;
		} else if("DEL".equals(op)) {
			parser = annullamentoParser;
		} else {
			return getOperazioneNonValida(CostantiCaricamento.OPERAZIONE_NON_SUPPORTATA, "Codice operazione "+op+" non supportata");
		}

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(this.formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		
		if(parserResult.getRecords().size() > 1) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record multipli trovati");
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

		String[] lineaSplitted = lineaString.split(this.delimiter);
		if(lineaSplitted.length < 0) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		Parser parser = null;
		if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
			parser = caricamentoResponseParser;
		} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
			parser = annullamentoResponseParser;
		} else {
			return getOperazioneNonValidaResponse(CostantiCaricamento.OPERAZIONE_NON_SUPPORTATA, "Tipo operazione "+tipoOperazione+" non supportata");
		}

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(this.formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return null;

		if(parserResult.getRecords().size() > 1) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record multipli trovati");
		}

		try {
			if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
				return new CaricamentoResponse(parserResult.getRecords().get(0));
			} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
				return new AnnullamentoResponse(parserResult.getRecords().get(0));
			} else {
				return getOperazioneNonValidaResponse(CostantiCaricamento.OPERAZIONE_NON_SUPPORTATA, "Tipo operazione "+tipoOperazione+" non supportata");
			}
		} catch(ValidationException e) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

	}

	private OperazioneNonValidaRequest getOperazioneNonValida(String codice, String dettaglio) {
		try {
			OperazioneNonValidaRequest request = new OperazioneNonValidaRequest();
			request.setCodiceErrore(codice);
			request.setDettaglioErrore(dettaglio);
			return request;
		} catch(ValidationException e) {
			logger.error("Errore durante l'inizializzazione di una operazione non valida: " + e.getMessage(), e);	
		}
		return null;
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
	public AbstractOperazioneResponse acquisisciResponse(AbstractOperazioneRequest request, Tracciato tracciato, BasicBD bd) throws ServiceException {
		AbstractOperazioneResponse response = null;

		if(request instanceof CaricamentoRequest) {
			CaricamentoRequest caricamentoRequest = (CaricamentoRequest) request;
			response = this.caricaVersamento(tracciato, caricamentoRequest, bd);
		} else if(request instanceof AnnullamentoRequest) {
			AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) request;
			response = this.annullaVersamento(tracciato, annullamentoRequest, bd);
		} else  if(request instanceof OperazioneNonValidaRequest) {
			OperazioneNonValidaRequest operazioneNonValidaRequest = (OperazioneNonValidaRequest) request;
			response = new OperazioneNonValidaResponse();
			response.setStato(StatoOperazioneType.NON_VALIDO);
			response.setEsito(operazioneNonValidaRequest.getCodiceErrore());
			response.setDescrizioneEsito(operazioneNonValidaRequest.getDettaglioErrore());
		}

		response.setDelim(this.delimiter);

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
		} catch(GovPayException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(e.getCodEsito().name());
			caricamentoResponse.setDescrizioneEsito(e.getMessage());
		} catch(NotAuthorizedException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			caricamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "");
		}

		return caricamentoResponse;
	}

	private AnnullamentoResponse annullaVersamento(Tracciato tracciato, AnnullamentoRequest request, BasicBD basicBD) throws ServiceException {


		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();

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
		} catch(GovPayException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito(e.getCodEsito().name());
			annullamentoResponse.setDescrizioneEsito(e.getMessage());
		} catch(NotAuthorizedException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "");
		}
		return annullamentoResponse;

	}


}
