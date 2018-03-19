package it.govpay.core.utils.tracciati.operazioni;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Format;
import org.openspcoop2.utils.csv.FormatReader;
import org.openspcoop2.utils.csv.Parser;
import org.openspcoop2.utils.csv.ParserResult;
import org.openspcoop2.utils.csv.Record;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.core.business.Incassi;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.dao.versamenti.PagamentiAttesaDAO;
import it.govpay.core.dao.versamenti.dto.CaricaVersamentoDTO;
import it.govpay.core.dao.versamenti.dto.CaricaVersamentoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.tracciati.CSVReaderProperties;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.TipoTracciatoType;

public class OperazioneFactory {

	private static Logger logger = LoggerWrapperFactory.getLogger(OperazioneFactory.class);
	private static Format formatW;
	private static Parser caricamentoParser;
	private static Parser caricamentoKoResponseParser;
	private static Parser caricamentoOkResponseParser;
	private static Parser annullamentoParser;
	private static Parser annullamentoOkResponseParser;
	private static Parser annullamentoKoResponseParser;
	private static Parser incassoParser;
	private static Parser incassoOkResponseParser;
	private static Parser incassoKoResponseParser;
	private static String delimiter;

	public static void init() throws UtilsException, Exception {
		FormatReader formatReader = new FormatReader(CSVReaderProperties.getInstance(logger).getProperties());
		formatW = formatReader.getFormat();
		delimiter = "" + formatW.getCsvFormat().getDelimiter();
		caricamentoParser = new Parser(OperazioneFactory.class.getResourceAsStream("/caricamento.mapping.properties"), true);
		annullamentoParser = new Parser(OperazioneFactory.class.getResourceAsStream("/annullamento.mapping.properties"), true);
		incassoParser = new Parser(OperazioneFactory.class.getResourceAsStream("/incasso.mapping.properties"), true);
		caricamentoOkResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/caricamento.response.ok.mapping.properties"), true);
		caricamentoKoResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/caricamento.response.ko.mapping.properties"), true);
		incassoOkResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/incasso.response.ok.mapping.properties"), true);
		incassoKoResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/incasso.response.ko.mapping.properties"), true);
		annullamentoOkResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/annullamento.response.ok.mapping.properties"), true);
		annullamentoKoResponseParser = new Parser(OperazioneFactory.class.getResourceAsStream("/annullamento.response.ko.mapping.properties"), true);
	}

	public AbstractOperazioneRequest acquisisci(Record record, TipoTracciatoType tipoTracciato, String op) throws ValidationException {
		try {
			if(tipoTracciato.equals(TipoTracciatoType.VERSAMENTI)) {
				if("ADD".equals(op)) {
					return new CaricamentoRequest(record);
				} else if("DEL".equals(op)) {
					return new AnnullamentoRequest(record);
				} else {
					throw new ValidationException("Codice operazione "+op+" non supportata per il tipo tracciato " + tipoTracciato);
				}
			} else if(tipoTracciato.equals(TipoTracciatoType.INCASSI)) {
				if("INC".equals(op)) {
					return new IncassoRequest(record);
				} else {
					throw new ValidationException("Codice operazione "+op+" non supportata per il tipo tracciato " + tipoTracciato);
				}
			} else {
				throw new ValidationException("Tipo tracciato "+tipoTracciato+" non supportato");
			}
		} catch(ValidationException e) {
			throw new ValidationException(CostantiCaricamento.ERRORE_SINTASSI + " : " + e.getMessage());
		}
	}

	public AbstractOperazioneRequest acquisisci(TipoTracciatoType tipoTracciato, byte[] linea, long idtracciato, long numLinea) throws ServiceException {
		AbstractOperazioneRequest request = parseLineaOperazioneRequest(tipoTracciato, linea);
		request.setIdTracciato(idtracciato);
		request.setLinea(numLinea);
		request.setDati(linea);
		return request;
	}

