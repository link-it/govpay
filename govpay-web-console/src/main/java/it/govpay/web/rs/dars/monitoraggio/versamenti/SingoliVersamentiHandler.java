package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.pagamento.SingoliVersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.web.rs.BaseRsService;
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

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";

	public SingoliVersamentiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			URI esportazione = null;
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso...");
			
			Versamenti versamentiDars = new  Versamenti();
			String versamentoId = Utils.getInstance().getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".idVersamento.id");
			String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);
			
			boolean eseguiRicerca = isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
			if(!isAdmin){
				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				VersamentiBD versamentiBD = new VersamentiBD(bd);
				VersamentoFilter filter = versamentiBD.newFilter();
				filter.setIdApplicazioni(operatore.getIdApplicazioni());
				filter.setIdUo(operatore.getIdEnti()); 
				
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
				fsw.setSortOrder(SortOrder.DESC);
				filter.getFilterSortList().add(fsw);
				
				long count = eseguiRicerca ? versamentiBD.count(filter) : 0;
				filter.setIdVersamento(Long.parseLong(idVersamento));
				
				eseguiRicerca = eseguiRicerca && count > 0;
			}

			SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);

			List<SingoloVersamento> singoliVersamenti = singoliVersamentiBD.getSingoliVersamenti(Long.parseLong(idVersamento));
			
			long count = singoliVersamenti != null ? singoliVersamenti.size() : 0;

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			if(singoliVersamenti != null && singoliVersamenti.size() > 0){
				for (SingoloVersamento entry : singoliVersamenti) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder));
				}
			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return elenco;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
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
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			@SuppressWarnings("unused")
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			// recupero oggetto
			SingoliVersamentiBD versamentiBD = new SingoliVersamentiBD(bd);
			SingoloVersamento versamento = versamentiBD.getSingoloVersamento(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(versamento), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 
			
			if(StringUtils.isNotEmpty(versamento.getCodSingoloVersamentoEnte())) 
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"), versamento.getCodSingoloVersamentoEnte());
			// Tributo
			Tributo tributo = versamento.getTributo(bd);
			if(tributo != null)
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tributo.label"), tributo.getCodTributo());  
			
			if(versamento.getStatoSingoloVersamento() != null)
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoSingoloVersamento.label"), versamento.getStatoSingoloVersamento().toString());
			if(versamento.getImportoSingoloVersamento() != null)
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoSingoloVersamento.label"), versamento.getImportoSingoloVersamento().toString()+ " Euro");
			if(StringUtils.isNotEmpty(versamento.getProvinciaResidenza())) 
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".provinciaResidenza.label"), versamento.getProvinciaResidenza());
			if(versamento.getTipoBollo() != null) 
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoBollo.label"), versamento.getTipoBollo().name());
			
			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(SingoloVersamento entry) {
		StringBuilder sb = new StringBuilder();
		
		BigDecimal importoTotale = entry.getImportoSingoloVersamento();
		String codVersamentoEnte = entry.getCodSingoloVersamentoEnte();
		
		sb.append("Versamento ").append(codVersamentoEnte).append(" di ").append(importoTotale).append(" Euro");
		
		return sb.toString();
	}

	@Override
	public String getSottotitolo(SingoloVersamento entry) {
		StringBuilder sb = new StringBuilder();
		
		StatoSingoloVersamento statoVersamento = entry.getStatoSingoloVersamento();
		sb.append("Stato ").append(statoVersamento); 
		
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
