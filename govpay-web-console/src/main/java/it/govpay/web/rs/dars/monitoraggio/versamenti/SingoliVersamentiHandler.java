package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;

public class SingoliVersamentiHandler extends BaseDarsHandler<SingoloVersamento> implements IDarsHandler<SingoloVersamento>{

	public SingoliVersamentiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public String getTitolo(SingoloVersamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		
		BigDecimal importoTotale = entry.getImportoSingoloVersamento();
		String codVersamentoEnte = entry.getCodSingoloVersamentoEnte();
		
		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle((this.nomeServizio + ".label.titolo"), codVersamentoEnte, (importoTotale + "€")));
		
		return sb.toString();
	}

	@Override
	public String getSottotitolo(SingoloVersamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		
		StatoSingoloVersamento statoVersamento = entry.getStatoSingoloVersamento();
		switch (statoVersamento) {
		case ANOMALO:
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoSingoloVersamento.anomalo"));
			break;
		case NON_ESEGUITO:
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoSingoloVersamento.nonEseguito"));
			break;
		case ESEGUITO:
		default:
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoSingoloVersamento.eseguito"));
			break;
		}
		return sb.toString();
	}
	
	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}
	
	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, SingoloVersamento entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public SingoloVersamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(SingoloVersamento entry, SingoloVersamento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
}