	public AbstractOperazioneRequest parseLineaOperazioneRequest(TipoTracciatoType tipoTracciato, byte[] linea) throws ServiceException {

		String lineaString = new String(linea);

		if(lineaString == null || lineaString.trim().isEmpty())
			return getOperazioneNonValida(CostantiCaricamento.EMPTY, "Linea vuota");

		String[] lineaSplitted = lineaString.split(delimiter);
		String op = lineaSplitted[0];

		Parser parser = null;
		if(tipoTracciato.equals(TipoTracciatoType.VERSAMENTI)) {
			if("ADD".equals(op)) {
				parser = caricamentoParser;
			} else if("DEL".equals(op)) {
				parser = annullamentoParser;
			} else {
				return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Codice operazione "+op+" non supportata per il tipo tracciato " + tipoTracciato);
			}
		} else if(tipoTracciato.equals(TipoTracciatoType.INCASSI)) {
			if("INC".equals(op)) {
				parser = incassoParser;
			} else {
				return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Codice operazione "+op+" non supportata per il tipo tracciato " + tipoTracciato);
			}			
		} else {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Tipo tracciato "+tipoTracciato+" non supportato");
		}
 

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");

		if(parserResult.getRecords().size() > 1) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record multipli trovati");
		}

		try {
			return acquisisci(parserResult.getRecords().get(0), tipoTracciato, op);
		} catch(ValidationException e) {
			return getOperazioneNonValida(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato: " + e.getMessage());
		}

	}

