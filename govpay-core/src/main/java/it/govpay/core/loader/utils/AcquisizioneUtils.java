package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.model.Dominio;
import it.govpay.core.loader.timers.model.AbstractOperazioneRequest;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.OperazioneNonValidaRequest;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.loader.Tracciato;
import it.govpay.model.TipoTributo;

import java.util.ArrayList;
import java.util.List;

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

public class AcquisizioneUtils {


	private Logger logger = LogManager.getLogger(AcquisizioneUtils.class);
	private Format formatW;
	private Parser caricamentoParser;
	private Parser annullamentoParser;


	public AcquisizioneUtils() {
		try{
			FormatReader formatReader = new FormatReader(CSVReaderProperties.getInstance(logger).getProperties());
			this.formatW = formatReader.getFormat();
			this.caricamentoParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/caricamento.mapping.properties"), true);
			this.annullamentoParser = new Parser(AcquisizioneUtils.class.getResourceAsStream("/annullamento.mapping.properties"), true);
		}catch(Exception e){
			logger.error("Errore durante l'inizializzazione di AcquisizioneUtils: " + e.getMessage(),e);
		}

	}
	public AbstractOperazioneRequest acquisisci(Record record) throws ValidationException, ServiceException {
		AbstractOperazioneRequest request = null;
		String op = record.getRecord().get(0);
		if("ADD".equals(op)) {
			request = new CaricamentoRequest(record);
		} else if("DEL".equals(op)) {
			request = new AnnullamentoRequest(record);
		} else {
			throw new ValidationException("Codice operazione "+op+" non supportata");
		}

		return request;
	}

	private Parser getParser(String op) throws ValidationException {
		if("ADD".equals(op)) {
			return caricamentoParser;
		} else if("DEL".equals(op)) {
			return annullamentoParser;
		} else {
			throw new ValidationException("Codice operazione "+op+" non supportata");
		}

	}

	public AbstractOperazioneRequest acquisisci(byte[] linea, Tracciato tracciato, Operatore operatore, Long numLinea, List<String> domini, List<String> tributi) throws ServiceException {

		AbstractOperazioneRequest request = null;
		try{

			validateACL(operatore, domini, tributi);
			
			String op = linea.toString().split(this.formatW.getCsvFormat().getRecordSeparator())[0]; //TODO trovare modo elegante

			ParserResult parsed = null;
			try {
				parsed = this.getParser(op).parseCsvFile(this.formatW, linea);
			} catch(UtilsException e) {
				throw new ValidationException(e);
			}

			if(parsed.getRecords() == null || parsed.getRecords().size() == 0)
				return null;

			if(parsed.getRecords().size() > 1)
				throw new ValidationException("Record multipli trovati");

			request = acquisisci(parsed.getRecords().get(0));
		} catch(ValidationException e) {
			request = new OperazioneNonValidaRequest();
		} finally {
			request.setIdTracciato(tracciato.getId());
			request.setLinea(numLinea);
			request.setDati(linea);
		}
		
		return request;

	}


	public List<String> getDomini(BasicBD bd) throws ServiceException {
		DominiBD dominiBD = new DominiBD(bd);

		List<Dominio> findAll = dominiBD.findAll(dominiBD.newFilter());

		List<String> getDomini = new ArrayList<String>();
		if(findAll != null && !findAll.isEmpty()) {
			for(Dominio d: findAll) {
				getDomini.add(d.getCodDominio());
			}
		}
		return getDomini;
	}

	public List<String> getTributi(BasicBD bd) throws ServiceException {
		TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);

		List<TipoTributo> findAll = tipiTributoBD.findAll(tipiTributoBD.newFilter());

		List<String> getDomini = new ArrayList<String>();
		if(findAll != null && !findAll.isEmpty()) {
			for(TipoTributo t: findAll) {
				getDomini.add(t.getCodTributo());
			}
		}
		return getDomini;

	}

	private void validateACL(Operatore operatore, List<String> codDomini, List<String> codTributi) throws ServiceException, ValidationException {

		if(ProfiloOperatore.ADMIN.equals(operatore.getProfilo())) {
			return;
		}

		throw new ValidationException("Solo gli operatori con ruolo ["+ProfiloOperatore.ADMIN+"] sono autorizzati a caricare tracciati");

		//		if(!codDomini.contains(codDominio)) {
		//			throw new ValidationException("CodDominio ["+codDominio+"] non esiste");
		//		}
		//		
		//		if(!codTributi.contains(codTributo)) {
		//			throw new ValidationException("Tributo ["+codTributo+"] non esiste");
		//		}
		//		
		//		// Controllo se ho il dominio
		//		boolean isDominioAbilitato = false;
		//		boolean isTributoAbilitato = false;
		//		
		//		for(Acl acl : operatore.getAcls()) {
		//			
		//			if(!isDominioAbilitato && acl.getServizio().equals(Servizio.CARICAMENTO) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
		//				isDominioAbilitato = true;
		//			}
		//			
		//			if(!isTributoAbilitato && acl.getServizio().equals(Servizio.CARICAMENTO) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
		//				isTributoAbilitato = true;
		//			}
		//		}
		//		
		//		if(!(isDominioAbilitato && isTributoAbilitato)) {
		//			throw new ValidationException("Operatore non autorizzato");
		//		}
	}

}