	public AbstractOperazioneResponse parseLineaOperazioneResponse(TipoOperazioneType tipoOperazione, StatoOperazioneType statoOperazione, byte[] linea) throws ServiceException {

		String lineaString = new String(linea);

		if(lineaString == null || "".equals(lineaString))
			return null;

		String[] lineaSplitted = lineaString.split(delimiter);
		if(lineaSplitted.length < 0) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		Parser parser = null;
		if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
			if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_OK))
				parser = caricamentoOkResponseParser;
			else if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_KO))
				parser = caricamentoKoResponseParser;
			else 
				return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" in stato "+statoOperazione+" non supportata");
		} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
			if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_OK))
				parser = annullamentoOkResponseParser;
			else if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_KO))
				parser = annullamentoKoResponseParser;
			else 
				return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" in stato "+statoOperazione+" non supportata");
		} else if(tipoOperazione.equals(TipoOperazioneType.INC)) {
			if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_OK))
				parser = incassoOkResponseParser;
			else if(statoOperazione.equals(StatoOperazioneType.ESEGUITO_KO))
				parser = incassoKoResponseParser;
			else 
				return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" in stato "+statoOperazione+" non supportata");
		} else {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Tipo operazione "+tipoOperazione+" non supportata");
		}

		ParserResult parserResult = null;
		try {
			parserResult = parser.parseCsvFile(formatW, linea);
		} catch(UtilsException e) {
			return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record non correttamente formato");
		}

		if(parserResult.getRecords() == null || parserResult.getRecords().size() == 0)
			return null;

		if(parserResult.getRecords().size() > 1) {
			if(!tipoOperazione.equals(TipoOperazioneType.INC)) {
				return getOperazioneNonValidaResponse(CostantiCaricamento.ERRORE_SINTASSI, "Record multipli trovati");
			}
		}

		try {
			if(tipoOperazione.equals(TipoOperazioneType.ADD)) {
				return new CaricamentoResponse(parserResult.getRecords().get(0));
			} else if(tipoOperazione.equals(TipoOperazioneType.DEL)) {
				return new AnnullamentoResponse(parserResult.getRecords().get(0));
			} else if(tipoOperazione.equals(TipoOperazioneType.INC)) {
				return new IncassoResponse(parserResult.getRecords());
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
		} else if(request instanceof IncassoRequest) {
			IncassoRequest incassoRequest = (IncassoRequest) request;
			response = eseguiIncasso(tracciato, incassoRequest, bd);
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

			PagamentiAttesaDAO pagamentiAttesa = new PagamentiAttesaDAO(basicBD);
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
			PagamentiAttesaDAO pagamentiAttesa = new PagamentiAttesaDAO(basicBD);
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

	private IncassoResponse eseguiIncasso(Tracciato tracciato, IncassoRequest request, BasicBD bd) throws ServiceException {


		Incassi incassi = new Incassi(bd);
		RichiestaIncassoDTO richiestaIncasso = new RichiestaIncassoDTO(null);
		richiestaIncasso.setCausale(request.getCausale());
		richiestaIncasso.setImporto(new BigDecimal(request.getImporto(), MathContext.DECIMAL64));
		richiestaIncasso.setDataContabile(request.getDataContabile());
		richiestaIncasso.setTrn(request.getTrn());

		richiestaIncasso.setCodDominio(request.getDominio());
		richiestaIncasso.setDataValuta(request.getDataValuta());
		richiestaIncasso.setDispositivo(request.getDispositivo());
		richiestaIncasso.setOperatore(tracciato.getOperatore(bd)); // TODO cosa succede se l'operatore ha solo ACL per ruolo??
		
		IncassoResponse response = new IncassoResponse();

		RichiestaIncassoDTOResponse richiestaIncassoResponse = null;
		try {
			richiestaIncassoResponse = incassi.richiestaIncasso(richiestaIncasso);
		} catch (IncassiException e) {
			response.setTrn(request.getTrn());
			response.setDominio(request.getDominio());
			response.setFaultCode(e.getCode());
			response.setFaultString(e.getMessage());
			response.setFaultDescription(e.getDetails());
			response.setEsito(IncassoResponse.ESITO_INC_KO);
			response.setStato(StatoOperazioneType.ESEGUITO_KO);
		} catch (NotAuthorizedException e) {
			response.setTrn(request.getTrn());
			response.setDominio(request.getDominio());
			response.setFaultCode("INC_KO_NOT_AUTH");
			response.setFaultString("Non autorizzato");
			response.setFaultDescription(e.getMessage());
			response.setEsito(IncassoResponse.ESITO_INC_KO);
			response.setStato(StatoOperazioneType.ESEGUITO_KO);
		} catch (InternalException e) {
			response.setTrn(request.getTrn());
			response.setFaultCode("INC_KO_INTERNAL");
			response.setFaultString("Internal");
			response.setFaultDescription(e.getMessage());
			response.setEsito(IncassoResponse.ESITO_INC_KO);
			response.setStato(StatoOperazioneType.ESEGUITO_KO);
		}

		if(richiestaIncassoResponse != null) {
			response.setEsito(IncassoResponse.ESITO_INC_OK);
			response.setStato(StatoOperazioneType.ESEGUITO_OK);
			response.setTrn(richiestaIncassoResponse.getIncasso().getTrn());
			response.setDominio(richiestaIncassoResponse.getIncasso().getCodDominio());

			for(Pagamento pagamento: richiestaIncassoResponse.getIncasso().getPagamenti(bd)) {
				SingoloIncassoResponse singoloIncassoResponse = new SingoloIncassoResponse();

				singoloIncassoResponse.setTrn(richiestaIncassoResponse.getIncasso().getTrn());
				singoloIncassoResponse.setDominio(richiestaIncassoResponse.getIncasso().getCodDominio());
				singoloIncassoResponse.setIur(pagamento.getIur());
				singoloIncassoResponse.setIuv(pagamento.getIuv());
				singoloIncassoResponse.setImporto(pagamento.getImportoPagato().doubleValue());
				singoloIncassoResponse.setDataPagamento(pagamento.getDataPagamento());
				singoloIncassoResponse.setCodVersamentoEnte(pagamento.getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
				singoloIncassoResponse.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
				response.add(singoloIncassoResponse);
			}
		}
		
		return response;

	}


}
